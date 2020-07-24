package com.preapm.agent.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import com.preapm.agent.bean.PatternsYaml;
import com.preapm.agent.bean.PatternsYaml.PatternMethod;
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

	public static JarBean getJarBean(String key) {
		for (JarBean p : pluginConfigYaml.getPlugins().values()) {
			boolean matchesPattern = JdkRegexpMethodPointcut.macth(p.getLoadPatterns(), key);
			if (matchesPattern) {
				return p;
			}
		}
		return null;
	}

	public static PluginConfigYaml getPluginConfigYaml() {
		return pluginConfigYaml;
	}

	public static Set<String> getBasePlginName() {
		return pluginConfigYaml.getBasePlugins().keySet();
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
		List<String> patternsList = patterns.getIncludedPatternsKey();
		if (patternsList != null && patternsList.size() > 0) {
			matchesPattern = JdkRegexpMethodPointcut.macth(patternsList, method);
		}
		patternsList = patterns.getExcludedPatternsKey();
		if (patternsList != null && patternsList.size() > 0) {
			matchesPattern = !JdkRegexpMethodPointcut.macth(patternsList, method);
		}
		log.info("isTarget  :   calssName:" + className + " method:" + method + " matchesPattern:" + matchesPattern);
		return matchesPattern;
	}

	public static PatternMethod isTargetR(String className, String method) {
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return null;
		}
		boolean matchesPattern = false;
		List<String> patternsList = patterns.getIncludedPatternsKey();
		if (patternsList != null && patternsList.size() > 0) {
			matchesPattern = JdkRegexpMethodPointcut.macth(patternsList, method);
		}
		patternsList = patterns.getExcludedPatternsKey();
		if (patternsList != null && patternsList.size() > 0) {
			matchesPattern = !JdkRegexpMethodPointcut.macth(patternsList, method);
		}
		//log.info("isTarget  :   calssName:" + className + " method:" + method + " matchesPattern:" + matchesPattern);
		String key = JdkRegexpMethodPointcut.macthR(patterns.getIncludedPatternsKey(), method);
		if(patterns.getIncludedPatterns() == null) {
			return null;
		}
		for (PatternMethod m : patterns.getIncludedPatterns()) {
			if (m == null) {
				continue;
			}
			if (m.getKey().equals(key)) {
				log.info("isTarget  :   calssName:" + className + " method:" + method + " matchesPattern:"
						+ matchesPattern + " 匹配key");
				return m;
			}
		}
		return null;
	}

	public static Set<JarBean> getPlugins(String className) {
		Set<JarBean> jarBeansSet = new HashSet<>();
			for (JarBean p:pluginConfigYaml.getPlugins().values()) {
				List<String> list = new ArrayList<>();
				list.addAll(p.getLoadPatterns());
				boolean matchesPattern = JdkRegexpMethodPointcut.macth(list, className);
				if(matchesPattern) {
					jarBeansSet.add(p);
				}
			}
		Patterns patterns = PreConfigUtil.get(className);
		if (patterns == null) {
			return jarBeansSet;
		}
		List<String> interceptors = patterns.getInterceptors() == null ? new ArrayList<>() : patterns.getInterceptors();
		List<PatternMethod> includedPatterns = patterns.getIncludedPatterns();
		if (includedPatterns != null) {
			for (PatternMethod m : includedPatterns) {
				if (m.getInterceptors() != null) {
					interceptors.addAll(m.getInterceptors());
				}
			}
		}
		if (interceptors != null) {
			for (String s : interceptors) {
				JarBean jarBean = pluginConfigYaml.getInterceptorKeyPlugins().get(s);
				jarBeansSet.add(jarBean);
			}
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
