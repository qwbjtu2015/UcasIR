package com.news.search;

public class CommentBean {  
	private String id;
	private String newsId;
	private String comment;
	private String createTime;
	private String userId;
	private String userName;
	private int pos_or_neg;
	public int getPos_or_neg() {
		return pos_or_neg;
	}
	public void setPos_or_neg(int pos_or_neg) {
		this.pos_or_neg = pos_or_neg;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String creatTime) {
		this.createTime = creatTime;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
} 
