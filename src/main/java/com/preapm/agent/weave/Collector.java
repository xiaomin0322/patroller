package com.preapm.agent.weave;
import java.util.Set;

import com.preapm.agent.util.PreApmConfigUtil;

import javassist.CtClass;

public abstract  class Collector {

	public boolean isTarget(String className, String method) {
		boolean flag = isTarget(className);
		if(!flag) {
			return flag;
		}
		flag = PreApmConfigUtil.get(className).contains(method);
		return flag;
	}

	public boolean isTarget(String className) {
		Set<String> methodSet = PreApmConfigUtil.get(className);
		if (methodSet == null || methodSet.isEmpty()) {
			return false;
		}
		return true;
	}

   public abstract byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass);

}