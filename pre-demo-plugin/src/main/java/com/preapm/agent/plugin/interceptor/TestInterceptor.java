package com.preapm.agent.plugin.interceptor;

import java.util.Arrays;

import com.preapm.agent.common.interceptor.AroundInterceptor;

public class TestInterceptor implements AroundInterceptor{

	@Override
	public void before(Object target, Object[] args) {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor  before taget :"+target);
		System.out.println("TestInterceptor  before args :"+Arrays.toString(args));
	}

	@Override
	public void after(Object target, Object[] args, Object result, Throwable throwable) {
		System.out.println("TestInterceptor  after taget :"+target);
		System.out.println("TestInterceptor  after args :"+Arrays.toString(args));
		System.out.println("TestInterceptor  after result :"+result);
		System.out.println("TestInterceptor  after throwable :"+throwable);
	}

}
