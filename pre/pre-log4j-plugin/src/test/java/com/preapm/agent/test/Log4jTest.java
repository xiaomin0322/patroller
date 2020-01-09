package com.preapm.agent.test;

import org.apache.log4j.Logger;

public class Log4jTest {

	final static Logger logger = Logger.getLogger(Log4jTest.class);

	public static void main(String[] args) {

		logger.info("123123123123 ");
		logger.info("aaa {}");
	}

}
