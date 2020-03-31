package com.preapm.sdk.zipkin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.sdk.zipkin.collector.HttpSpanCollector;
import com.preapm.sdk.zipkin.collector.SpanCollector;
import com.preapm.sdk.zipkin.util.GenerateKey;

import zipkin.Annotation;
import zipkin.BinaryAnnotation;
import zipkin.Endpoint;
import zipkin.Span;

/**
 * 
 * <pre>
 * ZipkinClient
 * </pre>
 * 
 * @author
 *
 * @since 2018年1月25日 下午4:18:06
 */
public class ZipkinClient {
	private static final Logger logger = LoggerFactory.getLogger(ZipkinClient.class);
	private SpanCollector spanCollector;
	private SpanStore spanStore;

	public ZipkinClient(String zipkinHost) {
		this.spanCollector = new HttpSpanCollector(zipkinHost);
		this.spanStore = new ThreadLocalSpanStore();
	}

	public SpanStore getSpanStore() {
		return this.spanStore;
	}

	public Span startSpan(Long id, Long traceId, Long parentId, String name) {
		try {
			Span.Builder builder = Span.builder().id(id).traceId(traceId).parentId(parentId).name(name)
					.timestamp(nanoTime());
			this.spanStore.setSpan(builder);
			sendBinaryAnnotation("threadName", Thread.currentThread().getName());
			return builder.build();
		} catch (Exception e) {
			logger.error("startSpanException", e);
			return Span.builder().id(id).traceId(traceId).timestamp(nanoTime()).build();
		}

	}

	public Span startSpan(Long traceId, Long parentId, String name) {
		Long id = GenerateKey.longKey();
		return startSpan(id, traceId, parentId, name);

	}

	public Span startRootSpan(String name) {
		Long id = GenerateKey.longKey();
		return startSpan(id, id, name);
	}

	public Span startSpan(String name) {
		long spanId = GenerateKey.longKey();
		long traceId = spanId;
		Long parentId = null;
		Span.Builder parentSpan = this.spanStore.getSpan();
		if (parentSpan != null) {
			Span parent = parentSpan.build();
			traceId = parent.traceId;
			parentId = parent.id;
		}
		return startSpan(spanId, traceId, parentId, name);

	}

	public Span getSpan() {
		Span.Builder parentSpan = this.spanStore.getSpan();
		if (parentSpan != null) {
			Span span = parentSpan.build();
			return span;
		}
		return null;
	}

	public void sendAnnotation(String value, Endpoint endpoint) {
		try {
			Span.Builder span = this.spanStore.getSpan();
			if(span == null) {
				logger.warn("span is null");
				return;
			}
			span.addAnnotation(Annotation.create(nanoTime(), value, endpoint));
		} catch (Exception e) {
			logger.error("sendAnnotationException", e);
		}
	}

	public void sendAnnotation(String value) {
		sendAnnotation(value, null);
	}

	public void sendBinaryAnnotation(String key, String value, Endpoint endpoint) {
		try {
			Span.Builder span = this.spanStore.getSpan();
			if(span == null || key == null || value == null) {
				return;
			}
			span.addBinaryAnnotation(BinaryAnnotation.create(key, value, endpoint));
		} catch (Exception e) {
			logger.error("sendBinaryAnnotationException", e);
		}

	}

	public void sendBinaryAnnotation(List<BinaryAnnotation> binaryAnnotations) {
		if (binaryAnnotations == null) {
			return;
		}
		try {
			Span.Builder span = this.spanStore.getSpan();
			for (BinaryAnnotation a : binaryAnnotations) {
				if (a == null) {
					continue;
				}
				span.addBinaryAnnotation(a);
			}
		} catch (Exception e) {
			logger.error("sendBinaryAnnotationException", e);
		}

	}

	public void sendBinaryAnnotation(String key, String value) {
		sendBinaryAnnotation(key, value, null);
	}

	public void finishSpan() {
		try {
			Span.Builder span = this.spanStore.getSpan();
			if (span != null) {
				long duration = nanoTime() - span.build().timestamp;
				this.spanCollector.collect(span.duration(duration).build());
			} else {
				logger.warn("you must use startSpan before finishSpan");
			}
			this.spanStore.removeSpan();
		} catch (Exception e) {
			logger.error("finishSpanException", e);
		}
	}

	public static Long nanoTime() {
		return System.currentTimeMillis() * 1000;
	}

}
