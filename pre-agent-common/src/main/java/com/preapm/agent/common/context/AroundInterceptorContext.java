package com.preapm.agent.common.context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;

public class AroundInterceptorContext {

	public static String agentId = "test";

	public static Set<AroundInterceptor> interceptors = new HashSet<AroundInterceptor>();

	public static Map<String, AroundInterceptor> interceptorsMap = new ConcurrentHashMap<String, AroundInterceptor>();

	public static void start(MethodInfo methodInfo) {
		start(methodInfo, methodInfo.getPlugins());
	}

	public static void start(MethodInfo methodInfo, String... names) {
		System.out.println("start Plugins :"+Arrays.toString(names));
		List<AroundInterceptor> aroundInterceptors = get(names);
		System.out.println("start Plugins aroundInterceptors :"+aroundInterceptors.size());
		for (AroundInterceptor i : get(names)) {
			i.before(methodInfo);
		}
	}

	static {
		init();
	}

	public static void after(MethodInfo methodInfo) {
		after(methodInfo, methodInfo.getPlugins());
	}

	public static void exception(MethodInfo methodInfo) {
		exception(methodInfo, methodInfo.getPlugins());
	}

	public static void after(MethodInfo methodInfo, String... names) {
		for (AroundInterceptor i : get(names)) {
			i.after(methodInfo);
		}
	}

	public static void exception(MethodInfo methodInfo, String... names) {
		for (AroundInterceptor i : get(names)) {
			i.exception(methodInfo);
		}
	}

	public static List<AroundInterceptor> get(Set<String> names) {
		checkName(names);
		System.out.println("com.preapm.agent.common.context.AroundInterceptorContext.get(Set<String>)参数："+names.size());
		System.out.println("com.preapm.agent.common.context.AroundInterceptorContext.get(Set<String>)interceptorsMap参数："+interceptorsMap.size());
		List<AroundInterceptor> list = new ArrayList<AroundInterceptor>();
		for (Entry<String, AroundInterceptor> e : interceptorsMap.entrySet()) {
			String name = e.getKey();
			System.out.println("插件map包的名字："+name);
			if (names.contains(name)) {
				list.add(e.getValue());
				System.out.println("执行插件名称："+name);
			}
		}
		return list;
	}
	
    public static void checkName(Set<String> names) {
    	for(String n :names) {
    		if(!interceptorsMap.containsKey(n)) {
    			init(n);
    			System.out.println("重新加载AroundInterceptor>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    		}
    	}
    }
    
    public static boolean init(String namePlugin) {
    	try {
			Class<?> classPlugin = Class.forName(namePlugin);
			AroundInterceptor newInstance = (AroundInterceptor)classPlugin.newInstance();
			interceptorsMap.put(namePlugin, newInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println("初始化成功 name:"+namePlugin);
    	return true;
    }
    
	public static List<AroundInterceptor> get(String... names) {
		Set<String> set = new HashSet<>();
		for (String n : names) {
			set.add(n);
		}
		return get(set);
	}

	public static void init() {
		synchronized (AroundInterceptorContext.class) {
			//if (interceptors == null || interceptors.size() == 0) {
				ServiceLoader<AroundInterceptor> serviceLoader = ServiceLoader.loadInstalled(AroundInterceptor.class);
				Iterator<AroundInterceptor> iterator = serviceLoader.iterator();
				while (iterator.hasNext()) {
					AroundInterceptor animal = iterator.next();
					interceptors.add(animal);
					String name = animal.name();
					interceptorsMap.put(name, animal);
				}
		//	}
		}

	}

	public static void addInterceptor(AroundInterceptor aroundInterceptor) {
		interceptors.add(aroundInterceptor);
	}
}
