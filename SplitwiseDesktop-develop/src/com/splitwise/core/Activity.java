package com.splitwise.core;

import java.util.Date;

import com.splitwise.splitwisesdk.responses.ActivityResponse;

public class Activity {
	private long id;
	private long type;
	private String content;
	private long created_by_id;
	private long source_id;
	private String source_type;
	private Date created_date;

    public Activity(ActivityResponse activity) {
    	this.id = activity.id;
    	this.type = activity.type;
    	this.content = activity.content;
    	this.created_by_id = activity.created_by_id;
    	this.source_id = activity.source_id;
    	this.source_type = activity.source_type;
    	this.created_date = activity.created_date;
    }
    
    public long getId() {
    	return this.id;
    }
    
    public String getContent() {
    	return this.content;
    }
    
    public Date getCreatedDate() {
    	return this.created_date;
    }
    
}
