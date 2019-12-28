package com.preapm.sdk.zipkin;

import java.util.Stack;

import com.alibaba.ttl.TransmittableThreadLocal;

import zipkin.Span;

/**
 * 
 * <pre>
 * ThreadLocalSpanStore,LazyTraceExecutor
 * </pre>
 * 
 * @author
 *
 * @since 2018年1月25日 下午4:18:02
 */
public class DoubleThreadLocalSpanStore implements SpanStore {// InheritableThreadLocal

	public static ThreadLocal<Stack<Span.Builder>> LOCAL_SPAN = new ThreadLocal<Stack<Span.Builder>>() {
		@Override
		protected Stack<Span.Builder> initialValue() {
			return new Stack<>();
		}
	};

	public static ThreadLocal<Stack<Span.Builder>> LOCAL_SPAN_TRANSMITTABLE = new TransmittableThreadLocal<Stack<Span.Builder>>() {
		@Override
		protected Stack<Span.Builder> initialValue() {
			return new Stack<>();
		}
	};

	@Override
	public Span.Builder getSpan() {
		if (isTransmittableThread()) {
			return LOCAL_SPAN_TRANSMITTABLE.get().empty() ? null : LOCAL_SPAN_TRANSMITTABLE.get().peek();
		}
		return LOCAL_SPAN.get().empty() ? null : LOCAL_SPAN.get().peek();
	}

	@Override
	public void setSpan(Span.Builder span) {
		if (span == null) {
			if (!LOCAL_SPAN.get().empty()) {
				LOCAL_SPAN.get().pop();
			}
		} else {
			LOCAL_SPAN.get().push(span);
		}

		if (span == null) {
			if (!LOCAL_SPAN_TRANSMITTABLE.get().empty()) {
				LOCAL_SPAN_TRANSMITTABLE.get().pop();
			}
		} else {
			LOCAL_SPAN_TRANSMITTABLE.get().push(span);
		}
	}

	@Override
	public void removeSpan() {
		if (!LOCAL_SPAN.get().empty()) {
			LOCAL_SPAN.get().pop();
		}
		if (!LOCAL_SPAN_TRANSMITTABLE.get().empty()) {
			LOCAL_SPAN_TRANSMITTABLE.get().pop();
		}
	}

	public boolean isTransmittableThread() {
		String name = Thread.currentThread().getName();
		if (!name.startsWith("http")) {
			return true;
		}
		return false;
	}
}
