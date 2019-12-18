package com.preapm.agent.bean;

import java.util.Set;

public class PluginConfigBean {

	private String name;
	private String loadPatterns;
	private String patterns;
	private Set<String> containPatterns;
	private Set<String> excludedPatterns;
	private Set<PluginJarBean>  plugins;

	public Set<PluginJarBean> getPlugins() {
		return plugins;
	}

	public void setPlugins(Set<PluginJarBean> plugins) {
		this.plugins = plugins;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoadPatterns() {
		return loadPatterns;
	}

	public void setLoadPatterns(String loadPatterns) {
		this.loadPatterns = loadPatterns;
	}

	public String getPatterns() {
		return patterns;
	}

	public void setPatterns(String patterns) {
		this.patterns = patterns;
	}

	public Set<String> getContainPatterns() {
		return containPatterns;
	}

	public void setContainPatterns(Set<String> containPatterns) {
		this.containPatterns = containPatterns;
	}

	public Set<String> getExcludedPatterns() {
		return excludedPatterns;
	}

	public void setExcludedPatterns(Set<String> excludedPatterns) {
		this.excludedPatterns = excludedPatterns;
	}


}
