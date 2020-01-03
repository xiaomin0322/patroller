package com.preapm.agent.plugin.interceptor;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;
import zipkin.Span;

/**
 * sun.net.www.protocol.http.HttpURLConnection.getInputStream()
 * 
 * 
 * 
 * @author Zengmin.Zhang
 *
 */
public class JdkResponseHttpInterceptor implements AroundInterceptor {
	@Override
	public void before(MethodInfo methodInfo) {
	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		System.out.println("com.preapm.agent.plugin.interceptor.JdkResponseHttpInterceptor  after");
		Integer rs = (Integer) methodInfo.getResult();
		if (methodInfo == null || rs == null || rs == -1) {
			return;
		}
		ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_RECV);
		ZipkinClientContext.getClient().finishSpan();
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
