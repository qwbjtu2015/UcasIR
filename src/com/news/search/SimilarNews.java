package com.news.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.news.index.NewsIndexer;


//	相似新闻查找
public class SimilarNews {
	private static File indexFile = null;     
    private static IndexSearcher searcher = null; 
    private static IndexReader reader = null;
    private static final int SIMILIAR_NUM = 8;
	
	
	
    public List<SearchBean> getSimilarNews(int targetDocId) throws Exception {
    	
    	// 创建IndexReader    	
    	indexFile = new File(NewsIndexer.searchDir);
    	reader = DirectoryReader.open(FSDirectory.open(indexFile));
    	// 创建IndexSearcher    	
    	searcher = new IndexSearcher(reader);
    	searcher.setSimilarity(new IKSimilarity());
    	
    	MoreLikeThis mlt = new MoreLikeThis(reader);
    	mlt.setFieldNames(new String[] {"title", "keyword", "category"});
    	mlt.setAnalyzer(new IKAnalyzer());
    	Query query = mlt.like(targetDocId);
    	TopFieldDocs topDocs = searcher.search(query, null, SIMILIAR_NUM, new Sort(), false, false);
    	
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
    		bean.setTitle(document.get("title"));
    		bean.setKeyword(document.get("keyword"));
    		bean.setSnippet(snippetGen(document.get("content"), query));
    		bean.setReleaseTime(document.get("release_time"));
    		bean.setJoinNum(Integer.parseInt(document.get("join_num")));
    		String conString = document.get("content");
    		conString = conString.substring(0,Math.min(300, conString.length()));
    		bean.setContent(conString);
    		listBean.add(bean);
    	}
    	return listBean;
    }   
    
    private String snippetGen(String content, Query query) {
    	SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter(NewsSearcher.prefixHTML, NewsSearcher.suffixHTML); 
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
	
//	public static void main(String[] args) {
//		int targetDocId = 9216;
//		SimilarNews news = new SimilarNews();
//		try {
//			List<SearchBean> result = news.getSimilarNews(targetDocId);
//			for (SearchBean bean : result) {
//				System.out.println("bean.title: " + bean.getTitle() + "bean.release_time: " + bean.getReleaseTime() + " bean.keyword: " + bean.getKeyword() + "bean.join" + bean.getJoinNum() + "  bean.score:" + bean.getContent());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
