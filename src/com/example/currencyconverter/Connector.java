package com.example.currencyconverter;

import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class Connector {
	String url;
	String response = "";
	public Connector(String url){
		this.url = url;
	}
	
	public boolean Connect(){
		HttpClient Client = new DefaultHttpClient();
		try {

			HttpGet httpget = new HttpGet(url);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = Client.execute(httpget, responseHandler);

		} catch (IOException ex) {
			return false;
		} 
		return true;
	}
	
	public String getResponse(){
		return response;
	}
}
