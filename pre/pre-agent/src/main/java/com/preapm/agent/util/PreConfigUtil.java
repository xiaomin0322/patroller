package com.preapm.agent.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.preapm.agent.bean.PatternsYaml;
import com.preapm.agent.bean.PatternsYaml.Patterns;
import com.preapm.agent.bean.PluginConfigYaml;
import com.preapm.agent.bean.PluginConfigYaml.JarBean;
import com.preapm.agent.weave.pattern.JdkRegexpMethodPointcut;

public class PreConfigUtil {

	private static Logger log = LogManager.getLogger(PreConfigUtil.class);

	private static PatternsYaml patternsYaml;
	private static PluginConfigYaml pluginConfigYaml;

	static {
		init();
	}

	public static Patterns get(String key) {
		for (Patterns p : patternsYaml.getPatterns().values()) {
			boolean matchesPattern = JdkRegexpMethodPointcut.macth(p.getPatterns(), key);
			if (matchesPattern) {
				return p;
			}
		}
		return null;
	}

	private static void init() {
		try {
			pluginConfigYaml = (PluginConfigYaml) YMLMappingUtil.reader(PluginConfigYaml.class);
			patternsYaml = (PatternsYaml) YMLMappingUtil.reader(PatternsYaml.class);
		} catch (Exception e) {
			log.severe("PreConfigUtil init error !" + e.getMessage());
		}
	}

	public static boolean isTarget(String className, String method) {
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return false;
		}
		boolean matchesPattern = false;
		List<String> patternsList = patterns.getIncludedPatterns();
		if (patternsList != null) {
			matchesPattern = JdkRegexpMethodPointcut.macth(patternsList, method);
		}
		patternsList = patterns.getExcludedPatterns();
		if (patternsList != null) {
			matchesPattern = !JdkRegexpMethodPointcut.macth(patternsList, method);
		}
		return matchesPattern;
	}

	public static Set<JarBean> getPlugins(String className) {
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return null;
		}
		Set<JarBean> jarBeansSet = new HashSet<>();
		List<String> plugins = patterns.getPlugins();
		for (String s : plugins) {
			JarBean jarBean = pluginConfigYaml.getPlugins().get(s);
			jarBeansSet.add(jarBean);
		}
		return jarBeansSet;
	}

	public static boolean isTarget(String className) {
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return false;
		}
		return true;
	}

}
