package com.preapm.agent.plugin.interceptor.filter;

import java.sql.Types;

import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.PreparedStatementProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.util.JdbcUtils;
import com.preapm.sdk.zipkin.ZipkinClient;
import com.preapm.sdk.zipkin.ZipkinClientContext;

public class TracerDruidFilter extends FilterEventAdapter {

	public static final String SQL_STR_KEY = "sql.sql";

	@Override
	protected void statementExecuteBefore(StatementProxy statement, String sql) {
		outLog(sql);
		logParameter(statement);
		super.statementExecuteBefore(statement, sql);
	}

	@Override
	protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
		// TODO Auto-generated method stub
		outLog(sql);
		logParameter(statement);
		super.statementExecuteUpdateBefore(statement, sql);
	}

	@Override
	protected void statementExecuteQueryBefore(StatementProxy statement, String sql) {
		// TODO Auto-generated method stub
		outLog(sql);
		logParameter(statement);
		super.statementExecuteQueryBefore(statement, sql);
	}

	protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
		String sql;
		if (statement instanceof PreparedStatementProxy) {
			sql = ((PreparedStatementProxy) statement).getSql();
		} else {
			sql = statement.getBatchSql();
		}
		outLog(sql);
		super.statementExecuteQueryBefore(statement, sql);
	}

	private void logParameter(StatementProxy statement) {

		if (!(statement instanceof PreparedStatementProxy)) {
			return;
		}

		{
			StringBuffer buf = new StringBuffer();
			buf.append(" Parameters : [");

			for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
				JdbcParameter parameter = statement.getParameter(i);
				if (i != 0) {
					buf.append(", ");
				}
				if (parameter == null) {
					continue;
				}

				int sqlType = parameter.getSqlType();
				Object value = parameter.getValue();
				switch (sqlType) {
				case Types.NULL:
					buf.append("NULL");
					break;
				default:
					buf.append(String.valueOf(value));
					break;
				}
			}
			buf.append("]");
			outLog("sql.Parameters", buf.toString());
		}
		{
			StringBuffer buf = new StringBuffer();
			buf.append(" Types : [");
			for (int i = 0, parametersSize = statement.getParametersSize(); i < parametersSize; ++i) {
				JdbcParameter parameter = statement.getParameter(i);
				if (i != 0) {
					buf.append(", ");
				}
				if (parameter == null) {
					continue;
				}
				int sqlType = parameter.getSqlType();
				buf.append(JdbcUtils.getTypeName(sqlType));
			}
			buf.append("]");
			outLog("sql.Types", buf.toString());
		}
	}

	private void outLog(String sql) {
		outLog(SQL_STR_KEY, sql);
	}

	private void outLog(String key, String sql) {
		ZipkinClient tracer = ZipkinClientContext.getClient();
		if (tracer == null) {
			return;
		}

		//tracer.startSpan("druid");
		if (tracer != null) {
			tracer.sendBinaryAnnotation(key, sql);
		}
		//tracer.finishSpan();
	}
}