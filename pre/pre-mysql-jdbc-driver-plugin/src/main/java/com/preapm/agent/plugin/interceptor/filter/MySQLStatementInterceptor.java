package com.preapm.agent.plugin.interceptor.filter;

import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.ResultSetInternalMethods;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.StatementInterceptorV2;

public class MySQLStatementInterceptor implements StatementInterceptorV2 {
	private static final Logger logger = LoggerFactory.getLogger(MySQLStatementInterceptor.class);

	@Override
	public void init(Connection conn, Properties props) throws SQLException {
		// TODO Auto-generated method stub

	}

	@Override
	public ResultSetInternalMethods preProcess(String sql, Statement interceptedStatement, Connection connection)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean executeTopLevelOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public ResultSetInternalMethods postProcess(String sql, Statement interceptedStatement,
			ResultSetInternalMethods originalResultSet, Connection connection, int warningCount, boolean noIndexUsed,
			boolean noGoodIndexUsed, SQLException statementException) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}