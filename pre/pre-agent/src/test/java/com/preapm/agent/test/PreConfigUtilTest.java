package com.preapm.agent.test;

import java.util.ArrayList;
import java.util.List;

import com.preapm.agent.util.PreConfigUtil;
import com.preapm.agent.weave.pattern.JdkRegexpMethodPointcut;

public class PreConfigUtilTest {

	public static void main(String[] args) {
		/*
		 * System.out.println(PreConfigUtil.isTargetR("com.preapm.agent.Bootstrap",
		 * "com.preapm.agent.Bootstrap()"));
		 * System.out.println(PreConfigUtil.isTarget("com.preapm.agent.Bootstrap",
		 * "com.preapm.agent.Bootstrap.print2(java.lang.String)"));
		 * System.out.println(PreConfigUtil.isTargetR(
		 * "com.*.cloud.im.controller.StoreController", "test()"));
		 */

		System.out.println(
				PreConfigUtil.isTargetR("redis.clients.jedis.BinaryJedis", "redis.clients.jedis.BinaryJedis.ping()"));

		List<String> patternsList = new ArrayList<>();
		patternsList.add("redis.clients.jedis.BinaryJedis.ping.*");

		System.out.println(JdkRegexpMethodPointcut.macth(patternsList, "redis.clients.jedis.BinaryJedis.ping()"));
		
		System.out.println(byte.class);

		System.out.println(byte[].class);
		
		System.out.println(byte[][].class);
	}

}
