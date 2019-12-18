package com.preapm.agent.plugin.interceptor;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;

/**
 * org.apache.catalina.connector.Request.Request()
 * org.apache.catalina.connector.Request.getRequest()
 * 
 * org.apache.catalina.core.ApplicationFilterChain.doFilter(ServletRequest,
 * ServletResponse)
 * 
 * @author Zengmin.Zhang
 *
 */
public class TomcatInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(TomcatInterceptor.class);

	@Override
	public void before(MethodInfo methodInfo) {

		try {
			HttpServletRequest request = (HttpServletRequest) methodInfo.getArgs()[0];
			//System.out.println("request================="+request);
			String url = request.getRequestURL().toString();
			String trace_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
			String span_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID);
			logger.info("获取trace_id：" + trace_id);
			logger.info("获取trace_id：" + span_id);
			if (trace_id != null && span_id != null) {
				BigInteger trace_id_bi = new BigInteger(trace_id, 16);
				BigInteger span_id_bi = new BigInteger(span_id, 16);
				ZipkinClientContext.getClient().startSpan(trace_id_bi.longValue(), span_id_bi.longValue(), url);
			}else {
				ZipkinClientContext.getClient().startSpan(ZipkinClientContext.serverName);
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
		ZipkinClientContext.getClient().finishSpan();

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
