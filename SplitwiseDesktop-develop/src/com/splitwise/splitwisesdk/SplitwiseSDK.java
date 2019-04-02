package com.splitwise.splitwisesdk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;

import com.splitwise.splitwisesdk.auth.OAuth;
import com.splitwise.splitwisesdk.responses.*;
import com.splitwise.core.Group;
import com.splitwise.logger.SplitwiseLogger;

public class SplitwiseSDK {
	private static SplitwiseSDK instance = null;
	// Register your app at https://secure.splitwise.com/apps/new and get API
	// Key
	private String consumerKey = "cCFUP5oGYVNJJAF9PlOR2qqBDcnzzEnPx5hofrh4";
	private String consumerSecret = "uwGfeBgdVlx2vAz4HwT3sPQgZ3Ib4PcYeZXyFiE2";
	
	private boolean hasValidAccessToken = false;
	
	private OAuth oauth;
	
	// API URLS
	final private String SPLITWISE_BASE_URL = "https://secure.splitwise.com/";
	final private String SPLITWISE_VERSION  = "v3.0";

	//URLs to make the request
	final private String REQUEST_TOKEN_URL   = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_request_token";
	final private String ACCESS_TOKEN_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_access_token";
	final private String AUTHORIZE_URL       = SPLITWISE_BASE_URL+"authorize";
	
	// Harsh
	final private String GET_CURRENT_USER_URL= SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_current_user";
	final private String GET_USER_URL        = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_user";
	final private String GET_FRIENDS_URL     = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_friends";
	final private String GET_FRIEND_URL     = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_friend";
	final private String GET_GROUPS_URL      = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_groups";
	final private String GET_GROUP_URL       = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_group";
	
	// Abhimanyu
	final private String GET_CURRENCY_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_currencies";
	final private String GET_CATEGORY_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_categories";
	final private String GET_EXPENSES_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_expenses";
	final private String GET_EXPENSE_URL     = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/get_expense";
	
	final private String GET_ACTIVITY_URL 	= SPLITWISE_BASE_URL + "api/" + SPLITWISE_VERSION+"/get_notifications";
	
	final private String CREATE_EXPENSE_URL  = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/create_expense";
	final private String CREATE_GROUP_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/create_group";
	final private String CREATE_FRIEND_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/create_friend";
	
	final private String ADD_USER_TO_GROUP_URL    = SPLITWISE_BASE_URL+"api/"+SPLITWISE_VERSION+"/add_user_to_group";
	
	
	
	private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	private SplitwiseSDK() {
		oauth = new OAuth(consumerKey,consumerSecret);
		oauth.setRequestTokenURL(this.REQUEST_TOKEN_URL);
		oauth.setAuthorizationURL(this.AUTHORIZE_URL);
		oauth.setAccessTokenURL(this.ACCESS_TOKEN_URL);
		
		LOGGER.setLevel(Level.FINER);
		//Load access token
		if(loadAccessToken()) {
			if(checkAccessTokenValid()) {
				hasValidAccessToken = true;
			}
		}
	}
	
	private boolean checkAccessTokenValid() {
		try {
			User currentUser = getCurrentUser();
			LOGGER.finer("User Respone " + currentUser.toString());
		} catch (APIException e) {
			LOGGER.info("Invalid Access Token");
			return false;
		}
		return true;
	}
	private void saveAccessToken() {
		String oauth_token = oauth.getOauthToken();
		String oauth_token_secret = oauth.getOauthTokenSecret();
		
		String tmpFname = "session.db";
		try {
			File f = new File(tmpFname);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(oauth_token + ";" + oauth_token_secret);
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean loadAccessToken() {
		LOGGER.info("Loading stored access token");
		String oauth_token = null;
		String oauth_token_secret = null;
		
		String tmpFname = "session.db";
		try {
			File f = new File(tmpFname);
			if(!f.exists()) return false;
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = br.readLine();
			if(line.contains(";")) {
				if(line.split(";").length == 2) {
					oauth_token = line.split(";")[0];
					oauth_token_secret = line.split(";")[1];
				}
			} else {
				return false;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(oauth_token + ";" + oauth_token_secret);
		oauth.setOauthToken(oauth_token);
		oauth.setOauthTokenSecret(oauth_token_secret);
		return true;
	}
	
	public static SplitwiseSDK getInstance() {
		if(instance == null) {
			instance = new SplitwiseSDK();
		}
		return instance;
	}

	public String getAuthorizationURL() {
		// Clear previous oauth
		revokeOauth();
		return oauth.getAuthorizationURL();
	}
	
	public String getAccessToken() {
		String response = oauth.getAccessToken();
		// Save Access Token for future use
		saveAccessToken();
		return response;
	}
	
	public void setOauthToken(String token) {
		oauth.setOauthToken(token);
	}
	
	public void setOauthTokenSecret(String token) {
		oauth.setOauthTokenSecret(token);
	}
	
	public String getOauthToken() {
		return oauth.getOauthToken();
	}
	
	public String getOauthTokenSecret() {
		return oauth.getOauthTokenSecret();
	}
	
	public void revokeOauth() {
		oauth.setOauthToken("");
		oauth.setOauthTokenSecret("");
		saveAccessToken();
	}
	
	public void setOauthVerifier(String token) {
		oauth.setOauthVerifier(token);
	}
	
	public boolean hasValidAccessToken() {
		return hasValidAccessToken;
	}
	
	public User getCurrentUser() throws APIException {
		String response = "";
		response = oauth.request(GET_CURRENT_USER_URL);
		
		return new User(response);
	}
	
	public List<Friend> getFriends() throws APIException {
		String response = "";
		response = oauth.request(this.GET_FRIENDS_URL);
		//LOGGER.info(response);
		return Friend.parseFriendsList(response);
	}
	
	public List<GroupResponse> getGroups() throws APIException {
		String response = "";
		response = oauth.request(this.GET_GROUPS_URL);
		LOGGER.info(response);
		return GroupResponse.parseGroupList(response);
	}
	
	public List<ActivityResponse> getActivities() throws APIException {
		return getActivities(new HashMap<String,String>());
	}
	
	public List<ActivityResponse> getActivities(Map<String,String> params) throws APIException {
		String response = "";
		//req.put("updated_after",dStr);
		response = oauth.request(GET_ACTIVITY_URL,params);
		LOGGER.info("Activity " + response);
		return ActivityResponse.parseActivityList(response);
	}
	
	public List<ExpenseResponse> getExpenses() throws APIException {
		String response = "";
		HashMap<String,String> req = new HashMap<String,String>();
		req.put("limit","50");
		response = oauth.request(GET_EXPENSES_URL,req);
		LOGGER.info(response);
		return ExpenseResponse.parseExpensesList(response);
	}
	
	public String getGroup() throws APIException {
		String response = "";
		response = oauth.request(this.GET_GROUP_URL);
		LOGGER.info(response);
		return response;
	}
	
	public String getFriend() throws APIException {
		String response = "";
		response = oauth.request(this.GET_FRIEND_URL);
		LOGGER.info(response);
		return response;
	}
	
	public ExpenseResponse createExpense(Map<String,String> parameters) throws APIException {
		/*
		req.setParameter("cost","10");
		req.setParameter("description","Testing");
		req.setParameter("users__0__owed_share","2.00");
		req.setParameter("users__0__paid_share","10.00");
		req.setParameter("users__0__user_id","9801405");
		req.setParameter("users__1__owed_share","8.00");
		req.setParameter("users__1__paid_share","0.00");
		req.setParameter("users__1__user_id","10092046");
		 */
		String response = "";
		response = oauth.requestPost(this.CREATE_EXPENSE_URL, parameters);
		LOGGER.info(response);
		return ExpenseResponse.parseExpensesList(response).get(0);
	}
	
	public Friend createFriend(Map<String,String> parameters) throws APIException {
		/*
		req.setParameter("user_email","vraj@scu.edu");
		 */
		String response = "";
		response = oauth.requestPost(this.CREATE_FRIEND_URL, parameters);
		LOGGER.info(response);
		return new Friend(response);
	}
	
	public GroupResponse createGroup(Map<String, String> parameters) throws APIException {
		// req.put("name","Test Group");
		
		String response = "";
		response = oauth.requestPost(this.CREATE_GROUP_URL, parameters);
		LOGGER.info(response);
		return new GroupResponse(response);
	}
	
	public Response createGroupMember(Map<String, String> parameters) throws APIException{
		// req.put("user_id","Test Group");
		// req.put("group_id","Test Group");
		String response = "";
		response = oauth.requestPost(this.ADD_USER_TO_GROUP_URL, parameters);
		LOGGER.info(response);
		return new Response(response);
	}
	
	public static void main(String args[]) {
		SplitwiseSDK sdk = SplitwiseSDK.getInstance();
		// Step 1: Execute and set oauth_token, oauth_token_secret from output.Get authorization url
		// System.out.println("Set oauth_token and oauth_token_secret in program");
		// System.out.println("Go to url: " + sdk.getAuthorizationURL());
		// Comment above lines
		
		// Step 2: Uncomment below, On the browser, after clicking on authorize copy oauth_verifier and paste it below
		/** /
		String oauth_token = "t8l1ophvezhxV2KAOC3aRFb36rOIDl2OGhe1h16N";
		String oauth_token_secret = "YeHkvmzc8t57Ftq4n2s8gd8fZMahBEUQIIzgcsMW";
		String oauth_verifier = "SQmyHFE4ScI2a0WvrQpA";
		
		sdk.setOauthToken(oauth_token);
		sdk.setOauthTokenSecret(oauth_token_secret);
		sdk.setOauthVerifier(oauth_verifier);
		System.out.println(sdk.getAccessToken());
		/**/
		// Copy oauth_token and oauth_token_secret from output and paste it below. Comment above lines
		
		//Step 3: Set access token and access token secret
		/**/
		//String oauth_access_token = "DuRUarYgjBpOpjDDIyV7Oj1NQVpiDp0WL9Yc6CAg";
		String oauth_access_token = "CmXVaOyOsG5h9IToAUoVN2whq7uTfpquKb9ANwEe";
		//String oauth_access_token_secret = "AcagAm8Xcizwbp5wWoCkFL5Ns0SaxNWDmi1yh7O3";
		String oauth_access_token_secret = "OtbQ6gUYGCafnMzSKVft8gszYxq6VmTYNO4qovsJ";
		
		
		//Z5mWLHzTlpc6MQ4ZmfXUukZuyZilMsRwQI1P5thh
		oauth_access_token = "Z5mWLHzTlpc6MQ4ZmfXUukZuyZilMsRwQI1P5thh";
		//PmoDuL2hCLcBxLZWRaer42VvlNRXJZysxL6zeuuY
		oauth_access_token_secret = "PmoDuL2hCLcBxLZWRaer42VvlNRXJZysxL6zeuuY";
		sdk.setOauthToken(oauth_access_token);
		sdk.setOauthTokenSecret(oauth_access_token_secret);
		
		try {
			//System.out.println(sdk.getCurrentUser());
			Map<String,String> req = new HashMap<String,String>();
			/*req.put("cost","10");
			req.put("description","Testing Bill");
			req.put("users__0__owed_share","2.00");
			req.put("users__0__paid_share","10.00");
			req.put("users__0__user_id","21598322");
			req.put("users__1__owed_share","8.00");
			req.put("users__1__paid_share","0.00");
			req.put("users__1__user_id","21694899");
			req.put("group_id","11803610");*/
			java.util.Date d = new java.util.Date(System.currentTimeMillis());
			//d.setDate(1);
			DateFormat m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			String dStr = m_ISO8601Local.format(d);
			System.out.println(dStr);
			req.put("updated_after",dStr);
			//System.out.println(sdk.createExpense(req));
			
			//fgXlRJ1b1kBecevvkCQeA6fDHfdaYzVnDh5t40a;WR9z7ShWOk4n4vgrpLxmionp21W6Iud2iHQ37z2f
			/*Map<String,String> req = new HashMap<String,String>();
			req.put("user_email","vraj@scu.edu");
			System.out.println(sdk.createFriend(req));*/
			
			
			/*Map<String,String> req = new HashMap<String,String>();
			req.put("name","Test Group");
			//System.out.println(sdk.oauth.requestPost(sdk.CREATE_GROUP_URL, req));*/
			//System.out.println(sdk.getExpenses() != null);
			System.out.println(sdk.getActivities(req));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new String(Base64.encodeBase64(" ".getBytes())));
//			
		
		/**/
	}

	

	

}
