package com.preapm.sdk.zipkin;

import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocal2Test {

	/*
	 * public static ThreadLocal<Stack<String>> LOCAL_SPAN = new
	 * TransmittableThreadLocal<Stack<String>>() {
	 * 
	 * @Override protected Stack<String> initialValue() { return new Stack<>(); } };
	 */

	/**
	 * 此时不需要阿里的theadlocal
	 */
	public static ThreadLocal<Stack<String>> LOCAL_SPAN = new ThreadLocal<Stack<String>>() {
		@Override
		protected Stack<String> initialValue() {
			return new Stack<>();
		}
	};

	public static String getSpan() {
		return LOCAL_SPAN.get().empty() ? null : LOCAL_SPAN.get().peek();
	}

	public static void setSpan(String span) {
		if (span == null) {
			if (!LOCAL_SPAN.get().empty()) {
				LOCAL_SPAN.get().pop();
			}
		} else {
			LOCAL_SPAN.get().push(span);
		}
	}

	public static void removeSpan() {
		if (!LOCAL_SPAN.get().empty()) {
			LOCAL_SPAN.get().pop();
		}
	}

	public static void main(String[] args) throws Exception {

		for (int i = 0; i < 10; i++) {
			test0();
		}
	}

	public static void test0() throws Exception {
		setSpan("test0");
		test1();
		removeSpan();
	}

	static ExecutorService executorService = Executors.newFixedThreadPool(1);

	public static void test1() throws Exception {
		String uid = UUID.randomUUID().toString();
		setSpan("test_" + uid);
		executorService.submit(new Runnable() {
			String s = getSpan();

			@Override
			public void run() {
				System.out.println(Thread.currentThread().getId() + "=======GetRunnableSpan===========" + s);
				System.out.println(Thread.currentThread().getId() + "=======getspan==========" + getSpan());
			}
		});
		executorService.submit(new Callable<String>() {
			String s = getSpan();

			@Override
			public String call() {
				System.out.println(Thread.currentThread().getId() + "=======GetCallableSpan===========" + s);
				System.out.println(Thread.currentThread().getId() + "=======getspan==========" + getSpan());
				return null;
			}
		});
		removeSpan();
	}

	public static void test2() {
		System.out.println("=======span===========" + getSpan());
		setSpan("test2");
		removeSpan();
	}
}
