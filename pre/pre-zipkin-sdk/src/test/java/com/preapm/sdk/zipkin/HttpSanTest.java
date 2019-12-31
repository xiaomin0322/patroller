package com.preapm.sdk.zipkin;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpSanTest {

	public static void main(String[] args) throws Exception {
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
	}
}
