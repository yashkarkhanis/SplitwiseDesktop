package com.splitwise.splitwisesdk.responses;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.splitwise.splitwisesdk.APIException;

public class ActivityResponse extends Response {
	public long id;
	public long type;
	public String content;
	public long created_by_id;
	public long source_id;
	public String source_type;
	public Date created_date;
	public String created_date_str;
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public ActivityResponse(String jsonText) throws APIException {
		super(jsonText);
		fetchValues();
	}
	
	public ActivityResponse(JSONObject activityObj) {
		this.jsonObj = activityObj;
		fetchValues();
	}

	public void fetchValues() {
		this.id = (long) jsonObj.get("id");
		this.content = (String) jsonObj.get("content");
		this.type = (long) jsonObj.get("type");
		
		if(jsonObj.get("created_by") != null)
			this.created_by_id = (long)jsonObj.get("created_by");
		
		JSONObject source = (JSONObject)jsonObj.get("source");
		this.source_id = (long)source.get("id");
		this.source_type = (String)source.get("type");
		
		DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		this.created_date_str = (String) jsonObj.get("created_at");
		
		try {
			this.created_date = m_ISO8601Local.parse((String) jsonObj.get("created_at"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			this.created_date = new Date(System.currentTimeMillis());
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return String.valueOf(id);
	}

	public static List<ActivityResponse> parseActivityList(String response) throws APIException {
		Response res = new Response(response);
		JSONArray activities = (JSONArray) res.jsonObj.get("notifications");
		ArrayList<ActivityResponse> activityList = new ArrayList<ActivityResponse>();
		for(int i = 0; i< activities.size(); i++) {
			JSONObject activityObj = (JSONObject) activities.get(i);
			//LOGGER.info(activityObj.toJSONString());
			activityList.add(new ActivityResponse(activityObj));
		}
		return activityList;
	}
}
