package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

public class ZipkinInterceptor implements AroundInterceptor {

	private static final ZipkinClient client = ZipkinClientContext.getClient();

	@Override
	public void before(MethodInfo methodInfo) {
		try {
			int ipv4 = InetAddressUtils.localIpv4();
			Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
			methodInfo.setLocalVariable(new Object[] { endpoint });
			client.startSpan(methodInfo.getMethodName());
			client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
			String[] argsName = methodInfo.getArgsName();
			if (argsName != null && argsName.length > 0) {
				for (int i = 0; i < argsName.length; i++) {
					String name = argsName[i];
					Object val = methodInfo.getArgs()[i];
					client.sendBinaryAnnotation("in." + name, String.valueOf(val), endpoint);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
		String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(methodInfo.getThrowable());
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		client.sendBinaryAnnotation("error", stackTrace, endpoint);
	}

	@Override
	public void after(MethodInfo methodInfo) {
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
		client.finishSpan();

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
}
