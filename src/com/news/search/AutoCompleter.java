package com.news.search;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.document.Document;
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
import org.apache.lucene.util.Version;

import com.news.index.NewsIndexer;

public class AutoCompleter {
  
	public static final String FIELD = "title";
	public static final String INDEX_DIR = "G:\\MySQLData\\newindex";
	public static final int RESULTS_TO_DISPLAY = 10;
	  
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
			final String [] stopWords =  {"a", "an", "and", "are", "as", "at", "be", "but", "by",
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
		List<LookupResult> results = suggestor.lookup(CharBuffer.wrap(q), false, AutoCompleter.RESULTS_TO_DISPLAY);
		String[] autosuggestResults = new String[results.size()];
		for(int i=0; i < results.size(); i++) {
			LookupResult result = results.get(i);
			autosuggestResults[i] = result.key.toString();
		}    
		return autosuggestResults;
	}

	public static void main(String[] args) {
		
//		String[] docs = {"foo","food","fail"};
//	    
//	    Directory dir = new RAMDirectory();
//	    IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_43, AutoCompleter.getAnalyzer());
//	    try {
//	    	IndexWriter iw = new IndexWriter(dir, iwConfig);
//		    
//		    for(int i=0; i<3; i++) {
//		    	Document doc = new Document();
//		    	doc.add(new TextField(AutoCompleter.FIELD, new StringReader(docs[i])));
//		    	iw.addDocument(doc);
//		    }
//		    iw.commit();
//		    iw.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	    Directory dir;
		try {
			dir = FSDirectory.open(new File(NewsIndexer.searchDir));
			AutoCompleter suggestor = new AutoCompleter();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}