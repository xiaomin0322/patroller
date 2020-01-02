package com.preapm.agent.weave.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.preapm.agent.bean.PatternsYaml.PatternMethod;
import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.bean.PatternsYaml.Track;
import com.preapm.agent.bean.PluginConfigYaml.JarBean;
import com.preapm.agent.weave.ClassWrapper;

public class ClassWrapperAroundInterceptor extends ClassWrapper {

	private Set<JarBean> plugins;
	private Patterns patterns;
	private PatternMethod patternMethod;

	public ClassWrapperAroundInterceptor() {
	}

	public ClassWrapperAroundInterceptor(Set<JarBean> plugins, Patterns patterns, PatternMethod patternMethod) {
		this.plugins = plugins;
		this.patterns = patterns;
		this.patternMethod = patternMethod;
	}

	public ClassWrapperAroundInterceptor(Patterns patterns, PatternMethod patternMethod) {
		this.patterns = patterns;
		this.patternMethod = patternMethod;
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
			stringBuilder.append("String preMethodArgsStr = ").append(toStr(StringUtils.join(argNameList, ",")))
					.append(";").append(line());
			stringBuilder.append("preMethondInfo.setArgsName(preMethodArgsStr.split(" + toStr(",") + "));")
					.append(line());
		}
		setSerialize(stringBuilder);
		setPlugin(stringBuilder);
		setTrack(stringBuilder);

		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.start(Thread.currentThread().getContextClassLoader(),preMethondInfo);")
				.append(line());
		return stringBuilder.toString();
	}

	public static void addInterceptor(StringBuilder stringBuilder, String namePlugin) {
		if (namePlugin != null) {
			stringBuilder.append("if(!com.preapm.agent.common.context.AroundInterceptorContext.containsInterceptor("
					+ toStr(namePlugin) + ")){");
			stringBuilder.append("try {\r\n" + "				Class<?> classPlugin = Class.forName("
					+ toStr(namePlugin) + ");\r\n"
					+ "				com.preapm.agent.common.interceptor.AroundInterceptor newInstance = (com.preapm.agent.common.interceptor.AroundInterceptor) classPlugin.newInstance();\r\n"
					+ "				com.preapm.agent.common.context.AroundInterceptorContext.addInterceptor("
					+ toStr(namePlugin) + ", newInstance);\r\n" + "			} catch (Exception e) {\r\n"
					+ "				e.printStackTrace();\r\n" + "			}\r\n"
					+ "			System.out.println(\"初始化成功 name:\" + "+toStr(namePlugin)+");\r\n");
			stringBuilder.append("}");
		}

	}

	public static void main(String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		addInterceptor(stringBuilder, "aaa");
		System.out.println(stringBuilder.toString());
}

		

	public void setSerialize(StringBuilder stringBuilder) {
		String serialize = null;

		if (patterns.getTrack() != null) {
			serialize = patterns.getTrack().getSerialize();
		}

		if (patternMethod.getTrack() != null) {
			serialize = patternMethod.getTrack().getSerialize();
		}

		if (serialize != null) {
			stringBuilder.append("preMethondInfo.setSerialize(" + toStr(serialize) + ");").append(line());
		}
	}

	public String afterAgent(String resultName) {
		StringBuilder stringBuilder = new StringBuilder();

		if (com.preapm.agent.constant.BaseConstants.ONLY_AFTER.equals(resultName)) {
			stringBuilder.append(
					"com.preapm.agent.common.bean.MethodInfo preMethondInfo = new com.preapm.agent.common.bean.MethodInfo();")
					.append(line());
			stringBuilder.append("preMethondInfo.setTarget(this);").append(line());
			setPlugin(stringBuilder);
			setTrack(stringBuilder);
		} else {
			if (resultName != null) {
				stringBuilder.append("preMethondInfo.setResult(" + resultName + ");").append(line());
			}
		}
		stringBuilder.append("com.preapm.agent.common.context.AroundInterceptorContext.after(Thread.currentThread().getContextClassLoader(),preMethondInfo);")
				.append(line());
		return stringBuilder.toString();
	}

	public void setTrack(StringBuilder stringBuilder) {
		if (patterns != null) {
			Track track = patterns.getTrack();
			if (patternMethod.getTrack() != null) {
				track = patternMethod.getTrack();
			}
			if (track != null) {
				stringBuilder.append("preMethondInfo.setInParam(" + track.isInParam() + ");").append(line());
				stringBuilder.append("preMethondInfo.setOutParam(" + track.isOutParam() + ");").append(line());
				stringBuilder.append("preMethondInfo.setTime(" + track.getTime() + ");").append(line());
			}
		}
	}

	public void setPlugin(StringBuilder stringBuilder) {
		Set<String> pluginNameSet = new HashSet<>();
		if (patternMethod.getInterceptors() != null) {
			pluginNameSet.addAll(patternMethod.getInterceptors());
		} else {
			if (patterns.getInterceptors() != null && patterns.getInterceptors().size() != 0) {
				pluginNameSet.addAll(patterns.getInterceptors());
			}
		}
		if (!pluginNameSet.isEmpty()) {
			stringBuilder.append("String prePluginsStr = ").append(toStr(StringUtils.join(pluginNameSet, ",")))
					.append(";").append(line());
			stringBuilder.append("preMethondInfo.setPlugins(prePluginsStr.split(" + toStr(",") + "));").append(line());

			/*for (String p : pluginNameSet) {
				addInterceptor(stringBuilder, p);
			}*/

		}
	}

	@Override
	public String doError(String error) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("preMethondInfo.setThrowable(" + error + ");").append(line());
		String errorSrc = "com.preapm.agent.common.context.AroundInterceptorContext.exception(Thread.currentThread().getContextClassLoader(),preMethondInfo);";
		stringBuilder.append(errorSrc).append(line());
		return stringBuilder.toString();
	}

}