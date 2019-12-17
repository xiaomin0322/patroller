package com.preapm.sdk.zipkin;

import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

public class Bootstrap {
	static ZipkinClient client = new ZipkinClient("http://10.23.191.242:5005");

	public static void main(String[] args) {
		new Bootstrap().print3("123123", "123");
	}

	public void print() {
		System.out.println("zzm");
	}

	public String print2(String s) {
		int ipv4 = InetAddressUtils.localIpv4();
		Endpoint endpoint = Endpoint.builder().serviceName("test").ipv4(ipv4).build();
		client.startSpan("print2");
		client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
		client.sendBinaryAnnotation("in", "in", endpoint);

		System.out.println("zzm " + s);

		client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
		client.sendBinaryAnnotation("test" + TraceKeys.SUFFIX_ERROR, "ERROR", endpoint);
		client.finishSpan();

		return s;
	}

	public String print3(String s, String s2) {
		int ipv4 = InetAddressUtils.localIpv4();
		Endpoint endpoint = Endpoint.builder().serviceName("test").ipv4(ipv4).build();
		client.startSpan("print3");
		client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
		client.sendBinaryAnnotation("in", "in", endpoint);
		System.out.println("zzm " + s);

		print2(s2);

		client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
		client.sendBinaryAnnotation("test" + TraceKeys.SUFFIX_ERROR, "ERROR", endpoint);
		client.finishSpan();
		return s2;
	}

}
