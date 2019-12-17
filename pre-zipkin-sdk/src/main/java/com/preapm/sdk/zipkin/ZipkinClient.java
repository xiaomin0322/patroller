package com.preapm.sdk.zipkin;

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

    public Span startSpan(Long id, Long traceId, Long parentId, String name) {
        if (id != null && traceId != null && parentId != null) {
            Span.Builder builder = Span.builder().id(id).traceId(traceId).parentId(parentId).name(name)
                    .timestamp(nanoTime());
            this.spanStore.setSpan(builder);
            return builder.build();
        } else {
            return this.startSpan(name);
        }

    }

    public Span startSpan(String name) {
        long id = GenerateKey.longKey();
        try {
            Span.Builder parentSpan = this.spanStore.getSpan();
            Span.Builder builder = Span.builder().id(id).traceId(id).name(name).timestamp(nanoTime());
            if (parentSpan != null) {
                Span span = parentSpan.build();
                builder.traceId(span.traceId).parentId(span.id);
            }
            this.spanStore.setSpan(builder);
            return builder.build();
        } catch (Exception e) {
            e.printStackTrace();
            return Span.builder().id(id).traceId(id).timestamp(nanoTime()).build();
        }
    }

    public void sendAnnotation(String value, Endpoint endpoint) {
        try {
            Span.Builder span = this.spanStore.getSpan();
            span.addAnnotation(Annotation.create(nanoTime(), value, endpoint));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendBinaryAnnotation(String key, String value, Endpoint endpoint) {
        try {
            Span.Builder span = this.spanStore.getSpan();
            span.addBinaryAnnotation(BinaryAnnotation.create(key, value, endpoint));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void finishSpan() {
        try {
            Span.Builder span = this.spanStore.getSpan();
            if (span != null) {
                long duration = nanoTime() - span.build().timestamp;
                this.spanCollector.collect(span.duration(duration).build());
            } else {
                logger.error("you must use startSpan before finishSpan");
            }
            this.spanStore.removeSpan();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Long nanoTime() {
        return System.currentTimeMillis() * 1000;
    }

}
