package com.preapm.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.preapm.agent.util.PreApmConfigUtil;
import com.preapm.agent.weave.BaseCollector;
import com.preapm.agent.weave.Collector;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

public class APMAgent implements ClassFileTransformer {

	private static Collector[] collectors;

	static {
		collectors = new Collector[] { new BaseCollector() };
	}

	private Map<ClassLoader, ClassPool> classPoolMap = new ConcurrentHashMap<>();

	@Override
	public byte[] transform(ClassLoader classLoader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		if ((className == null) || (classLoader == null)
				|| (classLoader.getClass().getName().equals("sun.reflect.DelegatingClassLoader"))
				|| (classLoader.getClass().getName().equals("org.apache.catalina.loader.StandardClassLoader"))
				|| (classLoader.getClass().getName().equals("javax.management.remote.rmi.NoCallStackClassLoader"))
				|| (classLoader.getClass().getName().equals("com.alibaba.fastjson.util.ASMClassLoader"))
				|| (className.indexOf("$Proxy") != -1) || (className.startsWith("java"))) {
			return null;
		}

		className = className.replaceAll("/", ".");
		if (!PreApmConfigUtil.isTarget(className)) {
			return null;
		}

		// 不同的ClassLoader使用不同的ClassPool
		ClassPool localClassPool;
		if (!this.classPoolMap.containsKey(classLoader)) {
			localClassPool = new ClassPool();
			localClassPool.insertClassPath(new LoaderClassPath(classLoader));
			this.classPoolMap.put(classLoader, localClassPool);
		} else {
			localClassPool = this.classPoolMap.get(classLoader);
		}

		try {
			CtClass localCtClass = localClassPool.get(className);
			for (Collector collector : collectors) {
					byte[] arrayOfByte = collector.transform(classLoader, className, classfileBuffer, localCtClass);
					System.out.println(String.format("%s APM agent insert success", new Object[] { className }));
					return arrayOfByte;
			}
		} catch (Throwable localThrowable) {
			new Exception(String.format("%s APM agent insert fail", new Object[] { className }), localThrowable)
					.printStackTrace();
		}
		return null;
	}
}