package com.preapm.agent.test;

import com.preapm.agent.util.ClassLoaderUtil;

public class ClassLoaderUtilTest {
	
	public static void main(String[] args) {
		ClassLoaderUtil.loadJarByClassName(null, "java.lang.String");
	}

}
