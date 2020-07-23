package com.preapm.agent.weave;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.util.PreConfigUtil;

import javassist.CtClass;

public abstract class Collector {

	public boolean isTarget(String className, String method) {
		return PreConfigUtil.isTarget(className, method);
	}

	public boolean isTarget(String className) {
		return PreConfigUtil.isTarget(className);
	}

	public boolean isTarget(String className, Class<?> clazz) {
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return false;
		}
		List<String> superClass = patterns.getSuperClass();
		if (superClass == null) {
			return true;
		}
		for (String superClazz : superClass) {
			try {
				Class<?> classPlugin = Class.forName(superClazz);
				classPlugin.isAssignableFrom(clazz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public abstract byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer,
			CtClass ctClass);

}