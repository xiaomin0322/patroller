package com.dominos.cloud.agent;

public class TestServiceMain {

	public static void main(String[] args) {
		new TestServiceMain().print("123123");
		System.out.println("=============");
		new TestServiceMain().print();
	}

	public void print() {
		System.out.println("zzm");
	}
	
	public void print(String s) {
		System.out.println("zzm "+s);
	}
}
