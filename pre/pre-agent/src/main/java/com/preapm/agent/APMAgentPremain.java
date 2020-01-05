package com.preapm.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import com.preapm.agent.util.LogManager;
import com.preapm.agent.util.PathUtil;

import javassist.ClassPool;

public class APMAgentPremain {

	private static Logger log = LogManager.getLogger(APMAgentPremain.class);

	public static void premain(String agentArgs, Instrumentation inst) {

		log.info("Hello, world! JavaAgen");
		log.info("agentArgs: " + agentArgs);

		Thread currentThread = Thread.currentThread();
		log.info("currentThreadClassLoader:  " + currentThread.getContextClassLoader());
		addLib(inst);
		APMAgent aPMAgentV1 = new APMAgent();
		inst.addTransformer(aPMAgentV1);
	}

	public static void addLib(Instrumentation inst) {
		String libPath = PathUtil.getProjectPath();
		log.info("当前jar包目录 === " + libPath);
		File jar = new File(libPath, "lib");
		try {
			for (File f : jar.listFiles()) {
				if (f.getName().endsWith(".jar")) {
					log.info("load jar == " + f.getAbsolutePath());
					inst.appendToBootstrapClassLoaderSearch(new JarFile(f));
					if (f.getName().startsWith("pre")) {
						ClassPool.getDefault().appendClassPath(f.getAbsolutePath());
					}
				}
			}
		} catch (Exception e) {
			log.severe(org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e));
		}
	}

}