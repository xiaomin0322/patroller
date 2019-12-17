package com.preapm.sdk.zipkin;

import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

public class ZipkinClientTest {

	public static void main(String[] args) throws Exception {
		ZipkinClient client = new ZipkinClient("http://10.23.191.1:5005");
		int ipv4 = InetAddressUtils.localIpv4();
		Endpoint endpoint = Endpoint.builder().serviceName("test").ipv4(ipv4).build();
		client.startSpan("testSpan");
		client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
		client.sendBinaryAnnotation("in", "in", endpoint);
		Thread.sleep(1000);

		client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
		client.sendBinaryAnnotation("test" + TraceKeys.SUFFIX_ERROR, "ERROR", endpoint);

		client.finishSpan();
		
		System.out.println("end");

	}

}
