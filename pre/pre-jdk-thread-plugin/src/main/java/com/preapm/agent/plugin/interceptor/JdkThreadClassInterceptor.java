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
			
			//也可在外面通用拦截器配置
	/*		CtMethod declaredMethod = ctClass.getDeclaredMethod("run");
			if(declaredMethod == null) {
				declaredMethod = ctClass.getDeclaredMethod("call");
			}
			declaredMethod.insertBefore("com.preapm.sdk.zipkin.ZipkinClientContext.getClient().getSpanStore().setSpan(span.toBuilder());");
			declaredMethod.insertAfter("com.preapm.sdk.zipkin.ZipkinClientContext.getClient().getSpanStore().removeSpan();");
*/		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
