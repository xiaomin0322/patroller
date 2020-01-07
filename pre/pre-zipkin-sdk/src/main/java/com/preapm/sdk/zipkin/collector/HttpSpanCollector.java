package com.preapm.sdk.zipkin.collector;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.preapm.sdk.zipkin.util.TraceKeys;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.sdk.zipkin.DefaultSpanMetrics;
import com.preapm.sdk.zipkin.SpanMetrics;

import lombok.Getter;

/**
 * 
 * <pre>
 * HttpSpanCollector
 * </pre>
 * 
 * @author
 *
 * @since 2018年1月25日 下午4:18:17
 */
public class HttpSpanCollector extends AbstractSpanCollector {
	private static final Logger logger = LoggerFactory.getLogger(HttpSpanCollector.class);
	@Getter
	private String zipkinHost;
	private String url;

	public HttpSpanCollector(String zipkinHost) {
		this(zipkinHost, new DefaultSpanMetrics());
	}

	public HttpSpanCollector(String zipkinHost, SpanMetrics metrics) {
		super(metrics);
		this.url = zipkinHost + (zipkinHost.endsWith("/") ? "" : "/") + "api/v1/spans";
	}


	@Override
	public void sendSpans(byte[] json) throws IOException {
		sendSpanByOkHttp(json);
		//System.out.println("=====================sendSpans=========================");
		//sendSpansByJdk(json);
	}


	private void sendSpanByOkHttp(byte[] json) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder()
				.connectTimeout(10 * 1000, TimeUnit.MILLISECONDS)
				.readTimeout(60 * 1000, TimeUnit.MILLISECONDS)
				.writeTimeout(60 * 1000, TimeUnit.MILLISECONDS)
				.build();
		//request headers
		Map<String,String> headerMap = new HashMap<>(4);
		headerMap.put("Content-Type", "application/json");
		headerMap.put("Content-Length", String.valueOf(json.length));
		headerMap.put(TraceKeys.PRE_AGENT_NOT_TRACE_TAG, TraceKeys.PRE_AGENT_NOT_TRACE_TAG);
		Headers headers = Headers.of(headerMap);
		MediaType JSON = MediaType.parse("application/json; charset=utf-8");
		//request body
		RequestBody body = RequestBody.create(JSON, json);

		Request request = new Request.Builder()
				.url(url)
				.headers(headers)
				.post(body)
				.build();

		try (Response response = client.newCall(request).execute()) {
			logger.info("send span data response : {}", response);
		}catch (Exception e){
			logger.error(e.getMessage());
			throw e;
		}
	}

	private void sendSpansByJdk(byte[] json) throws IOException {
		// intentionally not closing the connection, so as to use keep-alives
		HttpURLConnection connection = (HttpURLConnection) new URL(this.url).openConnection();
		connection.setConnectTimeout(10 * 1000);
		connection.setReadTimeout(60 * 1000);
		connection.setRequestMethod("POST");
		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty(TraceKeys.PRE_AGENT_NOT_TRACE_TAG, TraceKeys.PRE_AGENT_NOT_TRACE_TAG);
		connection.setDoOutput(true);
		connection.setFixedLengthStreamingMode(json.length);
		connection.getOutputStream().write(json);

		try (InputStream in = connection.getInputStream()) {
			while (in.read() != -1) {
				; // skip
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
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
