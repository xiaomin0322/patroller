package com.preapm.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import com.preapm.agent.util.PathUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class APMAgentPremain {

	public static void premain(String agentArgs, Instrumentation inst) {
		log.info("Hello, world! JavaAgen");
		log.info("agentArgs: {}", agentArgs);
		Thread currentThread = Thread.currentThread();
		log.info("currentThreadClassLoader:  {}", currentThread.getContextClassLoader());
		addLib(inst);
		APMAgent aPMAgentV1 = new APMAgent();
		inst.addTransformer(aPMAgentV1);
	}

	public static void addLib(Instrumentation inst) {
		String libPath = PathUtil.getProjectPath();
		log.info("当前jar包目录 === {}", libPath);
		File jar = new File(libPath, "lib");
		try {
			for (File f : jar.listFiles()) {
				log.info("load jar =={} ", f.getAbsolutePath());
				inst.appendToBootstrapClassLoaderSearch(new JarFile(f));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}