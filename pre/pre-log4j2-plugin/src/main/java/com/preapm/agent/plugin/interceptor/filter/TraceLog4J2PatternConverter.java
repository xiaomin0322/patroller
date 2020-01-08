package com.preapm.agent.plugin.interceptor.filter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;

import zipkin.Span;

@Plugin(name = "PreTraceLog4J2PatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "pt", "X-B3-TraceId" })
public class TraceLog4J2PatternConverter extends LogEventPatternConverter {

	private static final TraceLog4J2PatternConverter INSTANCE = new TraceLog4J2PatternConverter();

	public static TraceLog4J2PatternConverter newInstance(final String[] options) {
		return INSTANCE;
	}

	private TraceLog4J2PatternConverter() {
		super("X-B3-TraceId", "X-B3-TraceId");
	}

	@Override
	public void format(LogEvent logEvent, StringBuilder toAppendTo) {
		ZipkinClient client = ZipkinClientContext.getClient();
		String str = "";
		if (client != null) {
			Span span = client.getSpan();
			if(span!=null) {
				str = String.valueOf(span.traceId);
			}
			
		}
		toAppendTo.append(str);
	}
}