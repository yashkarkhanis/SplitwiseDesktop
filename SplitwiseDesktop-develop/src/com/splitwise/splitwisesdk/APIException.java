package com.splitwise.splitwisesdk;

public class APIException extends Exception{
	private String responseString = "";
	
	public APIException(String message, String responseString) {
		super(message);
		this.responseString = responseString;
	}
	
	public String getResponseString() {
		return this.responseString;
	}
}
