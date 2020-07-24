package com.preapm.agent.weave.impl;

import java.util.logging.Logger;

import com.preapm.agent.bean.PatternsYaml.PatternMethod;
import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.common.context.ClassInterceptorContext;
import com.preapm.agent.constant.BaseConstants;
import com.preapm.agent.enums.PatternEnum;
import com.preapm.agent.util.LogManager;
import com.preapm.agent.util.PreConfigUtil;
import com.preapm.agent.weave.ClassReplacer;
import com.preapm.agent.weave.ClassWrapper;
import com.preapm.agent.weave.Collector;

import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.Modifier;

public class AroundInterceptorCollector extends Collector {
	private static Logger log = LogManager.getLogger(AroundInterceptorCollector.class);

	public static AroundInterceptorCollector INSTANCE = new AroundInterceptorCollector();

	public AroundInterceptorCollector() {
	}

	private static String beginSrc = BaseConstants.NULL;
	private static String endSrc = BaseConstants.NULL;
	private static String errorSrc = BaseConstants.NULL;

	@Override
	public byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass) {
		String longName = null;
		try {

			// 处理植入classs拦截器逻辑
			boolean target = isTarget(className, ctClass, PatternEnum.Class.getCode());
			if (target) {
				Patterns patterns = PreConfigUtil.get(className, PatternEnum.Class.getCode());
				// 拦截器中修改class
				ClassInterceptorContext.call(patterns.getInterceptors(), ctClass, classLoader);
				//FileUtils.writeByteArrayToFile(new File("C:\\root\\"+className), ctClass.toBytecode());
			}
			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				longName = ctMethod.getLongName();
				if (/* (Modifier.isPublic(ctMethod.getModifiers())) && */(!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers())))) {
					PatternMethod patternMethod = PreConfigUtil.isTargetR(className, longName);
					if (patternMethod == null) {
						continue;
					}
					Patterns patterns = PreConfigUtil.get(className, PatternEnum.Around.getCode());
					ClassWrapper classWrapper = new ClassWrapperAroundInterceptor(patterns, patternMethod);
					classWrapper.beginSrc(beginSrc);
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(classLoader, classfileBuffer, ctClass, ctMethod, classWrapper);
				}
			}

			/*
			 * for (CtMethod ctMethod : ctClass.getMethods()) { longName =
			 * ctMethod.getLongName(); if ( (Modifier.isPublic(ctMethod.getModifiers())) &&
			 * (!Modifier.isStatic(ctMethod.getModifiers()) &&
			 * (!Modifier.isNative(ctMethod.getModifiers()))) &&
			 * !methodNameSet.contains(longName)) { PatternMethod patternMethod =
			 * PreConfigUtil.isTargetR(className, longName); if (patternMethod == null) {
			 * continue; } Patterns patterns = PreConfigUtil.get(className); ClassWrapper
			 * classWrapper = new ClassWrapperAroundInterceptor(patterns, patternMethod);
			 * classWrapper.beginSrc(beginSrc); classWrapper.endSrc(endSrc);
			 * classWrapper.errorSrc(errorSrc); replacer.replace(classLoader,
			 * classfileBuffer, ctClass, ctMethod, classWrapper);
			 * 
			 * methodNameSet.add(longName); } }
			 */

			CtConstructor[] constructors = ctClass.getDeclaredConstructors();
			if (constructors != null && constructors.length > 0) {
				for (CtConstructor c : constructors) {
					longName = c.getLongName();
					PatternMethod patternMethod = PreConfigUtil.isTargetR(className, longName);
					if (patternMethod == null) {
						continue;
					}
					Patterns patterns = PreConfigUtil.get(className, PatternEnum.Around.getCode());
					ClassWrapper classWrapper = new ClassWrapperAroundInterceptor(patterns, patternMethod);
					classWrapper.beginSrc(beginSrc);
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(classLoader, classfileBuffer, ctClass, c, classWrapper);

				}
			}

			return replacer.replace();
		} catch (Exception e) {

			log.severe("methodName: " + longName + "\n "
					+ org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}

		return new byte[0];
	}

}
