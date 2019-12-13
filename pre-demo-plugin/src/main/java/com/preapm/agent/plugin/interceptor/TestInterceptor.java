package com.preapm.agent.plugin.interceptor;

import java.util.Arrays;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;

public class TestInterceptor implements AroundInterceptor {

	@Override
	public void before(MethodInfo methodInfo) {
		// TODO Auto-generated method stub
		try {
			System.out.println("TestInterceptor  before taget :" + methodInfo.getTarget());
			System.out.println("TestInterceptor  before args :" + Arrays.toString(methodInfo.getArgs()));
			System.out.println("TestInterceptor  before getArgsName :" + Arrays.toString(methodInfo.getArgsName()));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor  after throwable :" + methodInfo.getThrowable());
	}

	@Override
	public void after(MethodInfo methodInfo) {
		System.out.println("TestInterceptor  before taget :" + methodInfo.getTarget());
		System.out.println("TestInterceptor  before args :" + Arrays.toString(methodInfo.getArgs()));
		System.out.println("TestInterceptor  before getArgsName :" + Arrays.toString(methodInfo.getArgsName()));
		System.out.println("TestInterceptor  after result :" + methodInfo.getResult());
	}

}
