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

	private ZipkinClientContext() {
	}

	static {
		init();
	}

	private static void init() {
		logger.info("ZipkinClientContext init start ");
		File file = new File(PathUtil.getRealPath(), "zipkin.properties");
		propertiesUtil = new PropertiesUtil(file);
		String url = propertiesUtil.getProperty("zipkin.url");
		logger.info("zipkin.url  {}", url);
		client = new ZipkinClient(url);

		logger.info("ZipkinClientContext init end ");
	}

	public static ZipkinClient getClient() {
		return client;
	}

}
