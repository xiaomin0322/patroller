package com.preapm.agent.weave.impl;

import java.util.logging.Logger;

import com.preapm.agent.util.LogManager;
import com.preapm.agent.weave.ClassReplacer;
import com.preapm.agent.weave.ClassWrapper;
import com.preapm.agent.weave.Collector;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class BaseCollector extends Collector {

	private static Logger log = LogManager.getLogger(BaseCollector.class);

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
			log.severe(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}

		return new byte[0];
	}

}
