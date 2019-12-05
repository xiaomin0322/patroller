package com.dominos.cloud.agent;

import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;

import com.dominos.cloud.common.util.SpringBeanUtils;
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
		Tracer tracer = SpringBeanUtils.getBean(Tracer.class);
		if (tracer != null) {
			Span currentSpan = tracer.getCurrentSpan();
			Span newSpan = tracer.createSpan(method, currentSpan);
			newSpan.tag("time", String.valueOf(endTime - startTime));
			//System.out.println("加入span成功："+method);
		}
		//System.out.println("end :"+this.toString());
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