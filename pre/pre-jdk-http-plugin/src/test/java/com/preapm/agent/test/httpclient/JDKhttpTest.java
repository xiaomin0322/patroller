package com.preapm.agent.test.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JDKhttpTest {

	public static void main(String[] args) throws Exception {
		String url = "https://www.github.com/";
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(1000);
		connection.setReadTimeout(1000);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		long start = System.currentTimeMillis();
		try (InputStream in = connection.getInputStream()) {
			/*
			 * while (in.read() != -1) { ; // skip }
			 */

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
