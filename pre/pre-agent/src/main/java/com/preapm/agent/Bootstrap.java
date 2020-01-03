package com.preapm.agent;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Bootstrap {
	
	public Integer i = new Integer(5);
	
	public Object o;
	
	static Map<Object, Object> classPoolMap = new ConcurrentHashMap<>();
	
	public Bootstrap() {
		// System.out.println("Bootstrap start");
	// System.out.println("Bootstrapend");
		
	}

	public static void main(String[] args)throws Exception {
		test4();
		new Bootstrap().print2("12");
	}
	
	
	public static void  test4() throws Exception {
		String url = "https://www.github.com/";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);
		InputStream in = connection.getInputStream();
		System.out.println("=========================================================="+in);
	}

	public void print() {
		System.out.println("zzm");
	}

	public String print2(String s) {
		System.out.println("zzm " + s);
		return s;
	}
	public String print3(String s,String s2) {
		System.out.println("zzm " + s);
		print2(s2);
		throw new RuntimeException("test");
	}
	
}
