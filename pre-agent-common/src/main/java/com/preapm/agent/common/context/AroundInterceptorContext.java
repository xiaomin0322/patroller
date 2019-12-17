package com.preapm.agent.common.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;

public class AroundInterceptorContext {

	public static String agentId = "test";

	public static Set<AroundInterceptor> interceptors = new HashSet<AroundInterceptor>();

	public static Map<String, AroundInterceptor> interceptorsMap = new HashMap<String, AroundInterceptor>();

	public static void start(MethodInfo methodInfo) {
		for (AroundInterceptor i : interceptors) {
			i.before(methodInfo);
		}
	}

	public static void start(MethodInfo methodInfo, Set<String> names) {
		for (AroundInterceptor i : get(names)) {
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

	public static void after(MethodInfo methodInfo, Set<String> names) {
		for (AroundInterceptor i : get(names)) {
			i.after(methodInfo);
		}
	}

	public static void exception(MethodInfo methodInfo, Set<String> names) {
		for (AroundInterceptor i : get(names)) {
			i.exception(methodInfo);
		}
	}

	public static List<AroundInterceptor> get(Set<String> names) {
		List<AroundInterceptor> list = new ArrayList<AroundInterceptor>();
		for (Entry<String, AroundInterceptor> e : interceptorsMap.entrySet()) {
			String name = e.getKey();
			if (names.contains(name)) {
				list.add(e.getValue());
			}
		}
		return list;
	}

	public static void init() {
		synchronized (AroundInterceptorContext.class) {
			if (interceptors == null || interceptors.size() == 0) {
				ServiceLoader<AroundInterceptor> serviceLoader = ServiceLoader.load(AroundInterceptor.class);
				Iterator<AroundInterceptor> iterator = serviceLoader.iterator();
				while (iterator.hasNext()) {
					AroundInterceptor animal = iterator.next();
					interceptors.add(animal);
					String name = animal.getClass().getName();
					interceptorsMap.put(name, animal);
				}
			}
		}

	}

	public static void addInterceptor(AroundInterceptor aroundInterceptor) {
		interceptors.add(aroundInterceptor);
	}
}
