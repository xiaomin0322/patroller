package com.preapm.agent.weave.impl;

import java.util.logging.Logger;

import com.preapm.agent.constant.BaseConstants;
import com.preapm.agent.util.ClassLoaderUtil;
import com.preapm.agent.util.LogManager;
import com.preapm.agent.weave.ClassReplacer;
import com.preapm.agent.weave.ClassWrapper;
import com.preapm.agent.weave.Collector;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;

public class AroundInterceptorCollector extends Collector {
	private static Logger log = LogManager.getLogger(AroundInterceptorCollector.class);

	public static AroundInterceptorCollector INSTANCE = new AroundInterceptorCollector();

	public AroundInterceptorCollector() {
	}

	private static String beginSrc=BaseConstants.NULL;
	private static String endSrc=BaseConstants.NULL;

	static {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.start(this, $args);");
		beginSrc = stringBuilder.toString();
		//endSrc = "com.preapm.agent.common.context.AroundInterceptorContext.target(this, $args, result, e);";
	}

	@Override
	public byte[] transform(ClassLoader classLoader, String className, byte[] classfileBuffer, CtClass ctClass) {
		try {
			//加载插件后，初始化插件
			ClassLoaderUtil.loadJar("C:\\eclipse-workspace\\zipkin-agent-main\\pre-agent\\plugin");
			
			
			ClassReplacer replacer = new ClassReplacer(className, classLoader, ctClass);
			for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
				String longName = ctMethod.getLongName();
				if ((Modifier.isPublic(ctMethod.getModifiers())) && (!Modifier.isStatic(ctMethod.getModifiers())
						&& (!Modifier.isNative(ctMethod.getModifiers()))) && isTarget(className, longName)) {
					ClassWrapper classWrapper = new ClassWrapperNull();
					classWrapper.beginSrc(beginSrc);
					classWrapper.endSrc(endSrc);
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
