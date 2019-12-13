package com.preapm.agent.weave.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.preapm.agent.weave.ClassWrapper;

public class ClassWrapperAroundInterceptor extends ClassWrapper {

	public String beforAgent(String methodName, List<String> argNameList) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"com.preapm.agent.common.bean.MethodInfo preMethondInfo = new com.preapm.agent.common.bean.MethodInfo();")
				.append(line());
		stringBuilder.append("preMethondInfo.setTarget(this);").append(line());
		stringBuilder.append("preMethondInfo.setArgs($args);").append(line());
		stringBuilder.append("preMethondInfo.setMethodName(" + toStr(methodName) + ");").append(line());
		if (argNameList != null && argNameList.size() != 0) {
			stringBuilder.append("String preMethodArgsStr = ").append(toStr(StringUtils.join(argNameList,","))).append(";")
					.append(line());
			stringBuilder.append("preMethondInfo.setArgsName(preMethodArgsStr.split(" + toStr(",") + "));")
					.append(line());
		}
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.start(preMethondInfo);")
				.append(line());
		return stringBuilder.toString();
	}

	public String afterAgent(String resultName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("preMethondInfo.setResult(" + resultName + ");").append(line());
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.after(preMethondInfo);")
				.append(line());
		return stringBuilder.toString();
	}

	@Override
	public String doError(String error) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("preMethondInfo.setThrowable(" + error + ");").append(line());
		String errorSrc = "com.preapm.agent.common.context.AroundInterceptorContext.exception(preMethondInfo);";
		stringBuilder.append(errorSrc).append(line());
		return stringBuilder.toString();
	}

}