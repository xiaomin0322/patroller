package com.preapm.agent.plugin.interceptor;

import java.lang.reflect.Field;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;

public class JdkThreadInterceptor implements AroundInterceptor {

	@Override
	public void before(MethodInfo methodInfo) {
		try {
			Class<? extends MethodInfo> class1 = methodInfo.getClass();
			Field declaredField = class1.getDeclaredField("span");
			if (declaredField == null) {
				return;
			}
			zipkin.Span span = (zipkin.Span) declaredField.get(methodInfo.getTarget());
			ZipkinClientContext.getClient().getSpanStore().setSpan(span.toBuilder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void exception(MethodInfo methodInfo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void after(MethodInfo methodInfo) {
		ZipkinClientContext.getClient().getSpanStore().removeSpan();

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
