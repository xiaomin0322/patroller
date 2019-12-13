package com.preapm.agent.common.context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;

public class AroundInterceptorContext {

	// public static Map<String, AroundInterceptor> interceptors = new
	// HashMap<String, AroundInterceptor>();

	public static Set<AroundInterceptor> interceptors = new HashSet<AroundInterceptor>();

	public static void start(MethodInfo methodInfo) {
		for (AroundInterceptor i : interceptors) {
			i.before(methodInfo);
		}
	}

	static {
		init();
	}

	public static void after(MethodInfo methodInfo) {
		for (AroundInterceptor i : interceptors) {
			i.after(methodInfo);
		}
	}
	
	public static void exception(MethodInfo methodInfo) {
		for (AroundInterceptor i : interceptors) {
			i.exception(methodInfo);
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
