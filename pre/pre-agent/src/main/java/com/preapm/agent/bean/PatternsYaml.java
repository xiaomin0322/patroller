package com.preapm.agent.bean;

import java.util.List;
import java.util.Map;

public class PatternsYaml {

    private Map<String,Patterns> patterns;

        public static class Patterns {

        private List<String> patterns;
        private List<String> excludedPatterns;
        private List<String> includedPatterns;
        private List<String> plugins;


        public List<String> getPlugins() {
			return plugins;
		}

		public void setPlugins(List<String> plugins) {
			this.plugins = plugins;
		}

		public List<String> getIncludedPatterns() {
        return includedPatterns;
        }

        public void setIncludedPatterns(List<String> includedPatterns) {
            this.includedPatterns = includedPatterns;
        }

        public List<String> getPatterns() {
            return patterns;
        }

        public void setPatterns(List<String> patterns) {
            this.patterns = patterns;
        }

        public List<String> getExcludedPatterns() {
            return excludedPatterns;
        }

        public void setExcludedPatterns(List<String> excludedPatterns) {
            this.excludedPatterns = excludedPatterns;
        }
    }

		public Map<String, Patterns> getPatterns() {
			return patterns;
		}

		public void setPatterns(Map<String, Patterns> patterns) {
			this.patterns = patterns;
		}
        
        
        
}
