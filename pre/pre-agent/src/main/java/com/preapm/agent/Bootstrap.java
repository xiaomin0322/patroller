package com.preapm.agent;

import java.io.IOException;
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
		//test4();
		new Bootstrap().print2("12");
	}
	
	
	public static void  test4() throws Exception {
		String url = "https://www.github.com/";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(100);
		connection.setReadTimeout(100);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		try (InputStream in = connection.getInputStream()) {
			while (in.read() != -1) {
				; // skip
			}
		} catch (IOException e) {
			try (InputStream err = connection.getErrorStream()) {
				if (err != null) { // possible, if the connection was dropped
					while (err.read() != -1) {
						; // skip
					}
				}
			}
			throw e;
		}
		System.out.println("==========================================================");
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
