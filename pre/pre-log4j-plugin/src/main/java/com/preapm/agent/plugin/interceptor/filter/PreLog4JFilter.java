package com.preapm.agent.plugin.interceptor.filter;

import org.apache.log4j.MDC;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import com.preapm.sdk.zipkin.ZipkinClientContext;

import zipkin.Span;

public class PreLog4JFilter extends Filter {

	@Override
	public int decide(LoggingEvent event) {

		Span sapn = ZipkinClientContext.getClient().getSpan();
		if (sapn != null) {
			MDC.put("X-B3-TraceId", Long.toHexString(sapn.traceId));
			MDC.put("X-B3-SpanId", Long.toHexString(sapn.id));
		}
		String message = event.getRenderedMessage();
		if (message.startsWith(com.preapm.sdk.zipkin.util.TraceKeys.LOG_TRACER_PREFIX)) {
			try {
				Span span = ZipkinClientContext.getClient().getSpan();
				if (span != null) {
					ZipkinClientContext.getClient().sendBinaryAnnotation(message, message, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ACCEPT;
	}

}