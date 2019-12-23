package com.preapm.agent.common.bean;

import java.util.Arrays;

public class MethodInfo {

	private String methodName;

	private Object target;
	private String[] argsName;

	private Object[] args;

	private Object result;

	private Throwable throwable;
	
	private Object[] localVariable;
	
	private String[] plugins;
	
	public String[] getPlugins() {
		return plugins;
	}

	public void setPlugins(String[] plugins) {
		this.plugins = plugins;
	}

	public String getClassName() {
		return target.getClass().getName();
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public String[] getArgsName() {
		return argsName;
	}

	public void setArgsName(String[] argsName) {
		this.argsName = argsName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public Object[] getLocalVariable() {
		return localVariable;
	}

	public void setLocalVariable(Object[] localVariable) {
		this.localVariable = localVariable;
	}

	@Override
	public String toString() {
		return "MethodInfo [methodName=" + methodName + ", target=" + target + ", argsName=" + Arrays.toString(argsName)
				+ ", args=" + Arrays.toString(args) + ", result=" + result + ", throwable=" + throwable
				+ ", localVariable=" + Arrays.toString(localVariable) + ", plugins=" + Arrays.toString(plugins) + "]";
	}

}
