package com.preapm.agent.plugin.bean;

public class Statistics {

	private String method;
	private long startTime;
	private long endTime;
	private Throwable error;

	public Statistics(String method) {
		this.method = method;
		this.startTime = System.currentTimeMillis();
	}

	

	public void end() {
		endTime = System.currentTimeMillis();
	}

	public void error(Throwable e) {
		error = e;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Statistics{");
		sb.append("method='").append(method).append('\'');
		sb.append(", startTime=").append(startTime);
		sb.append(", endTime=").append(endTime);
		sb.append(", error=").append(error);
		sb.append('}');
		return sb.toString();
	}
}