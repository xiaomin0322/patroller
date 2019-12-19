package com.preapm.agent.plugin.interceptor.filter;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;


/**
 * org.apache.http.impl.client.HttpClientBuilder.HttpClientBuilder()
 * @author Zengmin.Zhang
 *
 */
public class HttpRequestFilter implements HttpRequestInterceptor{

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		// TODO Auto-generated method stub
		
	}

}
