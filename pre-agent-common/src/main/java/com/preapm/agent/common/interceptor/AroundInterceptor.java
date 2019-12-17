
package com.preapm.agent.common.interceptor;

import com.preapm.agent.common.bean.MethodInfo;

public interface AroundInterceptor extends Interceptor {

	void before(MethodInfo methodInfo);
	
	void exception(MethodInfo methodInfo);

	void after(MethodInfo methodInfo);
	
	//void name();
}
