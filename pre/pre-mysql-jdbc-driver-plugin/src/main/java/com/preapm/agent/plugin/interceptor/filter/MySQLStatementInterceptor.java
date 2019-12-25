package com.preapm.agent.plugin.interceptor.filter;

import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptorV2;
import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;

public class MySQLStatementInterceptor implements StatementInterceptorV2 {
	public static final String SQL_STR = "SQL";

	@Override
	public void init(Connection conn, Properties props) throws SQLException {

	}

	@Override
	public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
			throws SQLException {
		ZipkinClient client = ZipkinClientContext.getClient();
		client.startSpan(SQL_STR);
		client.sendBinaryAnnotation(SQL_STR, sql);
		return null;
	}

	@Override
	public boolean executeTopLevelOnly() {
		return false;
	}

	@Override
	public void destroy() {

	}

	@Override
	public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement,
			ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed,
			boolean noGoodIndexUsed, SQLException statementException) throws SQLException {
		ZipkinClientContext.getClient().finishSpan();
		return null;
	}

}