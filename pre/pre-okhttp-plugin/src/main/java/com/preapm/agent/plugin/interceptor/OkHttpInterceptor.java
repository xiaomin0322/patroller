package com.preapm.agent.plugin.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.filter.OkHttpFilter;

/**
 * okhttp3.OkHttpClient.Builder.Builder()
 * 
 * @author Zengmin.Zhang
 *
 */
public class OkHttpInterceptor implements AroundInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(OkHttpInterceptor.class);

	private static final OkHttpFilter filter = new OkHttpFilter();

	@Override
	public void before(MethodInfo methodInfo) {
		okhttp3.OkHttpClient.Builder builder = (okhttp3.OkHttpClient.Builder) methodInfo.getTarget();
		logger.info("okhttp3.OkHttpClient.Builder before >>>>>>>>>>>>>>>>>>>>>>>"+builder);
	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		okhttp3.OkHttpClient.Builder builder = (okhttp3.OkHttpClient.Builder) methodInfo.getTarget();
		logger.info("okhttp3.OkHttpClient.Builder  >>>>>>>>>>>>>>>>>>>>>>>"+builder);
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>okhttp3.OkHttpClient.Builder  =========================="+builder);
		if (builder != null) {
			builder.addInterceptor(filter);
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
	
}
