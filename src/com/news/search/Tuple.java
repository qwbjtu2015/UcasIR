package com.news.search;
import java.util.List;

import com.news.search.SearchBean;


public class Tuple {
	private  List<SearchBean> result;  
    private  String[] relateWords;
    private int size;
    private double costSeconds;
    
	public Tuple(List<SearchBean> result, String[] relateWords, int size, double costSeconds) {  
        this.result = result;  
        this.relateWords = relateWords;  
        this.size = size;
        this.costSeconds = costSeconds;
    }
	
	public Tuple(List<SearchBean> result, String[] relateWords, int size) {  
        this.result = result;  
        this.relateWords = relateWords;  
        this.size = size;
    }
    
    public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public double getCostSeconds() {
		return costSeconds;
	}
	public void setCostSeconds(double costSeconds) {
		this.costSeconds = costSeconds;
	}

	public List<SearchBean> getResult() {
		return result;
	}
	public void setResult(List<SearchBean> result) {
		this.result = result;
	}
	public String[] getRelateWords() {
		return relateWords;
	}
	public void setRelateWords(String[] relateWords) {
		this.relateWords = relateWords;
	}
    
    
}
