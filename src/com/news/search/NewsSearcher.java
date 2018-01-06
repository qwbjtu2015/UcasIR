package com.news.search;

import com.news.index.*;

import java.io.File;     
import java.util.ArrayList;     
import java.util.HashSet;
import java.util.List;     
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;  
import org.apache.lucene.util.Version;  
import org.wltea.analyzer.lucene.IKAnalyzer;  



/**     
* NewsSearcher.java   
* @version 1.0   
* @createTime Lucene 数据库检索   
*/


public class NewsSearcher {     
	public static final int NORMAL = 0;
	public static final int WILDCARD = 1;
	public static final int TIMESORT = 2;
	public static final int HOTSORT = 3;
         
    private static File indexFile = null;     
    private static IndexSearcher searcher = null; 
    private static IndexReader reader = null;
    public static String prefixHTML = "<font color='red'>";
	public static String suffixHTML = "</font>";
    /** 查询结果条数 */    
    private static final int maxBufferedDocs = 100;
    // 相关词返回数量    
    private static final int RELATE_WORD_NUM = 8;    
    
    
    /**
     * 返回搜索结果列表
     * @param queryStr
     * @return
     * @throws Exception
     */
    public Tuple getResult(String queryStr, int MODE) throws Exception{     
        Tuple resultTuple = null;  
        
        try {
        	long t0 = System.currentTimeMillis();
        	resultTuple = search(queryStr,MODE);
        	resultTuple.setCostSeconds((System.currentTimeMillis()-t0));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return resultTuple;
    }    
    
    
    /**
     * 搜索索引
     * @param queryStr
     * @return
     * @throws Exception
     */
    public Tuple search(String queryStr, int MODE) throws Exception {
    	
    	// 创建IndexReader    	
    	indexFile = new File(NewsIndexer.searchDir);
    	reader = DirectoryReader.open(FSDirectory.open(indexFile));
    	// 创建IndexSearcher    	
    	searcher = new IndexSearcher(reader);
    	searcher.setSimilarity(new IKSimilarity());
    	
    	// 返回相关搜索词    	
    	QueryParser queryParser = new QueryParser(Version.LUCENE_43, "keyword", new IKAnalyzer());  
        Query query1 = queryParser.parse(queryStr);  
        TopDocs td = searcher.search(query1, RELATE_WORD_NUM);  
        ScoreDoc[] sd = td.scoreDocs;  
        Set<String> relateWordsSet = new HashSet<String>();
        for (int i = 0; i < sd.length; i++) {  
            int z = sd[i].doc;  
            Document doc = searcher.doc(z);  
            String[] words = doc.get("keyword").split(",");
            if(words.length > 1 && relateWordsSet.size() <= RELATE_WORD_NUM)
            	for(int j = 1; j < words.length; j++)
            		if(words[j] != queryStr)
            			relateWordsSet.add(words[j]);
        }
        String[] relateWords = relateWordsSet.toArray(new String[relateWordsSet.size()]);
    	

    	// 排序方式   
    	Query query = null;
    	TopFieldDocs topDocs = null;
    	Sort sort = null;
    	switch (MODE) {
    		//	按照相关度排序
			case 0:
				sort = new Sort();
				break;
			//	按照时间排序
			case 1: 
				sort = new Sort(new SortField("release_time", SortField.Type.STRING, true));	
				break;
			//	按照热度排序
			case 2:
				sort = new Sort(new SortField("join_num", SortField.Type.INT, true));
				break;
		}
//    	判断是否含有通配符
    	if(queryStr.contains("?") || queryStr.contains("*")){
    		Term term = new Term("title", queryStr);
    		WildcardQuery wildcardQuery = new WildcardQuery(term);
    		topDocs = searcher.search(wildcardQuery, null, 10, sort,false,false);
    	}
    	else {
    		// 构建查询语句    
        	String[] fields = {"title", "content", "keyword"};
        	MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_43, fields, new IKAnalyzer()); 
        	query = multiFieldQueryParser.parse(queryStr);
	    	topDocs = searcher.search(query, null, maxBufferedDocs, sort, false, false);
		}
    	ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    	
    	// 执行查询    	

    	
//    	TopDocs topDocs = searcher.search(query, maxBufferedDocs);
//    	ScoreDoc[] scoreDocs = topDocs.scoreDocs;
    	
    	// 将搜索结果转化为SearchBean列表
    	List<SearchBean> listBean = new ArrayList<SearchBean>();
    	SearchBean bean = null;
    	for(int i = 0; i < scoreDocs.length; i++) {
    		int docId = scoreDocs[i].doc;
    		Document document = searcher.doc(docId);
    		bean = new SearchBean();
    		bean.setDocId(docId);
    		bean.setId(document.get("newsId"));
    		bean.setSource(document.get("source"));
    		bean.setTitle(document.get("title"));
    		bean.setKeyword(document.get("keyword"));
    		bean.setSnippet(snippetGen(document.get("content"), query));
    		bean.setReleaseTime(document.get("release_time"));
    		bean.setCategory(document.get("category"));
    		listBean.add(bean);
    	}
    	
    	Tuple resultTuple = new Tuple(listBean, relateWords,listBean.size());
    	return resultTuple;
    }   
    
    
    
    
    String snippetGen(String content, Query query) {
    	SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(prefixHTML, suffixHTML); 
    	Highlighter highlighter = new Highlighter(simpleHTMLFormatter, new QueryScorer(query));
    	String highLightText = "";
    	try {
    		highLightText = highlighter.getBestFragment(new IKAnalyzer(), "content", content);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	// 摘要若为空，取正文前100个字符    	
    	if(highLightText == "")
    		highLightText = content.substring(0, Math.min(100, content.length()));
    	
    	return highLightText;
    }
    
    
    
    public static void main(String[] args) {
    	NewsSearcher newsSearcher = new NewsSearcher();
    	try {
			Tuple resulTuple = newsSearcher.getResult("排球",0);
			List<SearchBean> result = resulTuple.getResult();
			String[] relateWords = resulTuple.getRelateWords();
			int i = 0;
			for(SearchBean bean : result) {
				if(i == 10)
					break;
				System.out.println("bean.docid: " + bean.getDocId() + " bean.title: " + bean.getTitle() + "bean.release_time: " + bean.getReleaseTime() + " bean.keyword: " + bean.getKeyword() + "bean.join" + bean.getJoinNum() + "  bean.score:" + bean.getContent());
				i++;
			}
			System.out.println("搜索相关词");
			for (String string : relateWords) {
				System.out.println(string);
			}
			System.out.println("searchBean.result.size: " + result.size());
			System.out.println("查询所花费时间为： " + resulTuple.getCostSeconds() + " 秒");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
    }
}
  
