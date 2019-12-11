package com.preapm.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import com.preapm.agent.util.PathUtils;

public class APMAgentPremain {

	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Hello, world! JavaAgen");
		System.out.println("agentArgs: " + agentArgs);
		Thread currentThread = Thread.currentThread();
		System.out.println("currentThreadClassLoader: " + currentThread.getContextClassLoader());
		addLib(inst);
		APMAgent aPMAgentV1 = new APMAgent();
		inst.addTransformer(aPMAgentV1);
	}

	public static void addLib(Instrumentation inst) {
		String libPath = PathUtils.getProjectPath();
		System.out.println("当前jar包目录 ===" + libPath);
		File jar = new File(libPath,"lib");
		try {
			for (File f : jar.listFiles()) {
				System.out.println("load jar ==" + f.getAbsolutePath());
				inst.appendToBootstrapClassLoaderSearch(new JarFile(f));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}