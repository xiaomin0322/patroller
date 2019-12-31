package com.preapm.agent.plugin.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.serialize.SerializeFactory;
import com.preapm.sdk.zipkin.serialize.SerializeInterface;
import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.BinaryAnnotation;
import zipkin.Endpoint;

public class ZipkinInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(ZipkinInterceptor.class);

	private static final ZipkinClient client = ZipkinClientContext.getClient();

	@Override
	public void before(MethodInfo methodInfo) {
		try {
			if (client.getSpan() == null) {
				return;
			}
			int ipv4 = InetAddressUtils.localIpv4();
			Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
			Long startTime = System.currentTimeMillis();

			client.startSpan(methodInfo.getMethodName());
			client.sendBinaryAnnotation("className", methodInfo.getClassName(), endpoint);
			client.sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.PRE_NAME,
					Arrays.toString(methodInfo.getPlugins()), endpoint);
			SerializeInterface serialize = SerializeFactory.get(methodInfo.getSerialize());
			List<BinaryAnnotation> binaryAnnotationStart = create(methodInfo, serialize, endpoint);
			methodInfo.setLocalVariable(new Object[] { endpoint, startTime, serialize, binaryAnnotationStart });
			client.sendAnnotation(TraceKeys.CLIENT_SEND, endpoint);
		} catch (Exception e) {
			logger.error("beforeException",e);
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
		if (client.getSpan() == null) {
			return;
		}
		String stackTrace = org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(methodInfo.getThrowable());
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		client.sendBinaryAnnotation("error", stackTrace, endpoint);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void after(MethodInfo methodInfo) {
		if (client.getSpan() == null) {
			return;
		}
		try {
			if (methodInfo.getLocalVariable() != null) {
				Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
				Long startTime = (Long) methodInfo.getLocalVariable()[1];
				Long endTime = System.currentTimeMillis();
				client.sendAnnotation(TraceKeys.CLIENT_RECV, endpoint);
				long time = methodInfo.getTime();

				SerializeInterface serialize = (SerializeInterface) methodInfo.getLocalVariable()[2];
				if ((endTime - startTime) > time) {
					List<BinaryAnnotation> binaryAnnotationStart = (List<BinaryAnnotation>) methodInfo
							.getLocalVariable()[3];
					client.sendBinaryAnnotation(binaryAnnotationStart);
					if (methodInfo.isOutParam()) {
						client.sendBinaryAnnotation("out", serialize.serializa(methodInfo.getResult()), endpoint);
					}

				}
				client.finishSpan();
			}
		} catch (Exception e) {
			logger.error("afterException",e);
		}

	}

	public static List<BinaryAnnotation> create(MethodInfo methodInfo, SerializeInterface serialize,
			Endpoint endpoint) {
		if (!methodInfo.isInParam()) {
			return null;
		}
		List<BinaryAnnotation> binaryAnnotations = new ArrayList<>();
		String[] argsName = methodInfo.getArgsName();
		if (argsName != null && argsName.length > 0) {
			for (int i = 0; i < argsName.length; i++) {
				String name = argsName[i];
				Object val = methodInfo.getArgs()[i];
				BinaryAnnotation annotation = BinaryAnnotation.create("in." + name, serialize.serializa(val), endpoint);
				binaryAnnotations.add(annotation);
			}
		}
		return binaryAnnotations;
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
}
