package com.preapm.agent.plugin.interceptor;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.filter.PreLog4J2Filter;

/**
 * 
 * org.apache.logging.log4j.core.LoggerContext.setConfiguration(Configuration)
 * org.apache.logging.log4j.core.config.AbstractConfiguration.getPluginPackages()
 * 
 * @author Zengmin.Zhang
 *
 */
public class Log4j2Interceptor implements AroundInterceptor {

	public static String PACKAGENAME = "com.preapm.agent.plugin.interceptor.filter";

	@Override
	public void before(MethodInfo methodInfo) {
		Object[] args = methodInfo.getArgs();
		if (args != null) {
			Configuration configuration = (Configuration) args[0];
			if (configuration != null) {
				/*
				 * try { Field pluginPackagesField = configuration.getClass().getSuperclass()
				 * .getDeclaredField("pluginPackages"); pluginPackagesField.setAccessible(true);
				 * List<String> pluginPackages = (List<String>)
				 * pluginPackagesField.get(configuration); if (pluginPackages == null) {
				 * pluginPackages = new ArrayList<String>(); } for (String s : pluginPackages) {
				 * if (PACKAGENAME.equals(s)) { return; } } pluginPackages.add(PACKAGENAME);
				 * pluginPackagesField.set(configuration, pluginPackages); } catch (Exception e)
				 * { e.printStackTrace(); }
				 */
			}
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		Object[] args = methodInfo.getArgs();
		Configuration configuration = (Configuration) args[0];
		if (configuration != null) {
			Map<String, LoggerConfig> loggers = configuration.getLoggers();
			if (loggers != null) {
				for (Entry<String, LoggerConfig> e : loggers.entrySet()) {
					if ("".equals(e.getKey())) {
						e.getValue().addFilter(new PreLog4J2Filter());
					}
				}
			}
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
