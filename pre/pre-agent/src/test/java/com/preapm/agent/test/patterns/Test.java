package com.preapm.agent.test.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.preapm.agent.weave.pattern.JdkRegexpMethodPointcut;

public class Test {

	public static void main(String[] args) {
		JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
		String[] strings = new String[3];

		strings[0] = "com.preapm.agent.Bootstrap\\(\\)";
		strings[1] = "com.preapm.agent.Bootstrap.print2\\(java.lang.String\\)";
		strings[2] = "com.preapm.agent.Bootstrap.print3\\(java.lang.String,java.lang.String\\)";

		pointcut.setPatterns(strings);

		Pattern[] patterns = pointcut.compilePatterns(strings);

		pointcut.setCompiledPatterns(patterns);

//        System.out.println(pointcut.matchesPattern("com.preapm.agent.Bootstrap.print"));
		System.out.println(pointcut.matchesPattern("com.preapm.agent.Bootstrap.print2(java.lang.String)"));

		List<String> s = new ArrayList<>();
		s.add("com.p.*");
		s.add("com.*");
		String key = JdkRegexpMethodPointcut.macthR(s, "com.preapm.agent.test.patterns.Test");

		System.out.println(key);
		
		System.out.println(JdkRegexpMethodPointcut.macthsR(s, "com.preapm.agent.test.patterns.Test"));

	}
}
