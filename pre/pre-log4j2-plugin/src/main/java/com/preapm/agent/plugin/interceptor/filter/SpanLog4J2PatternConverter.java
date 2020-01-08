package com.preapm.agent.plugin.interceptor.filter;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;

import zipkin.Span;

@Plugin(name = "PreSpanLog4J2PatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "ps", "X-B3-SpanId" })
public class SpanLog4J2PatternConverter extends LogEventPatternConverter {

	private static final SpanLog4J2PatternConverter INSTANCE = new SpanLog4J2PatternConverter();

	public static SpanLog4J2PatternConverter newInstance(final String[] options) {
		return INSTANCE;
	}

	private SpanLog4J2PatternConverter() {
		super("X-B3-SpanId", "X-B3-SpanId");
	}

	@Override
	public void format(LogEvent logEvent, StringBuilder toAppendTo) {
		ZipkinClient client = ZipkinClientContext.getClient();
		String str = "SpanId";
		if (client != null) {
			Span span = client.getSpan();
			if (span != null) {
				str = String.valueOf(span.id);
			}
		}
		toAppendTo.append(str);
	}
}