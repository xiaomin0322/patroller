package com.preapm.agent;

import java.io.File;
import java.lang.instrument.Instrumentation;
import java.util.jar.JarFile;

import org.slf4j.LoggerFactory;

import com.preapm.agent.util.PathUtil;


public class APMAgentPremain {

	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(APMAgentPremain.class);

	public static void premain(String agentArgs, Instrumentation inst) {
		
		//PropertyConfigurator.configure("C:\\eclipse-workspace\\zipkin-agent-main\\target\\lib\\log4j.properties");
		
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
				if(f.getName().endsWith(".jar")) {
					log.info("load jar == " + f.getAbsolutePath());
					inst.appendToBootstrapClassLoaderSearch(new JarFile(f));
					//inst.appendToSystemClassLoaderSearch(new JarFile(f));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}