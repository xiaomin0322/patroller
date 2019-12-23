package com.preapm.agent.weave;

import com.preapm.agent.util.PreConfigUtil;

import javassist.CtClass;

public abstract class Collector {

	public boolean isTarget(String className, String method) {
		return PreConfigUtil.isTarget(className, method);
	}

	public boolean isTarget(String className) {
		return PreConfigUtil.isTarget(className);
	}

	public abstract byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer,
			CtClass ctClass);

}