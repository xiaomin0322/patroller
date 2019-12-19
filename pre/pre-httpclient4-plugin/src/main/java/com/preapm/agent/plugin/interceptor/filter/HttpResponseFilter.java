package com.preapm.agent.plugin.interceptor.filter;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.protocol.HttpContext;


/**
 * org.apache.http.impl.client.HttpClientBuilder.HttpClientBuilder()
 * @author Zengmin.Zhang
 *
 */
public class HttpResponseFilter implements HttpResponseInterceptor{

	@Override
	public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
		System.out.println("end>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
	}


}
