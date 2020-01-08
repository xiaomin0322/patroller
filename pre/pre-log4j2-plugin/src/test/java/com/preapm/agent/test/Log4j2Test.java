package com.preapm.agent.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Test {

	private static Logger logger_ = LogManager.getLogger(Log4j2Test.class);

	public static void main(String[] args) {

		logger_.info("123123123123 {}","a");
		logger_.info("aaa {}","b");
	}

}
