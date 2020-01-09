package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;

/**
 * 
 * 
 * @author Zengmin.Zhang
 *
 */
public class Log4jInterceptor implements AroundInterceptor {

	public static String PACKAGENAME = "com.preapm.agent.plugin.interceptor.filter";

	@Override
	public void before(MethodInfo methodInfo) {

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
