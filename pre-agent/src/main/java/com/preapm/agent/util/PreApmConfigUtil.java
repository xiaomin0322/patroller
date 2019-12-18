package com.preapm.agent.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.preapm.agent.bean.PluginConfigBean;
import com.preapm.agent.bean.PluginJarBean;

public class PreApmConfigUtil {
	
	private static Map<String, PluginConfigBean> targetMap = new HashMap<>();
	
	private static String pre_Zipkin_plugin = "com.preapm.agent.plugin.interceptor.ZipkinInterceptor";
	
	private static String pre_httpclient4_plugin = "com.preapm.agent.plugin.interceptor.Httpclient4Interceptor";
	
	private static String pre_Tomcat_plugin = "com.preapm.agent.plugin.interceptor.TomcatInterceptor";
	
	
    private static String pre_Zipkin_plugin_jar = "pre-zipkin-plugin";
	
	private static String pre_httpclient4_plugin_jar = "pre-httpclient4-plugin";
	
	private static String pre_Tomcat_plugin_jar = "pre-tomcat-plugin";

	static {
		PluginJarBean jarBeanZipkin = new PluginJarBean();
		jarBeanZipkin.setName(pre_Zipkin_plugin);
		jarBeanZipkin.setNameJar(pre_Zipkin_plugin_jar);
		
		PluginJarBean jarBeanhttpclient4 = new PluginJarBean();
		jarBeanhttpclient4.setName(pre_httpclient4_plugin);
		jarBeanhttpclient4.setNameJar(pre_httpclient4_plugin_jar);
		
		
		PluginJarBean jarBeanTomcat = new PluginJarBean();
		jarBeanTomcat.setName(pre_Tomcat_plugin);
		jarBeanTomcat.setNameJar(pre_Tomcat_plugin_jar);
		
		
		
		Set methodSet = new HashSet<>();
		
		
		methodSet.add("com.preapm.agent.Bootstrap.print2(java.lang.String)");
		methodSet.add("com.preapm.agent.Bootstrap.print3(java.lang.String,java.lang.String)");
		PluginConfigBean bean  = new PluginConfigBean();
		bean.setName("BootstrapTest");
		bean.setLoadPatterns("com.preapm.agent.Bootstrap");
		bean.setPatterns("com.preapm.agent.Bootstrap");
		bean.setContainPatterns(methodSet);
		methodSet = new HashSet<>();
		methodSet.add(jarBeanZipkin);
		bean.setPlugins(methodSet);
		targetMap.put(bean.getPatterns(), bean);

		methodSet = new HashSet<>();
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection()");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection(long)");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnectionDirect(long)");
		methodSet.add("com.alibaba.druid.pool.DruidDataSource.getConnection(java.lang.String, java.lang.String)");
		bean  = new PluginConfigBean();
		bean.setName("DruidDataSource");
		bean.setLoadPatterns("com.alibaba.druid.pool.DruidDataSource");
		bean.setPatterns("com.alibaba.druid.pool.DruidDataSource");
		bean.setContainPatterns(methodSet);
		methodSet = new HashSet<>();
		methodSet.add(jarBeanZipkin);
		bean.setPlugins(methodSet);
		targetMap.put(bean.getPatterns(), bean);

		methodSet = new HashSet<>();
		methodSet.add(
				"com.dominos.cloud.im.controller.StoreController.queryStoreGroupAll()");
		methodSet.add(
				"com.dominos.cloud.im.controller.StoreController.test(com.dominos.cloud.im.controller.ProductController)");
		methodSet.add(
				"com.dominos.cloud.im.controller.StoreController.test2(com.dominos.cloud.im.controller.ProductController,com.dominos.cloud.im.model.StoreGroupsWithBLOBs)");
		bean  = new PluginConfigBean();
		bean.setName("StoreController");
		bean.setLoadPatterns("com.dominos.cloud.im.controller.StoreController");
		bean.setPatterns("com.dominos.cloud.im.controller.StoreController");
		bean.setContainPatterns(methodSet);
		methodSet = new HashSet<>();
		methodSet.add(jarBeanZipkin);
		bean.setPlugins(methodSet);
		targetMap.put(bean.getPatterns(), bean);
		
		
		methodSet = new HashSet<>();
		methodSet.add(
				"org.exampledriven.zuul.eureka.customer.shared.server.server.rest.CustomerController.getCustomer(int)");
		bean  = new PluginConfigBean();
		bean.setName("CustomerController");
		bean.setLoadPatterns("org.exampledriven.zuul.eureka.customer.shared.server.server.rest.CustomerController");
		bean.setPatterns("org.exampledriven.zuul.eureka.customer.shared.server.server.rest.CustomerController");
		bean.setContainPatterns(methodSet);
		methodSet = new HashSet<>();
		methodSet.add(jarBeanZipkin);
		bean.setPlugins(methodSet);
		targetMap.put(bean.getPatterns(), bean);
		
		
		
		
		
		
		methodSet = new HashSet<>();
		//methodSet.add("org.apache.catalina.connector.Request.getRequest()");
		methodSet.add("org.apache.catalina.core.ApplicationFilterChain.doFilter(javax.servlet.ServletRequest,javax.servlet.ServletResponse)");
		bean  = new PluginConfigBean();
		bean.setName("tomcat");
		bean.setLoadPatterns("org.apache.catalina.connector.Request");
		//bean.setPatterns("org.apache.catalina.connector.Request");
		//bean.setLoadPatterns("org.apache.catalina.core.ApplicationFilterChain");
		bean.setPatterns("org.apache.catalina.core.ApplicationFilterChain");
		bean.setContainPatterns(methodSet);
		methodSet = new HashSet<>();
		methodSet.add(jarBeanTomcat);
		bean.setPlugins(methodSet);
		targetMap.put(bean.getPatterns(), bean);
		
		
		methodSet = new HashSet<>();
		methodSet.add("org.apache.http.impl.client.CloseableHttpClient.execute(org.apache.http.client.methods.HttpUriRequest)");
		bean  = new PluginConfigBean();
		bean.setName("httpClient4");
		bean.setLoadPatterns("org.apache.http.impl.client.CloseableHttpClient");
		bean.setPatterns("org.apache.http.impl.client.CloseableHttpClient");
		bean.setContainPatterns(methodSet);
		methodSet = new HashSet<>();
		methodSet.add(jarBeanhttpclient4);
		bean.setPlugins(methodSet);
		targetMap.put(bean.getPatterns(), bean);

	}
	
	public static PluginConfigBean  get(String key){
		return targetMap.get(key);
	}
	
	
	public static boolean isTarget(String className, String method) {
		boolean flag = isTarget(className);
		if(!flag) {
			return flag;
		}
		flag = PreApmConfigUtil.get(className).getContainPatterns().contains(method);
		if(flag) {
			System.out.println("className="+className+ " method"+method + "匹配到了！！！！！");
		}
		return flag;
	}
	
	public static Set<PluginJarBean> getPlugins(String className) {
		boolean flag = isTarget(className);
		if(!flag) {
			return null;
		}
		return PreApmConfigUtil.get(className).getPlugins();
	}

	public static boolean isTarget(String className) {
		Set<String> methodSet = PreApmConfigUtil.get(className).getContainPatterns();
		if (methodSet == null || methodSet.isEmpty()) {
			return false;
		}
		return true;
	}
	
	
	

}
