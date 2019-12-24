package com.preapm.agent.plugin.interceptor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.filter.TracerDruidFilter;

/**
 * com.alibaba.druid.pool.DruidDataSource.DruidDataSource()
 * 
 * @author Zengmin.Zhang
 *
 */
public class DruidInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(DruidInterceptor.class);

	@Override
	public void before(MethodInfo methodInfo) {

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		DruidDataSource druidDataSource = (DruidDataSource) methodInfo.getTarget();
		if (druidDataSource != null) {
			List<Filter> proxyFilters = druidDataSource.getProxyFilters();
			proxyFilters.add(new TracerDruidFilter());
			druidDataSource.setProxyFilters(proxyFilters);
		}
		logger.info("com.preapm.agent.plugin.interceptor.DruidInterceptor after ok >>>>>>>>>>>>>>.");
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
}
