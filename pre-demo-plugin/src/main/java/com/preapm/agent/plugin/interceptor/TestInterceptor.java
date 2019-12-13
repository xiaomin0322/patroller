package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.interceptor.AroundInterceptor;

public class TestInterceptor implements AroundInterceptor{

	@Override
	public void before(Object target, Object[] args) {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor  before");
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor  after");
	}

}
