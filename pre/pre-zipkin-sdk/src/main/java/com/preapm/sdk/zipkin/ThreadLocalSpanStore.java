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
 * span跨线程传递数据的时候，需要获取父线程的数据，拷贝到子线程中。
 * JDK自带的InheritableThreadLocal仅能解决每次手动创建线程的场景，如果使用线程池，它将无能为力。
 * 阿里开源的TTL仅能解决线程池的场景，手动创建线程无法解决。
 * 因此，采用自定义thread plugin的方式传递数据
 * @author 
 *
 * @since 2018年1月25日 下午4:18:02
 */
public class ThreadLocalSpanStore implements SpanStore {//InheritableThreadLocal

    public static ThreadLocal<Stack<Span.Builder>> LOCAL_SPAN = new ThreadLocal<Stack<Span.Builder>>() {
        @Override
        protected Stack<Span.Builder> initialValue() {
            return new Stack<>();
        }
    };
	
	 /* public static ThreadLocal<Stack<Span.Builder>> LOCAL_SPAN = new TransmittableThreadLocal<Stack<Span.Builder>>() {
	        @Override
	        protected Stack<Span.Builder> initialValue() {
	            return new Stack<>();
	        }
	    };
*/
    @Override
    public Span.Builder getSpan() {
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
    }

    @Override
    public void removeSpan() {
        if (!LOCAL_SPAN.get().empty()) {
            LOCAL_SPAN.get().pop();
        }
    }

	@Override
	public void removeAllSpan() {
		LOCAL_SPAN.remove();
	}
}
