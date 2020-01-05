package com.preapm.agent.plugin.interceptor;

import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.filter.BackLogFilter;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.spi.InterpretationContext;

/**
 * ch.qos.logback.core.joran.action.AppenderAction.begin(InterpretationContext,
 * String, Attributes)
 * 
 * @author Zengmin.Zhang
 *
 */
public class LogbackInterceptor implements AroundInterceptor {
	//private static final Logger logger = LoggerFactory.getLogger(LogbackInterceptor.class);

	private static final BackLogFilter filter = new BackLogFilter();

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
		InterpretationContext context = (InterpretationContext) methodInfo.getArgs()[0];
		Object object = context.peekObject();
		if (object instanceof Appender) {
			Appender appender = (Appender) object;
			appender.addFilter(filter);
		}
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}

}
