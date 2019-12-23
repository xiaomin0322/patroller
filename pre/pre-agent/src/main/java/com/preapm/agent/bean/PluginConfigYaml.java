package com.preapm.agent.bean;

import java.util.List;
import java.util.Map;

public class PluginConfigYaml {
    private Map<String,JarBean> plugins;

    private Map<String,JarBean> basePlugins;

    public static class JarBean {

        private String jarName;
        private List<String> interceptorNames;
        private List<String> loadPatterns;

        public List<String> getLoadPatterns() {
            return loadPatterns;
        }

        public void setLoadPatterns(List<String> loadPatterns) {
            this.loadPatterns = loadPatterns;
        }

        public List<String> getInterceptorNames() {
            return interceptorNames;
        }

        public void setInterceptorNames(List<String> interceptorNames) {
            this.interceptorNames = interceptorNames;
        }

        public String getJarName() {
            return jarName;
        }

        public void setJarName(String jarName) {
            this.jarName = jarName;
        }
    }

    public Map<String, JarBean> getPlugins() {
        return plugins;
    }

    public void setPlugins(Map<String, JarBean> plugins) {
        this.plugins = plugins;
    }

	public Map<String, JarBean> getBasePlugins() {
		return basePlugins;
	}

	public void setBasePlugins(Map<String, JarBean> basePlugins) {
		this.basePlugins = basePlugins;
	}
    
    

    //    public static class Plugins {
//        private Map<String,JarBean> jars;
//
//        private List<String> loadPatterns;
//
//        private HashMap<String, Patterns> appCollector;
//
//
//        public List<String> getLoadPatterns() {
//            return loadPatterns;
//        }
//
//        public void setLoadPatterns(List<String> loadPatterns) {
//            this.loadPatterns = loadPatterns;
//        }
//
//        public HashMap<String, Patterns> getAppCollector() {
//            return appCollector;
//        }
//
//        public void setAppCollector(HashMap<String, Patterns> appCollector) {
//            this.appCollector = appCollector;
//        }
//    }
//
//
//
//    public static class Patterns {
//
//        private List<String> patterns;
//        private List<String> excludedPatterns;
//        private List<String> includedPatterns;
//
//        public List<String> getIncludedPatterns() {
//            return includedPatterns;
//        }
//
//        public void setIncludedPatterns(List<String> includedPatterns) {
//            this.includedPatterns = includedPatterns;
//        }
//
//        public List<String> getPatterns() {
//            return patterns;
//        }
//
//        public void setPatterns(List<String> patterns) {
//            this.patterns = patterns;
//        }
//
//        public List<String> getExcludedPatterns() {
//            return excludedPatterns;
//        }
//
//        public void setExcludedPatterns(List<String> excludedPatterns) {
//            this.excludedPatterns = excludedPatterns;
//        }
//    }
//
//    public PluginConfigYaml.Plugins getPlugins() {
//        return plugins;
//    }
//
//    public void setPlugins(PluginConfigYaml.Plugins plugins) {
//        this.plugins = plugins;
//    }
}
