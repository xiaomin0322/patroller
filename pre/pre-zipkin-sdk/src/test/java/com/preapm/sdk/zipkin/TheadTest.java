package com.preapm.sdk.zipkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheadTest {

	// static ThreadLocal<Long> longLocal = new ThreadLocal<Long>();

	static ThreadLocal<Long> longLocal = new InheritableThreadLocal<Long>();

	public static void main(String[] args) throws Exception {
		// test1();

		// test3();

		 //test2();

		//test5();
		
		//test6();
		
		test4();

	}
	
	public static void test6() throws Exception {
	
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				longLocal.set(2l);
				System.out.println("span==" + longLocal.get());
				
				new Thread(new Runnable() {
					@Override
					public void run() {
						longLocal.set(2l);
						// TODO Auto-generated method stub
						System.out.println("span22==" + longLocal.get());
					}
				}).start();
			}
		});
		Thread.sleep(100);

	}
	
	
	public static void test5() throws Exception {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				longLocal.set(2l);
				// TODO Auto-generated method stub
				System.out.println("span==" + longLocal.get());
			}
		}).start();
		Thread.sleep(100);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("span2==" + longLocal.get());
			}
		}).start();
		Thread.sleep(100);

	}

	public static void test4() throws Exception {
		// ExecutorService executorService = Executors.newFixedThreadPool(4);
		ExecutorService executorService = Executors.newCachedThreadPool();

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				longLocal.set(6666L);
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getName() + " span==" + longLocal.get());

				ExecutorService executorService2 = Executors.newFixedThreadPool(4);

				executorService2.execute(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						System.out.println(Thread.currentThread().getName() + " span2==" + longLocal.get());
					}
				});
			}
		});
		// Thread.sleep(10);

		Thread.sleep(100);

	}

	public static void test2() throws Exception {
		// ExecutorService executorService = Executors.newFixedThreadPool(4);
		ExecutorService executorService = Executors.newCachedThreadPool();

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				longLocal.set(6666L);
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getName() + " span==" + longLocal.get());
			}
		});
		// Thread.sleep(10);

		executorService.execute(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println(Thread.currentThread().getName() + " span2==" + longLocal.get());
			}
		});

		Thread.sleep(100);

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
