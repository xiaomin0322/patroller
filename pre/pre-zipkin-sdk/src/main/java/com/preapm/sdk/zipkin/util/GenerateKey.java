package com.preapm.sdk.zipkin.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * <pre>
 * GenerateKey
 * </pre>
 * 
 * @author 
 *
 * @since 2018年1月25日 下午4:18:37
 */
public class GenerateKey {
	//private static Random RANDOM = new Random();

	public static Long longKey() {
		//return RANDOM.nextLong();
		return ThreadLocalRandom.current().nextLong();
	}
}
