package com.preapm.agent.plugin.interceptor;

import java.math.BigInteger;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.sdk.zipkin.ZipkinClientContext;
import com.preapm.sdk.zipkin.util.InetAddressUtils;
import com.preapm.sdk.zipkin.util.TraceKeys;

import zipkin.Endpoint;

/**
 * org.apache.catalina.connector.Request.Request()
 * org.apache.catalina.connector.Request.getRequest()
 * 
 * org.apache.catalina.core.ApplicationFilterChain.doFilter(ServletRequest,
 * ServletResponse)
 * 
 * tomcat要拦截最初的请求位置，因为tomcat servlet 内置拦截器，要在拦截器之前拦截：
 *  
 *      如果拦截 org.apache.catalina.core.StandardWrapperValve.invoke方法在拦截器之后，servlet之前的拦截器将得不到链路信息。
 *     造成得后果是: 拦截器得 链路，和log打印得trace_id 错乱 
 *   
 * 
 * @author Zengmin.Zhang
 *
 */
public class TomcatInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(TomcatInterceptor.class);
	
	private static final String TOMCAT_STR = "TOMCAT";

	@Override
	public void before(MethodInfo methodInfo) {
		int ipv4 = InetAddressUtils.localIpv4();
		Endpoint endpoint = Endpoint.builder().serviceName(ZipkinClientContext.serverName).ipv4(ipv4).build();
		methodInfo.setLocalVariable(new Object[] {endpoint});
		try {
			HttpServletRequest request = (HttpServletRequest) methodInfo.getArgs()[0];
			//System.out.println("request================="+request);
			String url = request.getRequestURL().toString();
			String method = request.getMethod();
			String queryString = request.getQueryString() == null ? "" :  request.getQueryString() ;
			String trace_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.TRACE_ID);
			String span_id = request.getHeader(com.preapm.sdk.zipkin.util.TraceKeys.SPAN_ID);
			String clientIP = com.preapm.sdk.zipkin.util.ClientUtil.getSpbillIp(request);
			
			ZipkinClientContext.getClient().getSpanStore().removeAllSpan();
			
			if (trace_id != null && span_id != null) {
				BigInteger trace_id_bi = new BigInteger(trace_id, 16);
				BigInteger span_id_bi = new BigInteger(span_id, 16);
				ZipkinClientContext.getClient().startSpan(trace_id_bi.longValue(), span_id_bi.longValue(), TOMCAT_STR);
				ZipkinClientContext.getClient().sendBinaryAnnotation("traceRoot","false");
			}else {
				/**
				 *   tomcat两个线程会传递
				 * http-nio-9005-exec-1 当前span ===-5227500798708065629 parentId  null traceId -5227500798708065629
                  http-nio-9005-exec-2 parent Span   -5227500798708065629 traceId -5227500798708065629
				 */
				ZipkinClientContext.getClient().startRootSpan(TOMCAT_STR);
				ZipkinClientContext.getClient().sendBinaryAnnotation("traceRoot","true");
			}
			//此处触发com.preapm.agent.plugin.interceptor.filter.BackLogFilter MDC.put("X-B3-TraceId"
			logger.info("获取trace_id：{}" , trace_id);
			logger.info("获取span_id：{}" , span_id);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_CLIENT_IP,clientIP, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_QUERY_STRING,queryString, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_METHOD,method, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.HTTP_URL,url, endpoint);
			ZipkinClientContext.getClient().sendBinaryAnnotation(com.preapm.sdk.zipkin.util.TraceKeys.PRE_NAME,Arrays.toString(methodInfo.getPlugins()), endpoint);
			ZipkinClientContext.getClient().sendAnnotation(TraceKeys.SERVER_SEND, endpoint);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		Endpoint endpoint = (Endpoint) methodInfo.getLocalVariable()[0];
		ZipkinClientContext.getClient().sendAnnotation(TraceKeys.SERVER_RECV, endpoint );
		ZipkinClientContext.getClient().finishSpan();
		//TODO 此处不能全部删掉，因为在业务中使用多线程，线程池会有问题。
		//ZipkinClientContext.getClient().getSpanStore().removeAllSpan();

	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
