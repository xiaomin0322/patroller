package com.preapm.agent.test;

import com.preapm.agent.bean.PatternsYaml;
import com.preapm.agent.bean.PatternsYaml.PatternMethod;
import com.preapm.agent.bean.PluginConfigYaml;
import com.preapm.agent.util.YMLMappingUtil;

public class YMLMappingUtilTest {

	public static void main(String[] args) throws Exception {
		PluginConfigYaml patternsYaml = (PluginConfigYaml) YMLMappingUtil.reader(PluginConfigYaml.class);
		System.out.println(patternsYaml.getPlugins());

		PatternsYaml aa = (PatternsYaml) YMLMappingUtil.reader(PatternsYaml.class);
		System.out.println(aa.getPatterns());
		
		
		for(PatternMethod m:aa.getPatterns().get("durid").getIncludedPatterns()) {
			System.out.println(m.getKey());
			System.out.println(m.getPlugins());
		}
		
	}

}
