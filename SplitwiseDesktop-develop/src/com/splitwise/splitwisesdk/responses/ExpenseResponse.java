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

public class ExpenseResponse extends Response {
	public long id;
	public long group_id;
	public String description;
	public Date updated_at;
	public long created_by_id;
	public float cost;
	public List<ExpenseUser> expenseUsers;
	public Date created_date;
	public boolean isDeleted;
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public ExpenseResponse(String jsonText) throws APIException {
		super(jsonText);
		fetchValues();
	}
	
	public ExpenseResponse(JSONObject expenseObj) {
		this.jsonObj = expenseObj;
		fetchValues();
	}

	public void fetchValues() {
		this.id = (long) jsonObj.get("id");
		if(jsonObj.get("group_id") != null)
			this.group_id = (long) jsonObj.get("group_id");
		this.description = (String) jsonObj.get("description");
		JSONObject temp = (JSONObject)jsonObj.get("created_by");
		this.created_by_id = (long)temp.get("id");
		this.cost = Float.parseFloat((String)jsonObj.get("cost"));
		this.isDeleted = (jsonObj.get("deleted_by") != null);
		
		expenseUsers = new ArrayList<ExpenseUser>();
		JSONArray users = (JSONArray) jsonObj.get("users");
		for(int i = 0; i< users.size(); i++) {
			expenseUsers.add(new ExpenseUser((JSONObject) users.get(i)));
		}
		
		
		DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		
		try {
			this.updated_at = m_ISO8601Local.parse((String) jsonObj.get("updated_at"));
			this.created_date = m_ISO8601Local.parse((String) jsonObj.get("date"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			this.updated_at = new Date(System.currentTimeMillis());
			this.created_date = new Date(System.currentTimeMillis());
			e.printStackTrace();
		}
	}
	
	public String getJson() {
		return super.toString();
	}
	public String toString() {
		return String.valueOf(id);
	}

	public static List<ExpenseResponse> parseExpensesList(String response) throws APIException {
		Response res = new Response(response);
		JSONArray expenses = (JSONArray) res.jsonObj.get("expenses");
		ArrayList<ExpenseResponse> expenseList = new ArrayList<ExpenseResponse>();
		for(int i = 0; i< expenses.size(); i++) {
			JSONObject expenseObj = (JSONObject) expenses.get(i);
			//LOGGER.info(expenseObj.toJSONString());
			expenseList.add(new ExpenseResponse(expenseObj));
		}
		return expenseList;
	}
	
	public static class ExpenseUser {
		public long id;
		public float paid_share;
		public float owed_share;
		public float net_balance;
		
		ExpenseUser(JSONObject obj) {
			this.id = (long) obj.get("user_id");
			this.paid_share = Float.parseFloat((String)obj.get("paid_share"));
			this.owed_share = Float.parseFloat((String)obj.get("owed_share"));
			this.net_balance = Float.parseFloat((String)obj.get("net_balance"));
		}
		
		
	}
	
}
