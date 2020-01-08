package com.preapm.agent.plugin.interceptor.filter;

import java.util.Arrays;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;

@Plugin(name = "Log4JFilter", category = Node.CATEGORY, elementType = Filter.ELEMENT_TYPE)
public class PreLog4J2Filter extends AbstractFilter {

	@PluginFactory
	public static PreLog4J2Filter createFilter() {
		return new PreLog4J2Filter();
	}

	@Override
	public Result filter(final LogEvent event) {
		Message message = event.getMessage();
		Object[] p = message.getParameters();
		String formattedMessage = message.getFormat();
		if (formattedMessage != null
				&& formattedMessage.startsWith(com.preapm.sdk.zipkin.util.TraceKeys.LOG_TRACER_PREFIX)) {
			ZipkinClient client = ZipkinClientContext.getClient();
			if (client != null) {
				client.sendBinaryAnnotation(formattedMessage, Arrays.toString(p));
			}
		}
		System.out.println("formattedMessage:" + formattedMessage + " getParameters:" + Arrays.toString(p));
		return Result.ACCEPT;
	}
}