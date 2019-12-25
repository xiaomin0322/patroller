package com.preapm.sdk.zipkin.sampler;

import zipkin.Span;

public interface Sampler {
	/**
	 * @return true if the span is not null and should be exported to the tracing system
	 */
	boolean isSampled(Span span);
}
