package com.preapm.agent.weave.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.preapm.agent.weave.ClassWrapper;

public class ClassWrapperAroundInterceptor extends ClassWrapper {

	public String beforAgent(String methodName, List<String> argNameList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"com.preapm.agent.common.bean.MethodInfo preMethondInfo = new com.preapm.agent.common.bean.MethodInfo();");
		stringBuilder.append("preMethondInfo.setTarget(this);");
		stringBuilder.append("preMethondInfo.setArgs($args);");
		stringBuilder.append("preMethondInfo.setMethodName(" + toStr(methodName) + ")");
		if (argNameList != null && argNameList.size() != 0) {
			stringBuilder.append("String preMethodArgsStr = ").append(StringUtils.join(argNameList));
			stringBuilder.append("preMethondInfo.setArgsName(preMethodArgsStr.split(\",\"))");
		}
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.start(preMethondInfo);");
		return stringBuilder.toString();
	}

	public String afterAgent(String resultName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("preMethondInfo.setResult(" + resultName + ");");
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.after(preMethondInfo);");
		return stringBuilder.toString();
	}

	@Override
	public String doError(String error) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("preMethondInfo.setThrowable(" + error + ");");
		String errorSrc = "com.preapm.agent.common.context.AroundInterceptorContext.exception(preMethondInfo);";
		return errorSrc;
	}

}