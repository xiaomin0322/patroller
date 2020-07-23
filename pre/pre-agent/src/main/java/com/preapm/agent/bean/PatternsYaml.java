package com.preapm.agent.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatternsYaml {

	private Map<String, Patterns> patterns;

	public static class PatternMethod {
		private String key;
		private List<String> interceptors;
		private Track track;

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public List<String> getInterceptors() {
			return interceptors;
		}

		public void setInterceptors(List<String> interceptors) {
			this.interceptors = interceptors;
		}

		public Track getTrack() {
			return track;
		}

		public void setTrack(Track track) {
			this.track = track;
		}

	}

	public static class Track {
		// 是否记录入参
		private boolean inParam;
		// 是否记录返回参数
		private boolean outParam;
		// 超过这个时间才记录
		private int time = -1;
		//序列化方式
		private String serialize="def";
		
		public String getSerialize() {
			return serialize;
		}

		public void setSerialize(String serialize) {
			this.serialize = serialize;
		}

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
		private List<PatternMethod> excludedPatterns;
		private List<PatternMethod> includedPatterns;
		private List<String> interceptors;
		private String superClass;
		
		public String getSuperClass() {
			return superClass;
		}

		public void setSuperClass(String superClass) {
			this.superClass = superClass;
		}

		public Track getTrack() {
			return track;
		}

		public void setTrack(Track track) {
			this.track = track;
		}

		public List<String> getInterceptors() {
			return interceptors;
		}

		public void setInterceptors(List<String> interceptors) {
			this.interceptors = interceptors;
		}

		public List<String> getPatterns() {
			return patterns;
		}

		public void setPatterns(List<String> patterns) {
			this.patterns = patterns;
		}

		public List<PatternMethod> getExcludedPatterns() {
			return excludedPatterns;
		}

		public void setExcludedPatterns(List<PatternMethod> excludedPatterns) {
			this.excludedPatterns = excludedPatterns;
		}

		public List<PatternMethod> getIncludedPatterns() {
			return includedPatterns;
		}

		public void setIncludedPatterns(List<PatternMethod> includedPatterns) {
			this.includedPatterns = includedPatterns;
		}

		public List<String> getIncludedPatternsKey() {
			List<String> list = new ArrayList<>();
			if (includedPatterns != null) {
				for (PatternMethod p : includedPatterns) {
					list.add(p.getKey());
				}
			}
			return list;
		}

		public List<String> getExcludedPatternsKey() {
			List<String> list = new ArrayList<>();
			if (excludedPatterns != null) {
				for (PatternMethod p : excludedPatterns) {
					list.add(p.getKey());
				}
			}
			return list;
		}

	}

	public Map<String, Patterns> getPatterns() {
		return patterns;
	}

	public void setPatterns(Map<String, Patterns> patterns) {
		this.patterns = patterns;
	}

}
