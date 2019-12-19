package com.preapm.agent.test.patterns;


import com.preapm.agent.Bootstrap;
import com.preapm.agent.weave.pattern.JdkRegexpMethodPointcut;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
        String[] strings = new String[1];

        strings[0] = "public java.lang.String com.preapm.agent.Bootstrap.print\\(java.lang.String\\)";


        pointcut.setPatterns(strings);

        Pattern[] patterns = pointcut.compilePatterns(strings);


        pointcut.setCompiledPatterns(patterns);

        Method method = Bootstrap.class.getMethods()[1];


//        System.out.println(pointcut.matchesPattern("com.preapm.agent.Bootstrap.print"));
        System.out.println(pointcut.matchesPattern(method.toString()));

    }
}
