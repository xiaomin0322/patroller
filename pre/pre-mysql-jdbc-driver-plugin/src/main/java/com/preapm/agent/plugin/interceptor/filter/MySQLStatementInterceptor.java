package com.preapm.agent.plugin.interceptor.filter;

import java.sql.SQLException;
import java.util.Properties;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptorV2;
import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;

public class MySQLStatementInterceptor implements StatementInterceptorV2 {
	public static final String SQL_STR = "SQL";

	public static final String PRE_SQL_STR = "PRE_SQL";

	public static final String POST_SQL_STR = "POST_SQL";

	public static final ZipkinClient client = ZipkinClientContext.getClient();

	@Override
	public void init(Connection conn, Properties props) throws SQLException {

	}

	@Override
	public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
			throws SQLException {
		if (client != null) {
			client.startSpan(SQL_STR);
			String psql = getSql(interceptedStatement);
			if (psql != null) {
				client.sendBinaryAnnotation(PRE_SQL_STR, psql);
			}
		}
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
		if (client != null) {
			String psql = getSql(interceptedStatement);
			if (psql != null) {
				client.sendBinaryAnnotation(POST_SQL_STR, psql);
			}
			if (statementException != null) {
				client.sendBinaryAnnotation("error", statementException.getMessage());
			}
			client.finishSpan();
		}
		return null;
	}

	private String getSql(Statement arg1) {
		String sql = null;
		if (arg1 instanceof PreparedStatement) {
			try {
				sql = ((PreparedStatement) arg1).asSql();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sql;
	}

}