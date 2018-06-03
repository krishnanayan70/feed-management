package com.stringsai.feedmanagement.data;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection="feeds")
public class Feed implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4741150158594744130L;
	private String message;
	private long postedOn;
	private String postedBy;
	
	public Feed() {
		this.postedOn = new Date().getTime();
	}
	
	public Feed(String feed) {
		this.message = feed;
		this.postedOn = new Date().getTime();
	}

	public String getMessage() {
		return message;
	}
	
	@Field
	public void setMessage(String message) {
		this.message = message;
	}

	public long getPostedOn() {
		return postedOn;
	}
	
	@Field
	public void setPostedOn(long postedOn) {
		this.postedOn = postedOn;
	}

	public String getPostedBy() {
		return postedBy;
	}
	
	@Field
	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

}
