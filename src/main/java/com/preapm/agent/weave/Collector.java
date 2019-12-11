package com.preapm.agent.weave;

import com.preapm.agent.util.PreApmConfigUtil;

import javassist.CtClass;

public abstract class Collector {

	public boolean isTarget(String className, String method) {
		return PreApmConfigUtil.isTarget(className, method);
	}

	public boolean isTarget(String className) {
		return PreApmConfigUtil.isTarget(className);
	}

	public abstract byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer,
			CtClass ctClass);

}