package com.preapm.sdk.zipkin;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.sdk.zipkin.util.PathUtil;
import com.preapm.sdk.zipkin.util.PropertiesUtil;

public class ZipkinClientContext {
	private static final Logger logger = LoggerFactory.getLogger(ZipkinClientContext.class);

	public static ZipkinClient client = null;

	public static PropertiesUtil propertiesUtil = null;
	
	public static String serverName = System.getProperty("server.name");
	
	public static String zipkinUrl = System.getProperty("zipkin.url");

	private ZipkinClientContext() {
	}

	static {
		init();
	}

	private static void init() {
		logger.info("ZipkinClientContext init start ");
		File file = new File(PathUtil.getProjectPath(), "zipkin.properties");
		propertiesUtil = new PropertiesUtil(file);
		if(zipkinUrl == null) {
			zipkinUrl = propertiesUtil.getProperty("zipkin.url");
		}
		logger.info("zipkin.url  {}", zipkinUrl);
		if(serverName == null) {
			serverName =  propertiesUtil.getProperty("server.name");
		}
		logger.info("serverName  {}", serverName); 
		client = new ZipkinClient(zipkinUrl);
		logger.info("ZipkinClientContext init end ");
	}

	public static ZipkinClient getClient() {
		return client;
	}

}
