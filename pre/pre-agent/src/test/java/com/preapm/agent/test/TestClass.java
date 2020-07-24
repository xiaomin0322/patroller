package com.preapm.agent.test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class TestClass extends A{

	public static void main(String[] args) {
		System.out.println(TestClass.class.isAssignableFrom(TestClass.class));
		
		System.out.println(Object.class.isAssignableFrom(TestClass.class));
		
		System.out.println(Runnable.class.isAssignableFrom(TestClass.class));
		
		Set<String> pluginNameSet = new LinkedHashSet<>();
		//pluginNameSet = new HashSet<>();
		pluginNameSet.add("a");
		pluginNameSet.add("c");
		pluginNameSet.add("b");
		
		for(String s:pluginNameSet) {
			System.out.println("ssss="+s);
		}
		
		String s = StringUtils.join(pluginNameSet, ",");
		
		System.out.println(Arrays.toString(s.split(",")));
	}

	
}

class A implements  Runnable{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
