package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;


/**
 * ch.qos.logback.core.joran.action.AppenderAction.begin(InterpretationContext,
 * String, Attributes)
 * 
 * @author Zengmin.Zhang
 *
 */
public class Log4j2Interceptor implements AroundInterceptor {
	//private static final Logger logger = LoggerFactory.getLogger(LogbackInterceptor.class);


	@Override
	public void before(MethodInfo methodInfo) {

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void after(MethodInfo methodInfo) {
		//System.out.println("methodInfo : {} "+methodInfo);
		/*InterpretationContext context = (InterpretationContext) methodInfo.getArgs()[0];
		Object object = context.peekObject();
		if (object instanceof Appender) {
			Appender appender = (Appender) object;
			appender.addFilter(filter);
		}*/
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
