package com.splitwise.splitwisesdk.responses;

import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.splitwise.splitwisesdk.APIException;

public class User extends Response {
	public long id;
	public String first_name;
	public String last_name;
	public Map<String,String> picture;
	public String email;
	public String registration_status;
	public String force_refresh_at;
	public String locale;
	public String country_code;
	public String date_format;
	public String default_currency;
	public long default_group_id;
	public String notifications_read;
	public int notification_count;
	public NotificationSetting notification;
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public User(String jsonText) throws APIException {
		super(jsonText);
		fetchValues();
	}
	
	public void fetchValues() {
		JSONObject userJObj = (JSONObject) jsonObj.get("user");
		this.first_name = (String) userJObj.get("first_name");
		this.last_name = (String) userJObj.get("last_name");
		this.email = (String) userJObj.get("email");
		this.id = (long) userJObj.get("id");
		this.notifications_read = (String) userJObj.get("notifications_read");
	}
	
	public String toString() {
		return jsonText;
	}
	
	private static class NotificationSetting {
		public boolean added_as_friend;
		public boolean added_to_group;
		public boolean expense_added;
		public boolean expense_updated;
		public boolean bills;
		public boolean payments;
		public boolean monthly_summary;
		public boolean announcements;
	}
}
