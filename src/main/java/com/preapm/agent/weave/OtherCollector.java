package com.preapm.agent.weave;

import com.preapm.agent.bean.Statistics;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class OtherCollector extends Collector {

	public static OtherCollector INSTANCE = new OtherCollector();

	private OtherCollector() {
	}

	private static final String beginSrc;
	private static final String endSrc = "inst.end(statistic);";
	private static final String errorSrc;
	private static final String weavePackageName = "com.preapm.agent.weave";
	private static final String beanPackageName = "com.preapm.agent.bean";

	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append(weavePackageName + ".OtherCollector inst=" + weavePackageName + ".OtherCollector.INSTANCE;");
		stringBuilder.append(beanPackageName + ".Statistics statistic = inst.start(\"%s\");");
		beginSrc = stringBuilder.toString();
		errorSrc = "inst.error(statistic,e);";

	}

	@Override
	public byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass) {
		try {
			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				String longName = ctMethod.getLongName();
				if ((Modifier.isPublic(ctMethod.getModifiers())) && (!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers()))) && isTarget(className, longName)) {
					ClassWrapper classWrapper = new ClassWrapperSpringZipkin();
					classWrapper.beginSrc(String.format(beginSrc, ctMethod.getLongName()));
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(classLoader, classfileBuffer, ctClass, ctMethod, classWrapper);
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
