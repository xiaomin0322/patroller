package com.preapm.agent.weave;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class BaseCollector extends Collector {

	@Override
	public byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass) {
		try {
			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				String longName = ctMethod.getLongName();
				if ((Modifier.isPublic(ctMethod.getModifiers())) && (!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers()))) && isTarget(className, longName)) {
					ClassWrapper classWrapper = new ClassWrapperSpringZipkin();
					replacer.replace(classLoader, classfileBuffer, ctClass, ctMethod, classWrapper);
				}
			}
			return replacer.replace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new byte[0];
	}

}
