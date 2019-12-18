package com.preapm.agent.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

public class ReflectUtil {

	private static final Map<String, List<String>> PARAM_MAP = new HashMap<String, List<String>>();

	/**
	 * 拿到方法的参数名称 比如get(long id, String name) 获得id, name
	 */
	public static List<String> getParamNameList(CtMethod cm) {
		if (cm == null) {
			return null;
		}
		System.out.println("methodName="+cm.getLongName());
		String methodName = cm.getName();
		List<String> paramNameList = PARAM_MAP.get(methodName);
		if (paramNameList == null || paramNameList.size() <= 0) {
			if (paramNameList == null || paramNameList.size() <= 0) {
				try {
					paramNameList = new ArrayList<String>();
					// 使用javaassist的反射方法获取方法的参数名
					MethodInfo methodInfo = cm.getMethodInfo();
					CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
					LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
							.getAttribute(LocalVariableAttribute.tag);
					if (attr != null) {
						int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
						for (int i = 0; i < cm.getParameterTypes().length; i++) {
							paramNameList.add(attr.variableName(i + pos));
						}
					}
				} catch (NotFoundException e) {
					System.out.println("methodName="+cm.getLongName());
					e.printStackTrace();
					
				}

				PARAM_MAP.put(methodName, paramNameList);
			}
		}
		return paramNameList;
	}

}