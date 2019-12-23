package com.preapm.agent.plugin.interceptor;

import org.apache.http.client.methods.HttpUriRequest;
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
		/*try {
			HttpUriRequest request = (HttpUriRequest) methodInfo.getArgs()[0];
			if (request != null) {
				Span span = ZipkinClientContext.getClient().getSpan();
				if (span != null) {
					logger.debug("放入traceId到http请求头：" + Long.toHexString(span.traceId));
					logger.debug("放入SPAN_ID到http请求头：" + Long.toHexString(span.id));
					request.addHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID, Long.toHexString(span.traceId));
					request.addHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID, Long.toHexString(span.id));
				} else {
					logger.debug("ZipkinClientContext.getClient().getSpan() is null");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		try {
			org.apache.http.HttpRequest  request = (HttpUriRequest) methodInfo.getArgs()[1];
			if (request != null) {
				int ipv4 = InetAddressUtils.localIpv4();
				Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
				methodInfo.setLocalVariable(new Object[] {endpoint});
				ZipkinClientContext.getClient().startSpan(request.getRequestLine().getUri());
				ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_SEND,endpoint);
				ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.PRE_NAME, "httpclient4", endpoint);
				Span span = ZipkinClientContext.getClient().getSpan();
				if (span != null) {
					logger.debug("放入traceId到http请求头：" + Long.toHexString(span.traceId));
					logger.debug("放入SPAN_ID到http请求头：" + Long.toHexString(span.id));
					request.addHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID, Long.toHexString(span.traceId));
					request.addHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID, Long.toHexString(span.id));
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
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		ZipkinClientContext.getClient().sendAnnotation(TraceKeys.CLIENT_RECV, endpoint );
		ZipkinClientContext.getClient().finishSpan();
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
