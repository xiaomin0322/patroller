package com.preapm.agent.test;

import com.preapm.agent.util.PreConfigUtil;

public class PreConfigUtilTest {
	
	
	public static void main(String[] args) {
		System.out.println(PreConfigUtil.isTarget("com.preapm.agent.Bootstrap"));
		System.out.println(PreConfigUtil.isTarget("com.preapm.agent.Bootstrap", "com.preapm.agent.Bootstrap\\(\\)"));
		
		System.out.println(PreConfigUtil.isTarget("com.preapm.agent.Bootstrap", "com.preapm.agent.Bootstrap.print2\\(java.lang.String\\)"));
	}

}
