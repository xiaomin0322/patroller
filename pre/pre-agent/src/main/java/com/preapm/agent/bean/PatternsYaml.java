package com.preapm.agent.bean;

import java.util.List;
import java.util.Map;

public class PatternsYaml {

	private Map<String, Patterns> patterns;

	public static class Track {
		// 是否记录入参
		private boolean inParam;
		// 是否记录返回参数
		private boolean outParam;
		// 超过这个时间才记录
		private int time=-1;

		public boolean isInParam() {
			return inParam;
		}

		public void setInParam(boolean inParam) {
			this.inParam = inParam;
		}

		public boolean isOutParam() {
			return outParam;
		}

		public void setOutParam(boolean outParam) {
			this.outParam = outParam;
		}

		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}
	}

	public static class Patterns {

		private Track track;
		private List<String> patterns;
		private List<String> excludedPatterns;
		private List<String> includedPatterns;
		private List<String> plugins;

		public Track getTrack() {
			return track;
		}

		public void setTrack(Track track) {
			this.track = track;
		}

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
