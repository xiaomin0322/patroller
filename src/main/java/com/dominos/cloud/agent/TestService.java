package com.dominos.cloud.agent;

public class TestService {

	public static void main(String[] args) {
		new TestService().print("123123");
		System.out.println("=============");
		new TestService().print();
	}

	public void print() {
		System.out.println("zzm");
	}
	
	public void print(String s) {
		System.out.println("zzm "+s);
	}
}
