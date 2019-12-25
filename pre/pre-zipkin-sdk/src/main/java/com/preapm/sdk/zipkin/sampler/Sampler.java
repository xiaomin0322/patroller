package com.preapm.sdk.zipkin.sampler;

public interface Sampler {
	/**
	 * @return true if the span is not null and should be exported to the tracing system
	 */
	boolean isSampled(Object span);
}
