package com.dominos.cloud.agent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dominos.cloud.agent.util.ReflectUtil;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class OtherCollector implements Collector {

	public static OtherCollector INSTANCE = new OtherCollector();
	

	private OtherCollector() {
	}

	private static final String beginSrc;
	private static final String endSrc = "inst.end(statistic);";
	private static final String errorSrc;

	private static Map<String, Set<String>> targetMap = new HashMap<>();

	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("com.dominos.cloud.agent.OtherCollector inst=com.dominos.cloud.agent.OtherCollector.INSTANCE;");
		stringBuilder.append("com.dominos.cloud.agent.Statistics statistic = inst.start(\"%s\");");
		beginSrc = stringBuilder.toString();
		errorSrc = "inst.error(statistic,e);";

		Set<String> methodSet = new HashSet<>();
		methodSet.add("com.dominos.cloud.agent.TestServiceMain.print(java.lang.String)");
		targetMap.put("com.dominos.cloud.agent.TestServiceMain", methodSet);

		// targetSet.add("com.mysql.jdbc.ConnectionImpl");
		// targetSet.add("com.mysql.jdbc.PreparedStatement");

		methodSet = new HashSet<>();
		methodSet = new HashSet<>();
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection()");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection(long)");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnectionDirect(long)");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection(java.lang.String, java.lang.String)");
		targetMap.put("com.alibaba.druid.pool.DruidDataSource", methodSet);
		// targetMap.put("com.alibaba.druid.pool.DruidPooledConnection", null);

	}

	@Override
	public boolean isTarget(String className, ClassLoader classLoader, CtClass ctClass) {
		return targetMap.containsKey(className);
	}

	@Override
	public boolean isTarget(String className) {
		return targetMap.containsKey(className);
	}

	@Override
	public byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass) {
		try {
			Set<String> methodSet = targetMap.get(className);
			if (methodSet == null || methodSet.isEmpty()) {
				return classfileBuffer;
			}
			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				String longName = ctMethod.getLongName();
				if ((Modifier.isPublic(ctMethod.getModifiers())) && (!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers()))) && methodSet.contains(longName)) {
					// System.out.println("ctMethod.getname：" + ctMethod.getLongName() +"
					// methodSet.size : "+methodSet.size());
					
					String methodName = ctMethod.getName();
			    	
			    	List<String> paramNameList = ReflectUtil.getParamNameList(ctMethod);
			    	
			    	System.out.println("方法名称："+methodName+" paramNameList："+Arrays.toString(paramNameList.toArray()));
			    	

					ClassWrapper classWrapper = new ClassWrapper();
					classWrapper.beginSrc(String.format(beginSrc, ctMethod.getLongName()));
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(ctClass,ctMethod, classWrapper);
				}
			}
			return replacer.replace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

	public Statistics start(String methodSign) {
		return new Statistics(methodSign);
	}

	public void end(Statistics statistics) {
		statistics.end();
	}

	public void error(Statistics statistics, Throwable e) {
		statistics.error(e);
	}
}
