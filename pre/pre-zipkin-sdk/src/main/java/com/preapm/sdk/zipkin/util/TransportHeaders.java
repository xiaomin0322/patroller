package com.preapm.sdk.zipkin.util;

import lombok.Getter;

/**
 * 
 * <pre>
 * TransportHeaders
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:57
 */
public enum TransportHeaders {

    TraceId("Yiwugou-Zipkin-TraceId"),

    SpanId("Yiwugou-Zipkin-SpanId"),

    ParentSpanId("Yiwugou-Zipkin-ParentSpanId"),

    Sampled("Yiwugou-Zipkin-Sampled");

    @Getter
    private final String name;

    private TransportHeaders(final String name) {
        this.name = name;
    }
}
