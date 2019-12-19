package com.preapm.agent;

public class Bootstrap {
	
	public Integer i = new Integer(5);
	
	public Object o;
	
	public Bootstrap() {
		 System.out.println("Bootstrap start");
	 System.out.println("Bootstrapend");
		
	}

	public static void main(String[] args) {
		new Bootstrap();
		System.out.println("asd");
		
		new Bootstrap().print2("123123");
		//new Bootstrap().print();
		//new Bootstrap().print3("123123","123");
	}

	public void print() {
		System.out.println("zzm");
	}

	public String print2(String s) {
		System.out.println("zzm " + s);
		return s;
	}
	public String print3(String s,String s2) {
		System.out.println("zzm " + s);
		print2(s2);
		throw new RuntimeException("test");
	}
	
}
