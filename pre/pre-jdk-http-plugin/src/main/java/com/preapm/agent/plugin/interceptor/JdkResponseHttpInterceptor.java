package com.preapm.agent.plugin.interceptor;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.TraceKeys;

import sun.net.www.MessageHeader;
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
	// private final Logger logger =
	// LoggerFactory.getLogger(JdkConnectHttpInterceptor.class);
	@Override
	public void before(MethodInfo methodInfo) {
	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		try {
			HttpURLConnection connection = (HttpURLConnection) methodInfo.getTarget();
			Class clazz = getSupperClass(connection.getClass());
			Field responseCodeField = clazz.getDeclaredField("responseCode");
			responseCodeField.setAccessible(true);
			Integer responseCode = (Integer) responseCodeField.get(connection);
			if (responseCode == null || responseCode.intValue() == -1 || responseCode.intValue() == 301
					|| responseCode.intValue() == 302 || responseCode.intValue() == 303
					|| responseCode.intValue() == 307 || responseCode.intValue() == 308) {
				return;
			}
			/*
			 * String headerField =
			 * getHeader(com.preapm.sdk.zipkin.util.TraceKeys.PRE_AGENT_NOT_TRACE_TAG,
			 * connection); if(headerField != null) {
			 * ZipkinClientContext.getClient().getSpanStore().removeSpan(); return; }
			 */
			Span span = ZipkinClientContext.getClient().getSpan();
			if(span == null) {
				return;
			}
			if(!com.preapm.agent.plugin.interceptor.JdkConnectHttpInterceptor.SPAN_NAME_STR.equals(span.name)) {
				return;
			}
			ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_RECV);
			ZipkinClientContext.getClient().finishSpan();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getHeader(String name, Object connection) {
		try {
			Field requestsField = connection.getClass().getDeclaredField("cachedHeaders");
			requestsField.setAccessible(true);
			MessageHeader messageHeader = (MessageHeader) requestsField.get(connection);
			if (messageHeader == null) {
				requestsField = connection.getClass().getDeclaredField("requests");
				requestsField.setAccessible(true);
				messageHeader = (MessageHeader) requestsField.get(connection);
			}
			if (messageHeader == null) {
				requestsField = connection.getClass().getDeclaredField("responses");
				requestsField.setAccessible(true);
				messageHeader = (MessageHeader) requestsField.get(connection);
			}
			String headerField = messageHeader.findValue(name);
			return headerField;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

	private Class getSupperClass(Class clazz) {
		if (clazz == HttpURLConnection.class) {
			return clazz;
		}
		return getSupperClass(clazz.getSuperclass());
	}

}
