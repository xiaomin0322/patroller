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

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		okhttp3.OkHttpClient.Builder builder = (okhttp3.OkHttpClient.Builder) methodInfo.getTarget();
		if (builder != null) {
			builder.addInterceptor(filter);
			logger.info("add filter to OkHttpFilter>>>>>>>>>>>>>>>>>>>>>>>");
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
	
	public static void main(String[] args) {
		System.out.println(okhttp3.OkHttpClient.Builder.class.getName());
	}

}
