package com.preapm.sdk.zipkin.collector;

import zipkin.Span;

/**
 * 
 * <pre>
 * SpanCollector
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:26
 */
public interface SpanCollector {
    public void collect(final Span span);
}
