package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.interceptor.ClassInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

public class JdkThreadClassInterceptor implements ClassInterceptor {

	@Override
	public String name() {
		return this.getClass().getName();
	}

	@Override
	public void callback(CtClass ctClass) {
		try {
			CtField f = CtField.make(
					"private zipkin.Span span =  com.preapm.sdk.zipkin.ZipkinClientContext.getClient().getSpan();",
					//"private zipkin.Span span = null;",
					ctClass);
			ctClass.addField(f);
			
			//在此处植入拦截器比较合适，因为这是一个逻辑，如果分开在外面配置拦截器，有可能不同的线程在不同的包中，比较分散，导致配置的不全。会出现问题
			CtMethod declaredMethod = ctClass.getDeclaredMethod("run");
			if(declaredMethod == null) {
				declaredMethod = ctClass.getDeclaredMethod("call");
			}
			declaredMethod.insertBefore("if(span!=null){com.preapm.sdk.zipkin.ZipkinClientContext.getClient().getSpanStore().setSpan(span.toBuilder());}");
			declaredMethod.insertAfter("if(span!=null){com.preapm.sdk.zipkin.ZipkinClientContext.getClient().getSpanStore().removeSpan();}");
	  } catch (Exception e) {
			e.printStackTrace();
		}

	}

}
