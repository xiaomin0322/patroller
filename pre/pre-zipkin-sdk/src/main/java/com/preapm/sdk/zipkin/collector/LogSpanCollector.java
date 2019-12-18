package com.preapm.sdk.zipkin.collector;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import zipkin.Span;

/**
 * 
 * <pre>
 * LogSpanCollector
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:22
 */
public class LogSpanCollector extends AbstractSpanCollector {
    private static final Logger logger = LoggerFactory.getLogger(LogSpanCollector.class);

    @Override
    public void sendSpans(byte[] json) throws IOException {
        List<Span> spans = super.bytesToList(json);
        for (Span span : spans) {
            logger.info("{}", span);
        }
    }

}
