package com.preapm.agent.plugin.interceptor;

import java.math.BigInteger;

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
	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		try {
			HttpServletRequest request = (HttpServletRequest) methodInfo.getResult();
			if (request != null) {
				String trace_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
				System.out.println("获取trace_id：" + trace_id);
				if (trace_id != null) {
					BigInteger trace_id_bi = new BigInteger(trace_id, 16);
					// String span_id =
					// request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID);
					if (trace_id != null) {
						ThreadLocalTraceStore.set(trace_id_bi.longValue());
					}
				}

			} else {
				System.out.println(
						">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>HttpServletRequest is null >>>>>>>>>>>>>>>>>>>>>>>>");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
