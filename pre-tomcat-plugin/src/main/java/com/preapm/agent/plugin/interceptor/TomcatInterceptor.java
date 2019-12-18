package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;

/**
 * org.apache.catalina.connector.Request.Request()
 * org.apache.catalina.connector.Request.getRequest()
 * 
 * @author Zengmin.Zhang
 *
 */
public class TomcatInterceptor implements AroundInterceptor {

	 
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
