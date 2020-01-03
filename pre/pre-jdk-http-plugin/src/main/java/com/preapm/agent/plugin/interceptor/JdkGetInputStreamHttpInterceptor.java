package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.TraceKeys;

/**
 * sun.net.www.protocol.http.HttpURLConnection.getInputStream()
 * 
 * 
 * 
 * @author Zengmin.Zhang
 *
 */
public class JdkGetInputStreamHttpInterceptor implements AroundInterceptor {
	@Override
	public void before(MethodInfo methodInfo) {
	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		if (methodInfo == null) {
			return;
		}
		System.out.println("com.preapm.agent.plugin.interceptor.JdkGetInputStreamHttpInterceptor  after");
		ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_RECV);
		ZipkinClientContext.getClient().finishSpan();
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
