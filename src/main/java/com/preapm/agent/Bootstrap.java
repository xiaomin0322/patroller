package com.preapm.agent;

public class Bootstrap {

	public static void main(String[] args) {
		new Bootstrap().print("123123");
		new Bootstrap().print();
	}

	public void print() {
		System.out.println("zzm");
	}

	public void print(String s) {
		System.out.println("zzm " + s);
	}
}
