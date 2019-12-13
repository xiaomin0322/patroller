
package com.preapm.agent.common.interceptor;

public interface AroundInterceptor extends Interceptor {

	void before(Object target, Object[] args);

	void after(Object target, Object[] args, Object result, Throwable throwable);
}
