package com.preapm.agent.plugin.interceptor;

import java.util.Arrays;
import java.util.Objects;

import org.springframework.cloud.sleuth.Tracer;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.util.PreSpringBeanUtils;

public class SpringSlethInterceptor implements AroundInterceptor {

	private ThreadLocal<Tracer> threadLocalTracer = new ThreadLocal<>();

	private ThreadLocal<org.springframework.cloud.sleuth.Span> threadLocalSpan = new ThreadLocal<>();

	@Override
	public void before(MethodInfo methodInfo) {
		try {
			System.out.println("SpringSlethInterceptor  before taget :" + methodInfo.getTarget());
			System.out.println("SpringSlethInterceptor  before args :" + Arrays.toString(methodInfo.getArgs()));
			System.out.println(
					"SpringSlethInterceptor  before getArgsName :" + Arrays.toString(methodInfo.getArgsName()));

			org.springframework.cloud.sleuth.Span newSpan = null;
			org.springframework.cloud.sleuth.Tracer tracer = null;
			tracer =  PreSpringBeanUtils.getBean(org.springframework.cloud.sleuth.Tracer.class);
			if (tracer != null) {
				org.springframework.cloud.sleuth.Span currentSpan = tracer.getCurrentSpan();
				newSpan = tracer.createSpan(methodInfo.getMethodName(), currentSpan);
				System.out.println("加入span成功：" + methodInfo.getMethodName());
				threadLocalTracer.set(tracer);
				threadLocalSpan.set(newSpan);

				if (methodInfo.getArgs() != null) {
					for (int i = 0; i < methodInfo.getArgs().length; i++) {
						String arg = methodInfo.getArgsName()[i];
						Object o = methodInfo.getArgs()[i];
						newSpan.tag(arg, String.valueOf(o));
						System.out.println("参数名称:" + arg + "参数值：" + o);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
		// TODO Auto-generated method stub
		System.out.println("SpringSlethInterceptor  after throwable :" + methodInfo.getThrowable());
		org.springframework.cloud.sleuth.Span newSpan = threadLocalSpan.get();
		if (newSpan != null) {
			newSpan.tag("error",
					org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(methodInfo.getThrowable()));
		}
	}

	@Override
	public void after(MethodInfo methodInfo) {
		System.out.println("SpringSlethInterceptor  before taget :" + methodInfo.getTarget());
		System.out.println("SpringSlethInterceptor  before args :" + Arrays.toString(methodInfo.getArgs()));
		System.out.println("SpringSlethInterceptor  before getArgsName :" + Arrays.toString(methodInfo.getArgsName()));
		System.out.println("SpringSlethInterceptor  after result :" + methodInfo.getResult());

		org.springframework.cloud.sleuth.Span newSpan = threadLocalSpan.get();
		org.springframework.cloud.sleuth.Tracer tracer = threadLocalTracer.get();

		if (newSpan != null && tracer != null) {
			if (!Objects.isNull(methodInfo.getResult())) {
				newSpan.tag("out", String.valueOf(methodInfo.getResult()));
				System.out.println("返回值：" + methodInfo.getResult());
			}
			newSpan.logEvent(org.springframework.cloud.sleuth.Span.CLIENT_RECV);
			tracer.close(newSpan);
		}
		
		threadLocalSpan.remove();
		threadLocalTracer.remove();
		
	}

}
