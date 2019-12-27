package com.preapm.agent.plugin.interceptor.filter;

import java.util.Arrays;

import org.slf4j.MDC;

import com.preapm.sdk.zipkin.ZipkinClientContext;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import zipkin.Span;

public class BackLogFilter extends Filter<ILoggingEvent> {

	@Override
	public FilterReply decide(ILoggingEvent event) {

		Object[] argumentArray = event.getArgumentArray();
		if (argumentArray == null || argumentArray.length == 0) {
			return FilterReply.ACCEPT;
		}
		
		Span sapn = ZipkinClientContext.getClient().getSpan();
		if(sapn != null) {
			MDC.put("X-B3-TraceId",Long.toHexString(sapn.traceId));
			MDC.put("X-B3-SpanId",Long.toHexString(sapn.id));
		}
		String message = event.getMessage();
		if (message.startsWith("tracer")) {
			try {
				Span span = ZipkinClientContext.getClient().getSpan();
				if(span!=null) {
					ZipkinClientContext.getClient().sendBinaryAnnotation(message, Arrays.toString(argumentArray), null);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return FilterReply.ACCEPT;
	}

}