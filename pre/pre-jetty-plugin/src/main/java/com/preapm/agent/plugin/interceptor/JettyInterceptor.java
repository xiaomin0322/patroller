package com.preapm.agent.plugin.interceptor;

import java.math.BigInteger;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

/**
 * org.eclipse.jetty.server.Request
 * org.eclipse.jetty.server.Response
 *
 *
 * org.eclipse.jetty.servlet.ServletHandler
 *
 * 
 * @author Zengmin.Zhang
 *
 */
public class JettyInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(JettyInterceptor.class);
	
	private static final String JETTY_STR = "JETTY";

	@Override
	public void before(MethodInfo methodInfo) {
		int ipv4 = InetAddressUtils.localIpv4();
		Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
		methodInfo.setLocalVariable(new Object[] {endpoint});
		try {
			HttpServletRequest request = (HttpServletRequest) methodInfo.getArgs()[2];
			//System.out.println("request================="+request);
			String url = request.getRequestURL().toString();
			String method = request.getMethod();
			String queryString = request.getQueryString() == null ? "" :  request.getQueryString() ;
			String traceId = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
			String spanId = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID);
			String clientIP = com.preapm.sdk.zipkin.util.ClientUtil.getSpbillIp(request);
			
			logger.info("jetty before 获取traceId：{}, spanId: {}", traceId, spanId);

			if (traceId != null && spanId != null) {
				BigInteger traceIdBi = new BigInteger(traceId, 16);
				BigInteger spanIdBi = new BigInteger(spanId, 16);
				ZipkinClientContext.getClient().startSpan(traceIdBi.longValue(), spanIdBi.longValue(), JETTY_STR);
				ZipkinClientContext.getClient().sendBinaryAnnotation("traceRoot","false");
			}else {
				ZipkinClientContext.getClient().startRootSpan(JETTY_STR);
				ZipkinClientContext.getClient().sendBinaryAnnotation("traceRoot","true");
			}
			
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_CLIENT_IP,clientIP, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_QUERY_STRING,queryString, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_METHOD,method, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_URL,url, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.PRE_NAME,Arrays.toString(methodInfo.getPlugins()), endpoint);
			ZipkinClientContext.getClient().sendAnnotation(TraceKeys.SERVER_SEND, endpoint);
			
		} catch (Exception e) {
			logger.error("pre agent jetty interceptor exception", e);
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {}

	@Override
	public void after(MethodInfo methodInfo) {
		logger.info("jetty after ");
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		ZipkinClientContext.getClient().sendAnnotation(TraceKeys.SERVER_RECV, endpoint );
		ZipkinClientContext.getClient().finishSpan();

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
