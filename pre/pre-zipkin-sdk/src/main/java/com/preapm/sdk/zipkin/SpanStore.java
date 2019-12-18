package com.preapm.sdk.zipkin;

import zipkin.Span;

/**
 * 
 * <pre>
 * SpanStore
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:17:57
 */
public interface SpanStore {
    public Span.Builder getSpan();

    public void setSpan(Span.Builder span);

    public void removeSpan();
}
