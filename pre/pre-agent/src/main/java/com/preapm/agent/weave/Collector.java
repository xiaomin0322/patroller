package com.preapm.agent.weave;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.enums.PatternEnum;
import com.preapm.agent.util.PreConfigUtil;

import javassist.CtClass;
import javassist.NotFoundException;

public abstract class Collector {

	public boolean isTarget(String className, String method) {
		return PreConfigUtil.isTarget(className, method);
	}

	public boolean isTarget(String className) {
		return PreConfigUtil.isTarget(className,PatternEnum.Around);
	}
	
	public boolean isTarget(String className,PatternEnum patternEnum) {
		return PreConfigUtil.isTarget(className,patternEnum);
	}

	public boolean isTarget(String className, CtClass ctClass) {
		return  isTarget(className, ctClass, PatternEnum.Around.getCode());
	}

	public boolean isTarget(String className, CtClass ctClass,String type) {
		Patterns patterns = PreConfigUtil.get(className,type);
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

	public abstract byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer,
			CtClass ctClass);

}