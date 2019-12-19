package com.preapm.agent.plugin.interceptor.filter;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.sdk.zipkin.ZipkinClientContext;

import zipkin.Span;

/**
 * org.apache.http.impl.client.HttpClientBuilder.HttpClientBuilder()
 * 
 * @author Zengmin.Zhang
 *
 */
public class HttpRequestFilter implements HttpRequestInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(HttpRequestFilter.class);

	@Override
	public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
		try {
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
		}

	}

}
