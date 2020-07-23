package com.preapm.sdk.zipkin;

import java.util.Stack;

import com.alibaba.ttl.TransmittableThreadLocal;

public class ThreadLocalTest {

	public static ThreadLocal<Stack<String>> LOCAL_SPAN = new TransmittableThreadLocal<Stack<String>>() {
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
		test0();
	}

	public static void test0() throws Exception {
		setSpan("test0");
		test1();
		removeSpan();
	}

	public static void test1() throws Exception {
		setSpan("test1");
		new Thread(new Runnable() {
		    String s = getSpan();
			@Override
			public void run() {
				System.out.println("=======Runnable  span===========" + s);
				System.out.println("=======getspan=========="+getSpan());
				//test2();
			}
		}).start();
		removeSpan();
	}

	public static void test2() {
		System.out.println("=======span===========" + getSpan());
		setSpan("test2");
		removeSpan();
	}
}
