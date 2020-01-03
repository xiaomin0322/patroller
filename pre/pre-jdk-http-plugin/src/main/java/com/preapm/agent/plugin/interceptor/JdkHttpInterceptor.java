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
public class JdkHttpInterceptor implements AroundInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(JdkHttpInterceptor.class);

	private static final String SPAN_NAME_STR = "jdkhttp";

	@Override
	public void before(MethodInfo methodInfo) {
         System.out.println("com.preapm.agent.plugin.interceptor.JdkHttpInterceptor start >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		try {
			HttpURLConnection connection = (HttpURLConnection) methodInfo.getTarget();
			String headerField = connection.getHeaderField(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
			if (connection != null && headerField == null) {
				URL url = connection.getURL();
				int ipv4 = InetAddressUtils.localIpv4();
				Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
				methodInfo.setLocalVariable(new Object[] { endpoint });
				ZipkinClientContext.getClient().startSpan(SPAN_NAME_STR);
				ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
				ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.PRE_NAME,
						Arrays.toString(methodInfo.getPlugins()), endpoint);
				ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_URL,
						url.toString(), endpoint);
				String method = connection.getRequestMethod();
				String host = url.getHost();
				ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_HOST,
						host, endpoint);
				ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_METHOD,
						method, endpoint);
				Span span = ZipkinClientContext.getClient().getSpan();
				if (span != null) {
					logger.debug("放入traceId到http请求头：" + Long.toHexString(span.traceId));
					logger.debug("放入SPAN_ID到http请求头：" + Long.toHexString(span.id));
					connection.addRequestProperty(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID,
							Long.toHexString(span.traceId));
					connection.addRequestProperty(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID,
							Long.toHexString(span.id));
				} else {
					logger.debug("ZipkinClientContext.getClient().getSpan() is null");
				}
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
		if (methodInfo.getLocalVariable() != null) {
			Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
			ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
			ZipkinClientContext.getClient().finishSpan();
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
