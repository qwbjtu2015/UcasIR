package com.news.search;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.spell.TermFreqIterator;
import org.apache.lucene.search.suggest.Lookup.LookupResult;
import org.apache.lucene.search.suggest.analyzing.FuzzySuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.news.db.JDBCUtil;
import com.news.index.NewsIndexer;

public class AutoCompleter {
  
	public static final String FIELD = "title";
	public static final String INDEX_DIR = "G:\\MySQLData\\SuggestIndex";
	public static final int RESULTS_TO_DISPLAY = 10;
	private static Connection conn = null;     
    private static Statement stmt = null;     
    private static ResultSet rs = null; 
	  
	private FuzzySuggester suggestor = new FuzzySuggester(AutoCompleter.getAnalyzer());
	
	
	/**
	 * 
	 * @return
	 */
	public static Analyzer getAnalyzer() {
		//Defining a custom analyzer which will be used to index and suggest the data set
		//自定义一个Analyzer，用于检索和建议数据集		
		Analyzer autosuggestAnalyzer = new Analyzer() {
			// 定义停用词表			
			final String [] stopWords =  {"我", "的", "地", "得", "等等", "at", "be", "but", "by",
			"for", "if", "in", "into", "is", "it",
			"no", "not", "of", "on", "or", "s", "such",
			"t", "that", "the", "their", "then", "there", "these",
			"they", "this", "to", "was", "will", "with"};
      
			@Override
			protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
				final Tokenizer tokenizer = new WhitespaceTokenizer(Version.LUCENE_43, reader);
				TokenStream tok = new LowerCaseFilter(Version.LUCENE_43, tokenizer);
				tok = new StopFilter(Version.LUCENE_43, tok, StopFilter.makeStopSet(Version.LUCENE_43, stopWords, true));
				return new TokenStreamComponents(tokenizer, tok) {
					@Override
					protected void setReader(final Reader reader) throws IOException {
						super.setReader(reader);
					}
				};
			}
		};
		return autosuggestAnalyzer;
}
  
	/**
	 * 构建Suggestor
	 * @param indexDir 创建索引的目录
	 * 使用默认的Field
	 * @return
	*/
	public boolean buildSuggestor(String indexDir) {
		try {
			Directory dir = new MMapDirectory(new File(indexDir));
			return buildSuggestor(dir, FIELD);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}  
	}
  
	/**
	 * 
	 * @param indexDir 创建索引的目录
	 * @param fieldName 构建索引的Field
	 * @return
	*/
	public boolean buildSuggestor(String indexDir, String fieldName) {
		try {
			Directory dir = new MMapDirectory(new File(indexDir));
			return buildSuggestor(dir, fieldName);
		} catch (IOException e) {
			e.printStackTrace();
			return false; 
		}
	}

	public boolean buildSuggestor(Directory directory, String fieldName) {
	
		IndexReader reader;
		try {
		      
			reader = DirectoryReader.open(directory);
			AtomicReader aReader = SlowCompositeReaderWrapper.wrap(reader); // Should use reader.leaves instead ?
			Terms terms = aReader.terms(fieldName);
			      
			if (terms == null) 
				return false; 
			      
			TermsEnum termEnum = terms.iterator(null);
			TermFreqIterator wrapper = new TermFreqIterator.TermFreqIteratorWrapper(termEnum);
			      
			suggestor.build(wrapper);
		      
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
  
	public String[] suggest(String q) {
		List<LookupResult> results = suggestor.lookup(CharBuffer.wrap(q), true, AutoCompleter.RESULTS_TO_DISPLAY);
		String[] autosuggestResults = new String[results.size()];
		for(int i=0; i < results.size(); i++) {
			LookupResult result = results.get(i);
			autosuggestResults[i] = result.key.toString();
		}    
		return autosuggestResults;
	}
	
	/**   
	    * 获取数据库数据   
	    * @return ResultSet   
	    * @throws Exception   
	    */    
    private void readDatabase() throws Exception {     
        conn = JDBCUtil.getConnection();     
        if(conn == null) {     
            throw new Exception("数据库连接失败！");     
        }     
        String sql = "SELECT title, content, keyword FROM news";     
        try {     
            stmt = conn.createStatement();     
            rs = stmt.executeQuery(sql);     
            createSuggestIndex(rs);   //给数据库创建索引,此处执行一次，不要每次运行都创建索引，以后数据有更新可以后台调用更新索引     
                
        } catch(Exception e) {     
            e.printStackTrace();     
            throw new Exception("数据库查询sql出错！ sql : " + sql);     
        } finally {     
            if(rs != null) rs.close();     
            if(stmt != null) stmt.close();     
            if(conn != null) conn.close();     
        }     
    }   
    
    
    private void createSuggestIndex(ResultSet rs){
    	IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_43, new StandardAnalyzer(Version.LUCENE_43));
    	//索引的打开方式，没有索引文件就新建，有就打开  	    	
    	iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND); 
    	Directory directory = null;
    	IndexWriter indexWriter = null;
    	try {
    		
			directory = FSDirectory.open(new File(INDEX_DIR));
			
			//如果索引处于锁定状态，则解锁  
            if (IndexWriter.isLocked(directory)){  
                IndexWriter.unlock(directory);  
            }
			indexWriter = new IndexWriter(directory, iwConfig);
			
			Document document = null;
			while(rs.next()) {
				document = new Document();
				TextField title = new TextField("title", rs.getString("title"), Field.Store.YES);
				TextField content = new TextField("content", rs.getString("content"), Field.Store.YES);
				TextField keyword = new TextField("keyword", rs.getString("keyword"), Field.Store.YES);
				document.add(title);
				document.add(content);
				document.add(keyword);
				indexWriter.addDocument(document);
			}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	//将indexWrite操作提交，如果不提交，之前的操作将不会保存到硬盘  
        try {  
            //这一步很消耗系统资源，所以commit操作需要有一定的策略  
            indexWriter.commit();  
            //关闭资源  
            indexWriter.close();  
            directory.close();  
            System.out.println("索引创建完成");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
	

	public static void main(String[] args) {
		
	    Directory dir;
		try {
			dir = FSDirectory.open(new File(INDEX_DIR));
			AutoCompleter suggestor = new AutoCompleter();
			suggestor.readDatabase();
		    boolean success = suggestor.buildSuggestor(dir, AutoCompleter.FIELD);
		    if(success) {
		    	String[] results = suggestor.suggest("医");
		    	for (String string : results) {
					System.out.println(string);
		    	}
		    }
		    else {
		    	System.out.println("Failed to build suggestor");
		    }
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}