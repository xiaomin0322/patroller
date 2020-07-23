package com.preapm.agent.common.context;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

import com.preapm.agent.common.interceptor.ClassInterceptor;

import javassist.CtClass;

public class ClassInterceptorContext {

	public static Map<String, ClassInterceptor> interceptorsMap = new ConcurrentHashMap<String, ClassInterceptor>();

	static {
		init();
	}

	public static void init() {
		synchronized (ClassInterceptorContext.class) {
			ServiceLoader<ClassInterceptor> serviceLoader = ServiceLoader.loadInstalled(ClassInterceptor.class);
			Iterator<ClassInterceptor> iterator = serviceLoader.iterator();
			while (iterator.hasNext()) {
				ClassInterceptor animal = iterator.next();
				String name = animal.name();
				interceptorsMap.put(name, animal);
			}
		}
	}

	public static void call(List<String> names, CtClass ctClass) {
		if (names == null || ctClass == null) {
			return;
		}
		for (String name : names) {
			call(name, ctClass);
		}
	}

	public static void call(String name, CtClass ctClass) {
		if (!interceptorsMap.containsKey(name)) {
			return;
		}
		interceptorsMap.get(name).callback(ctClass);

	}
}
