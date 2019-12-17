package com.preapm.agent;

public class Bootstrap {

	public static void main(String[] args) {
		new Bootstrap().print("123123");
		new Bootstrap().print();
		new Bootstrap().print("123123","123");
	}

	public void print() {
		System.out.println("zzm");
	}

	public String print(String s) {
		System.out.println("zzm " + s);
		return s;
	}
	public String print(String s,String s2) {
		System.out.println("zzm " + s);
		print(s2);
		throw new RuntimeException("test");
	}
	
}
