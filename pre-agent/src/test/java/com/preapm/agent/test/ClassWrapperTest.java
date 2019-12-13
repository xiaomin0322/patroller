package com.preapm.agent.test;

import com.preapm.agent.weave.ClassWrapper;

public class ClassWrapperTest {

	
	public static void main(String[] args) {
		String test = "12313";

		String s = " System.out.println(\"加入span成功：\"+" + ClassWrapper.toStr(test) + ");\r\n";
		
		System.out.println(s);
		
		s = " System.out.println(\"参数名称:\"+"+ ClassWrapper.toStrto(test)
		 +"\"参数值：\"+"+test+");\r\n";
		
		System.out.println(s);
		

	}
}
