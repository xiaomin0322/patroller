package com.preapm.agent.test;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class VLInterface {

	public static void main(String[] args) throws NotFoundException {
		String className = "com.dominos.cloud.agent.util.ReflectUtil";
		String methodName = "getParamNameList";
		String[] methodVariableName = getMethodParams(className, methodName);
		for (String s : methodVariableName) {
			System.out.println(s);
		}
	}

	/***
	 * 通过类名 方法名获取方法中的参数名
	 *
	 * @param classname  类的完整路径
	 * @param methodname 方法名
	 * @return
	 */
	public static String[] getMethodParams(String classname, String methodname) {
		try {
			ClassPool pool = ClassPool.getDefault();
			CtClass cc = pool.get(classname);
			CtMethod cm = cc.getDeclaredMethod(methodname);
			MethodInfo methodInfo = cm.getMethodInfo();
			CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
			String[] paramNames = new String[cm.getParameterTypes().length];
			LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
					.getAttribute(LocalVariableAttribute.tag);
			if (attr != null) {
				int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
				for (int i = 0; i < paramNames.length; i++) {
					paramNames[i] = attr.variableName(i + pos);
				}
				return paramNames;
			}
		} catch (Exception e) {
			System.out.println("getMethodVariableName fail " + e);
		}
		return null;

	}
}