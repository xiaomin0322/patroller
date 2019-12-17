package com.preapm.agent.plugin.interceptor;

import javax.servlet.http.HttpServletRequest;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ThreadLocalTraceStore;

/**
 * org.apache.catalina.connector.Request.Request()
 * org.apache.catalina.connector.Request.getRequest()
 * 
 * @author Zengmin.Zhang
 *
 */
public class TomcatInterceptor implements AroundInterceptor {

	@Override
	public void before(MethodInfo methodInfo) {
		try {
			HttpServletRequest request = (HttpServletRequest) methodInfo.getResult();
			String trace_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
			if (trace_id != null) {
				ThreadLocalTraceStore.set(Long.valueOf(trace_id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {

	}

}
