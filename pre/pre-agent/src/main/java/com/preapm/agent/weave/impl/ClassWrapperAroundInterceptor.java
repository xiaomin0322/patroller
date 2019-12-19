package com.preapm.agent.weave.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.preapm.agent.bean.PluginJarBean;
import com.preapm.agent.weave.ClassWrapper;

public class ClassWrapperAroundInterceptor extends ClassWrapper {
	
	private Set<PluginJarBean> plugins;
	
	public ClassWrapperAroundInterceptor() {}
	
	public ClassWrapperAroundInterceptor(Set<PluginJarBean> plugins) {
		this.plugins = plugins;
	}
	

	public String beforAgent(String methodName, List<String> argNameList) {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(
				"com.preapm.agent.common.bean.MethodInfo preMethondInfo = new com.preapm.agent.common.bean.MethodInfo();")
				.append(line());
		stringBuilder.append("preMethondInfo.setTarget(this);").append(line());
		stringBuilder.append("preMethondInfo.setMethodName(" + toStr(methodName) + ");").append(line());
		if (argNameList != null && argNameList.size() != 0) {
			stringBuilder.append("preMethondInfo.setArgs($args);").append(line());
			stringBuilder.append("String preMethodArgsStr = ").append(toStr(StringUtils.join(argNameList,","))).append(";")
					.append(line());
			stringBuilder.append("preMethondInfo.setArgsName(preMethodArgsStr.split(" + toStr(",") + "));")
					.append(line());
		}
		if (plugins != null && plugins.size() != 0) {
			Set<String> pluginNameSet = new HashSet<>();
			for(PluginJarBean p :plugins) {
				pluginNameSet.add(p.getName());
			}
			stringBuilder.append("String prePluginsStr = ").append(toStr(StringUtils.join(pluginNameSet,","))).append(";")
					.append(line());
			stringBuilder.append("preMethondInfo.setPlugins(prePluginsStr.split(" + toStr(",") + "));")
					.append(line());
		}
		
		
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.start(preMethondInfo);")
				.append(line());
		return stringBuilder.toString();
	}

	public String afterAgent(String resultName) {
		StringBuilder stringBuilder = new StringBuilder();
		if(resultName!=null) {
			stringBuilder.append("preMethondInfo.setResult(" + resultName + ");").append(line());
		}
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