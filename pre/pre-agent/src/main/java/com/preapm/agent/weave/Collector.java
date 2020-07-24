package com.preapm.agent.weave;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.util.PreConfigUtil;

import javassist.CtClass;
import javassist.NotFoundException;

public abstract class Collector {

	public boolean isTarget(String className, String method) {
		return PreConfigUtil.isTarget(className, method);
	}

	public boolean isTarget(String className) {
		return PreConfigUtil.isTarget(className);
	}

	public boolean isTarget(String className, CtClass ctClass) {
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return false;
		}
		List<String> superClass = patterns.getSuperClass();
		if (superClass == null) {
			return true;
		}
		CtClass[] interfaces = null;
		try {
			interfaces = ctClass.getInterfaces();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		if (ArrayUtils.isEmpty(interfaces)) {
			return false;
		}
		for (CtClass c : interfaces) {
			String name = c.getName();
			if (superClass.contains(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isTargetC(String className) {
		Patterns patterns = PreConfigUtil.get(className);
		if (className.contains("preapm")) {
			System.out.println("====" + className);
		}
		if (patterns == null) {
			return false;
		}
		List<String> superClass = patterns.getSuperClass();
		if (superClass == null) {
			return true;
		}
		try {
			Class<?> classSrc = Class.forName(className);
			for (String superClazz : superClass) {

				Class<?> classPlugin = Class.forName(superClazz);
				boolean assignableFrom = classPlugin.isAssignableFrom(classSrc);
				if (assignableFrom) {
					return assignableFrom;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public abstract byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer,
			CtClass ctClass);

}