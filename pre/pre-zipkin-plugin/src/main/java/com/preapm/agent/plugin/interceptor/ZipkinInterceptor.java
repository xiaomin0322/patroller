package com.preapm.agent.plugin.interceptor;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

public class ZipkinInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(ZipkinInterceptor.class);

	private static final ZipkinClient client = ZipkinClientContext.getClient();

	@Override
	public void before(MethodInfo methodInfo) {
		try {
			/*if(client.getSpan() == null) {
				return ;
			}*/
			int ipv4 = InetAddressUtils.localIpv4();
			Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
			Long startTime = System.currentTimeMillis();
			methodInfo.setLocalVariable(new Object[] { endpoint, startTime });
			client.startSpan(methodInfo.getMethodName());
			client.sendBinaryAnnotation("className", methodInfo.getClassName(), endpoint);
			client.sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.PRE_NAME,
					Arrays.toString(methodInfo.getPlugins()), endpoint);
			client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
		/*if(client.getSpan() == null) {
			return ;
		}*/
		String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(methodInfo.getThrowable());
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		client.sendBinaryAnnotation("error", stackTrace, endpoint);
	}

	@Override
	public void after(MethodInfo methodInfo) {
		/*if(client.getSpan() == null) {
			return ;
		}*/
		
		if (methodInfo.getLocalVariable() != null) {
			Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
			Long startTime = (Long) methodInfo.getLocalVariable()[1];
			Long endTime = System.currentTimeMillis();
			client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
			long time = methodInfo.getTime();

			if ((endTime - startTime) > time) {
				if (methodInfo.isInParam()) {
					String[] argsName = methodInfo.getArgsName();
					if (argsName != null && argsName.length > 0) {
						for (int i = 0; i < argsName.length; i++) {
							String name = argsName[i];
							Object val = methodInfo.getArgs()[i];
							client.sendBinaryAnnotation("in." + name, String.valueOf(val), endpoint);
						}
					}
				}
				if (methodInfo.isOutParam()) {
					client.sendBinaryAnnotation("out", String.valueOf(methodInfo.getResult()), endpoint);
				}

			}
			client.finishSpan();
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
}
