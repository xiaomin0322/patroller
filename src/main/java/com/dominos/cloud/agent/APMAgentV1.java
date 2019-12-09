package com.dominos.cloud.agent;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;

public class APMAgentV1 implements ClassFileTransformer {

	private static Collector[] collectors;

	static {
		collectors = new Collector[] { OtherCollector.INSTANCE };
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
		
		System.out.println("transform.classLoader："+classLoader.getClass().getName());

		className = className.replaceAll("/", ".");
		if (!OtherCollector.INSTANCE.isTarget(className)) {
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
				if (collector.isTarget(className, classLoader, localCtClass)) {
					byte[] arrayOfByte = collector.transform(classLoader, className, classfileBuffer, localCtClass);
					System.out.println(String.format("%s APM agent insert success", new Object[] { className }));
					return arrayOfByte;
				}
			}
		} catch (Throwable localThrowable) {
			new Exception(String.format("%s APM agent insert fail", new Object[] { className }), localThrowable)
					.printStackTrace();
		}
		return null;
	}

	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Hello, world! JavaAgen");
		System.out.println("agentArgs: " + agentArgs);
		File jar = new File("C:\\eclipse-workspace\\zipkin-agent\\target\\lib");
		/*try {
			for(File f:jar.listFiles()) {
				//inst.appendToBootstrapClassLoaderSearch(new JarFile(new File("C:\\eclipse-workspace\\zipkin-agent\\target\\zipkin-agent.jar")));
				//inst.appendToBootstrapClassLoaderSearch(new JarFile(f));
				inst.appendToSystemClassLoaderSearch(new JarFile(f));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		inst.addTransformer(new APMAgentV1());
	}
	

}