package com.preapm.agent.plugin.interceptor;

import org.apache.http.client.methods.HttpUriRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;

import zipkin.Span;

/**
 * org.apache.http.client.HttpClient.execute(HttpUriRequest)
 * 
 * 
 * 
 * @author Zengmin.Zhang
 *
 */
public class Httpclient4Interceptor implements AroundInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(Httpclient4Interceptor.class);

	@Override
	public void before(MethodInfo methodInfo) {
	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
