package com.dominos.cloud.test;

public class ClassWrapperTest {

	
	public static void main(String[] args) {
		String test = "12313";

		String s = " System.out.println(\"加入span成功：\"+" + toStr(test) + ");\r\n";
		
		System.out.println(s);
		
		s = " System.out.println(\"参数名称:\"+"+ toStrto(test)
		 +"\"参数值：\"+"+test+");\r\n";
		
		System.out.println(s);
		

	}
}
