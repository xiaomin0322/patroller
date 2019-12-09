package com.dominos.cloud.agent;

import java.lang.instrument.Instrumentation;

public class APMAgentMain {

	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Hello, world! JavaAgen");
		System.out.println("agentArgs: " + agentArgs);
		Thread currentThread = Thread.currentThread();
		System.out.println("currentThreadClassLoader: " + currentThread.getContextClassLoader());
		try {
			APMAgentV1 aPMAgentV1 = new APMAgentV1();
			inst.addTransformer(aPMAgentV1);
			System.out.println("currentThread: " + currentThread.getContextClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}