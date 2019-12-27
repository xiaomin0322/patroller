package com.preapm.sdk.zipkin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

public class ThreadLocalSpanStoreTest {

	static ZipkinClient client = new ZipkinClient("http://10.23.191.241:5005");
	static Endpoint endpoint  = null;
	public static void main(String[] args) throws Exception {
		test1();
	}

	public static void test1() throws Exception {
		ExecutorService executorService = Executors.newCachedThreadPool();

		ExecutorService httpClient = Executors.newCachedThreadPool();

		int ipv4 = InetAddressUtils.localIpv4();
		client.startSpan("MAIN");
		endpoint= Endpoint.builder().serviceName("test").ipv4(ipv4).build();
		client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
		
		
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				client.startSpan("TOMCATA");
				System.out.println("TOMCATA");
				httpClient.execute(new Runnable() {
					@Override
					public void run() {
						client.startSpan("httpClientA");
						System.out.println("httpClientA");
						client.finishSpan();
					}
				});
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				client.finishSpan();
			}
		});

		Thread.sleep(1000);
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				client.startSpan("TOMCATB");
				System.out.println("TOMCATB");
				httpClient.execute(new Runnable() {
					@Override
					public void run() {
						client.startSpan("httpClientB");
						System.out.println("httpClientB");
						client.finishSpan();
					}
				});
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				client.finishSpan();
			}
		});
		Thread.sleep(1000);
		client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
		client.finishSpan();

	}

}
