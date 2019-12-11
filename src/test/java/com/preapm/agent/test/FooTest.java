package com.preapm.agent.test;

import com.preapm.agent.weave.ClassWrapper;

public class FooTest {

	public String getFoo() {
		return "foo";
	}

	public static void main(String[] args) {
		String filepath = System.getProperty("user.dir");
		System.out.println(filepath);
		String arg = "1";
		
		String s = "newSpan.tag("+ClassWrapper.toStr(arg)+", "+"$args["+2+"]"+");\r\n";
		System.out.println(s);
		
		String ss = "newSpan.tag(\"out\", "+"com.alibaba.fastjson.JSONObject.toJSONString(["+arg+")"+");\r\n";
		
		System.out.println(ss);
	}
}