package com.preapm.agent.plugin.interceptor.filter;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.sdk.zipkin.ZipkinClientContext;

@WebFilter(filterName = "ATraceFilter", urlPatterns = "/*")
public class ATraceFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(ATraceFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			String url = request.getRequestURL().toString();
			String trace_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
			String span_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID);
			logger.debug("获取trace_id：" + trace_id);
			logger.debug("获取trace_id：" + span_id);
			if (trace_id != null && span_id != null) {
				BigInteger trace_id_bi = new BigInteger(trace_id, 16);
				BigInteger span_id_bi = new BigInteger(span_id, 16);
				ZipkinClientContext.getClient().startSpan(trace_id_bi.longValue(), span_id_bi.longValue(), url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		filterChain.doFilter(servletRequest, servletResponse);

		ZipkinClientContext.getClient().finishSpan();

	}

	@Override
	public void destroy() {

	}
}