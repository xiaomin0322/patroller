package com.preapm.agent.plugin.interceptor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.ConnectionImpl;
import com.mysql.jdbc.NoSubInterceptorWrapper;
import com.mysql.jdbc.StatementInterceptorV2;
import com.preapm.agent.common.bean.MethodInfo;
import com.preapm.agent.common.interceptor.AroundInterceptor;
import com.preapm.agent.plugin.interceptor.filter.MySQLStatementInterceptor;

/**
 * com.mysql.jdbc.ConnectionImpl.initializeSafeStatementInterceptors()
 * 
 * @author Zengmin.Zhang
 *
 */
public class MySQLInterceptor implements AroundInterceptor {
	private static final Logger logger = LoggerFactory.getLogger(MySQLInterceptor.class);

	@Override
	public void before(MethodInfo methodInfo) {

	}

	@Override
	public void exception(MethodInfo methodInfo) {
	}

	@Override
	public void after(MethodInfo methodInfo) {
		ConnectionImpl connectionImpl = (ConnectionImpl)methodInfo.getTarget();
		List<StatementInterceptorV2> statementInterceptorsInstances = connectionImpl.getStatementInterceptorsInstances();
		statementInterceptorsInstances.add(new NoSubInterceptorWrapper(new MySQLStatementInterceptor()));
		logger.info("com.preapm.agent.plugin.interceptor.MySQLInterceptor after ok >>>>>>>>>>>>>>>>");
	}

	@Override
	public String name() {
		return this.getClass().getName();
	}
}
