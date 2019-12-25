package com.preapm.agent.weave.impl;

import java.util.Set;
import java.util.logging.Logger;

import com.preapm.agent.bean.PatternsYaml.PatternMethod;
import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.bean.PluginConfigYaml.JarBean;
import com.preapm.agent.constant.BaseConstants;
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
			// 加载插件后，初始化插件
			com.preapm.agent.util.ClassLoaderUtil.loadJarByClassName(classLoader, className);

			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				 longName = ctMethod.getLongName();
				if (/* (Modifier.isPublic(ctMethod.getModifiers())) && */(!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers())))) {
					PatternMethod patternMethod = PreConfigUtil.isTargetR(className, longName);
					if (patternMethod == null) {
						continue;
					}
					Patterns patterns = PreConfigUtil.get(className);
					ClassWrapper classWrapper = new ClassWrapperAroundInterceptor(patterns, patternMethod);
					classWrapper.beginSrc(beginSrc);
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(classLoader, classfileBuffer, ctClass, ctMethod, classWrapper);
				}
			}
			CtConstructor[] constructors = ctClass.getDeclaredConstructors();
			if (constructors != null && constructors.length > 0) {
				for (CtConstructor c : constructors) {
					 longName = c.getLongName();
					PatternMethod patternMethod = PreConfigUtil.isTargetR(className, longName);
					if (patternMethod == null) {
						continue;
					}
					Patterns patterns = PreConfigUtil.get(className);
					ClassWrapper classWrapper = new ClassWrapperAroundInterceptor(patterns, patternMethod);
					classWrapper.beginSrc(beginSrc);
					classWrapper.endSrc(endSrc);
					classWrapper.errorSrc(errorSrc);
					replacer.replace(classLoader, classfileBuffer, ctClass, c, classWrapper);

				}
			}

			return replacer.replace();
		} catch (Exception e) {
			
			log.severe("methodName: "+longName + "\n "+org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}

		return new byte[0];
	}

}
