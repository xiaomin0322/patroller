package com.preapm.agent.bean;

import java.util.Set;

public class TargetBean {

	private String className;

	private Set<String> methodName;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Set<String> getMethodName() {
		return methodName;
	}

	public void setMethodName(Set<String> methodName) {
		this.methodName = methodName;
	}

}
