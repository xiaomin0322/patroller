
package com.preapm.agent.common.interceptor;

import javassist.CtClass;

public interface ClassInterceptor extends Interceptor {

	String name();

	void callback(CtClass ctClass);
}
