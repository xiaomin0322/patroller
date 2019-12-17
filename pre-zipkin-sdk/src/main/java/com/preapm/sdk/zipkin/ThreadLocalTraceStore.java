package com.preapm.sdk.zipkin;

import com.preapm.sdk.zipkin.util.GenerateKey;

/**
 * 
 * <pre>
 * ThreadLocalSpanStore
 * </pre>
 * 
 * @author
 *
 * @since 2018年1月25日 下午4:18:02
 */
public class ThreadLocalTraceStore {

	public static ThreadLocal<Long> LOCAL_TRACE = new ThreadLocal<Long>() {
		@Override
		protected Long initialValue() {
			return GenerateKey.longKey();
		}
	};

	public static Long get() {
		return LOCAL_TRACE.get();
	}

	public static void remove() {
		LOCAL_TRACE.remove();
	}
}
