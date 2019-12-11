package com.dominos.cloud.test;

public class FooTest {

	public String getFoo() {
		return "foo";
	}

	public static void main(String[] args) {
		String filepath = System.getProperty("user.dir");
		System.out.println(filepath);
	}
}