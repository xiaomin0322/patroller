package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.filter.PreLog4JFilter;

/**
 * 
 * org.apache.log4j.PropertyConfigurator.parseAppender(Properties, String)
 * 
 * @author Zengmin.Zhang
 *
 */
public class Log4jInterceptor implements AroundInterceptor {

	@Override
	public void before(MethodInfo methodInfo) {

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		org.apache.log4j.Appender appender = (org.apache.log4j.Appender) methodInfo.getResult();
		if (appender != null) {
			appender.addFilter(new PreLog4JFilter());
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
