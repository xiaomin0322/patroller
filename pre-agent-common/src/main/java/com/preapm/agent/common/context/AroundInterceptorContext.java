package com.preapm.agent.common.context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import com.preapm.agent.common.interceptor.AroundInterceptor;

public class AroundInterceptorContext {

	// public static Map<String, AroundInterceptor> interceptors = new
	// HashMap<String, AroundInterceptor>();

	public static Set<AroundInterceptor> interceptors = new HashSet<AroundInterceptor>();

	public static void start(Object target, Object[] args) {
		for (AroundInterceptor i : interceptors) {
			i.before(target, args);
		}
	}

	static {
		init();
	}

	public static void target(Object target, Object[] args, Object result, Throwable throwable) {
		for (AroundInterceptor i : interceptors) {
			i.after(target, args, result, throwable);
		}
	}

	public static void init() {
		synchronized (AroundInterceptorContext.class) {
			if (interceptors == null || interceptors.size() == 0) {
				ServiceLoader<AroundInterceptor> serviceLoader = ServiceLoader.load(AroundInterceptor.class);
				Iterator<AroundInterceptor> iterator = serviceLoader.iterator();
				while (iterator.hasNext()) {
					AroundInterceptor animal = iterator.next();
					interceptors.add(animal);
				}
			}
		}

	}

	public static void addInterceptor(AroundInterceptor aroundInterceptor) {
		interceptors.add(aroundInterceptor);
	}
}
