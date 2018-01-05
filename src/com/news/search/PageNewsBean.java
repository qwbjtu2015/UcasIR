package com.news.search;

import java.util.List;


public class PageNewsBean{

    private int curPage = 1; //当前是第几页
    private int maxPage; //一共有多少页
    private int maxRowCount; //一共有多少行
    public int rowsPerPage = 10; //每页多少行

    public List<SearchBean> data;//检索结果
    public List<SearchBean> datashow;//要展示的结果

    public int getCurPage() {
        return curPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public int getMaxRowCount() {
        return maxRowCount;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public void setMaxRowCount(int maxRowCount) {
        this.maxRowCount = maxRowCount;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }
    
    public PageNewsBean() throws Exception{
    	this.setPage();
    }
    
    public PageNewsBean(String queryStr) throws Exception {
        NewsSearcher newsSearcher = new NewsSearcher();
        List<SearchBean> result = newsSearcher.search(queryStr,0).getResult();         
        this.data = result;
        this.setPage();
    }
    
    //得到要显示于本页的数据
    public PageNewsBean getResult(String pagenum) throws Exception {
    	int pageNum = Integer.parseInt(pagenum);
    	int baseNum = pageNum -1;
    	this.datashow.clear();
    	if(pageNum == this.maxPage){
            this.datashow = this.data.subList(baseNum * this.rowsPerPage, this.data.size());
        }else {
        	this.datashow = this.data.subList(baseNum * this.rowsPerPage, baseNum * this.rowsPerPage + this.rowsPerPage);
        }
        this.setCurPage(pageNum);
    	return this;
	}
    
    //获取总行数
    public int getAvailableCount() throws Exception {
        return data.size();
    }
    
    //初始化时对Page进行设置
    public void setPage() throws Exception {

        //得到总行数
        this.setMaxRowCount(data.size());

        if (this.maxRowCount % this.rowsPerPage == 0) { //根据总行数计算总页数
            this.maxPage = this.maxRowCount / this.rowsPerPage;
        } else {
            this.maxPage = this.maxRowCount / this.rowsPerPage + 1;
        }
    }
}