package com.preapm.sdk.zipkin;

/**
 * 
 * <pre>
 * SpanMetrics
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:17:53
 */
public interface SpanMetrics {
    void incrementAcceptedSpans(int quantity);

    void incrementDroppedSpans(int quantity);
}
