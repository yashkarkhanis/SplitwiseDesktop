package com.splitwise.splitwisesdk.auth;

import java.net.URLEncoder;
import java.util.SortedMap;
import java.util.TreeMap;

public class OAuthRequest {
	final static String HMAC_SHA1 = "HMAC-SHA1";
	
	private String oauth_consumer_key;
	private String oauth_signature_method;
	private String oauth_signature = null;
	private String oauth_timestamp;
	private String oauth_nonce;
	private String oauth_token;
	private String oauth_token_secret;
	private String oauth_verifier;
	private String oauth_body_hash;
	private String endpoint;
	private String method;
	final private String oauth_version = "1.0";
	private SortedMap<String,String> params;
	
	OAuthRequest() {
		params = new TreeMap<String,String>((a,b)->a.compareTo(b));
		
		this.oauth_timestamp = String.valueOf(this.getCurrentTimestamp());
		this.oauth_nonce = String.valueOf(this.getNonce());
		
		params.put("oauth_timestamp",this.oauth_timestamp);
		params.put("oauth_nonce",this.oauth_nonce);
		//params.put("oauth_timestamp","1551691368");
		//params.put("oauth_nonce","42199551");
	}
	protected void setConsumerKey(String consumerKey) {
		this.oauth_consumer_key = consumerKey;
		params.put("oauth_consumer_key",consumerKey);
	}
	protected void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	protected String getEndpoint() {
		return this.endpoint;
	}
	protected void setOauthToken(String token) {
		this.oauth_token = token;
		params.put("oauth_token",token);
	}
	protected void setOauthTokenSecret(String token) {
		this.oauth_token_secret = token;
	}
	protected void setOauthVerifier(String token) {
		this.oauth_verifier = token;
		params.put("oauth_verifier",token);
	}
	protected void setOauthBodyHash(String bodyHash) {
		this.oauth_body_hash = bodyHash;
		params.put("oauth_body_hash",bodyHash);
	}
	
	protected void setOauthSignatureMethod(String signatureMethod) {
		this.oauth_signature_method = signatureMethod;
		params.put("oauth_signature_method",signatureMethod);
	}
	
	protected void setMethod(String method) {
		this.method = method;
	}
	
	protected String getMethod() {
		return this.method;
	}
	
	protected String getRequestBody() {
		StringBuilder sb = new StringBuilder();
		params.put("oauth_version",oauth_version);
		boolean isFirst = true;
		for(String key : params.keySet()) {
			if(isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			sb.append(key + "=" + params.get(key));
		}
		//sb.append("oauth_version" + "=" + oauth_version);
		return sb.toString();
	}
	
	protected void setSignature(String signature) {
		this.oauth_signature = URLEncoder.encode(signature);
		params.put("oauth_signature",this.oauth_signature);
	}
	
	protected String getRequestHash() {
		params.remove("oauth_signature");
		return URLEncoder.encode(getRequestBody());
	}
	
	protected long getCurrentTimestamp() {
		return System.currentTimeMillis()/1000;
	}
	
	protected long getNonce() {
		return (long)(Math.random() * 1000000) + 1000000;
	}
	
	public String toString() {
		return getRequestBody().replace("&", "\n");
	}
	public void setParameter(String string, String string2) {
		params.put(string,string2);
	}
}
