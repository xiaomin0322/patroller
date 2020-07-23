package com.preapm.agent.test;

public class TestClass extends A{

	public static void main(String[] args) {
		System.out.println(TestClass.class.isAssignableFrom(TestClass.class));
		
		System.out.println(Object.class.isAssignableFrom(TestClass.class));
		
		System.out.println(Runnable.class.isAssignableFrom(TestClass.class));
	}

	
}

class A implements  Runnable{
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
