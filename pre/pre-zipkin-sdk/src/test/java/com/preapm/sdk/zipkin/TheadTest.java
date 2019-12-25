package com.preapm.sdk.zipkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheadTest {

	// static ThreadLocal<Long> longLocal = new ThreadLocal<Long>();

	static ThreadLocal<Long> longLocal = new InheritableThreadLocal<Long>();

	public static void main(String[] args) throws Exception {
		test1();
		
		test3();

	}

	public static void test3() throws Exception {
		longLocal.set(2l);
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("span==" + longLocal.get());
			}
		});
		Thread.sleep(100);

	}

	public static void test1() throws Exception {
		longLocal.set(2l);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("span==" + longLocal.get());
			}
		}).start();
		Thread.sleep(100);

	}

}
