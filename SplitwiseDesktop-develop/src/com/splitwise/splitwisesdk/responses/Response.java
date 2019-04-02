package com.splitwise.splitwisesdk.responses;

import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.splitwise.splitwisesdk.APIException;

public class Response {
	protected String jsonText;
	protected JSONObject jsonObj;
	
	public Response() {}
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public Response(String jsonText) throws APIException {
		this.jsonText = jsonText;
		parseJSON();
		
		if(jsonObj.containsKey("error")) {
			LOGGER.warning("Error " + jsonObj.get("error").toString());
			throw new APIException(jsonObj.get("error").toString(), jsonText);
		}
	}
	
	private void parseJSON() {
		JSONParser genParser = new JSONParser();
		try {
			jsonObj = (JSONObject) genParser.parse(jsonText.toString());
		} catch (Exception e) {
			LOGGER.severe("Unable to parse json" + e.getMessage());
		}
	}
	
	public String toString() {
		return jsonText;
	}
}
