package com.preapm.agent.common.context;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.preapm.agent.common.interceptor.ClassInterceptor;

import javassist.CtClass;

public class ClassInterceptorContext {

	public static Map<String, ClassInterceptor> interceptorsMap = new ConcurrentHashMap<String, ClassInterceptor>();

	public static void call(List<String> names, CtClass ctClass,ClassLoader classLoader) {
		if (names == null || ctClass == null) {
			return;
		}
		for (String name : names) {
			call(name, ctClass,classLoader);
		}
	}

	public static ClassInterceptor get(String name,ClassLoader classLoader) {
		ClassInterceptor newInstance = interceptorsMap.get(name);
		if (newInstance != null) {
			return newInstance;
		}
		try {
			if(classLoader == null) {
				classLoader = ClassLoader.getSystemClassLoader();
			}
			Class<?> classPlugin = Class.forName(name,false,classLoader);
			newInstance = (ClassInterceptor) classPlugin.newInstance();
			interceptorsMap.put(name, newInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newInstance;
	}

	public static void call(String name, CtClass ctClass,ClassLoader classLoader) {
		get(name,classLoader).callback(ctClass);

	}

}
