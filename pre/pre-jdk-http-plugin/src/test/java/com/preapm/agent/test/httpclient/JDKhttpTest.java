package com.preapm.agent.test.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.preapm.sdk.zipkin.util.TraceKeys;



/**
 * sun.net.www.protocol.https.DelegateHttpsURLConnection:https://www.github.com/
 * sun.net.www.protocol.http.HttpURLConnection:http://www.github.com/
 * 
 * @author Zengmin.Zhang
 *
 */
public class JDKhttpTest {

	public static void main(String[] args) throws Exception {
		String url = "http://www.github.com/";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(1000);
		connection.setReadTimeout(1000);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty(TraceKeys.PRE_AGENT_NOT_TRACE_TAG, TraceKeys.PRE_AGENT_NOT_TRACE_TAG);
		connection.setDoOutput(true);
		
		System.out.println(connection.getClass().getName());

		long start = System.currentTimeMillis();
		try (InputStream in = connection.getInputStream()) {
			  while (in.read() != -1) { ; 
			  }
			System.out.println((System.currentTimeMillis() - start));
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
	}
}
