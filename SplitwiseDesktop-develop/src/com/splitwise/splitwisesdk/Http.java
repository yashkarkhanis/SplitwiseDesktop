package com.splitwise.splitwisesdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

public class Http {
	static HttpClient client = HttpClientBuilder.create().build();
	static ReentrantLock clientLock = new ReentrantLock(); // So that multiple thread don't send request at the same time when request is also in progress
	
	final private static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	public static String sendPostRequest(String url, Map<String,String> headers, String body) {
		String responseText = "";
		HttpPost request = new HttpPost(url);
		//HttpPost request = new HttpPost("http://localhost:8000/");
		try {
			
			for(Map.Entry<String, String> header : headers.entrySet()) {
				request.addHeader(header.getKey(),header.getValue());
			}
			
			request.setEntity(new StringEntity(body));
			//System.out.println(request.toString());
			
			HttpResponse response = executeQuery(request);
			BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
		
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			responseText = result.toString();
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			request.releaseConnection();
		}
		return responseText;
	}
	
	public static String sendGetRequest(String url, Map<String,String> params) {
		String responseText = "";
		
		try {
			URIBuilder builder = new URIBuilder(url);
			if(params != null)
				for(Map.Entry<String, String> param : params.entrySet()) {
					builder.setParameter(param.getKey(),param.getValue());
				}
		
			HttpGet request = new HttpGet(builder.build());
			//System.out.println(request.toString());
			try {
				HttpResponse response = executeQuery(request);
				BufferedReader rd = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
		
				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}
				responseText = result.toString();
			} catch(Exception e) {
					
			} finally {
				request.releaseConnection();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return responseText;
	}
	
	public static HttpResponse executeQuery(HttpUriRequest request) throws ClientProtocolException, IOException {
		if(clientLock.isLocked()) {
			LOGGER.info(Thread.currentThread().getName() + " Waiting for lock");
		}
		clientLock.lock();
		LOGGER.info(Thread.currentThread().getName() + " Acquired for lock");
		HttpResponse response = client.execute(request);
		LOGGER.info(Thread.currentThread().getName() + " Releasing for lock");
		clientLock.unlock();
		return response;
	}
	
}
