package com.preapm.agent.weave;

import java.util.List;

import com.preapm.agent.constant.BaseConstants;

public class ClassWrapperSpringZipkin extends ClassWrapper {

	public String beforAgent() {
		StringBuilder builder = new StringBuilder();
		builder.append(" org.springframework.cloud.sleuth.Span newSpan = null;\r\n"
				+ "		org.springframework.cloud.sleuth.Tracer tracer  = null;");
		return builder.toString();
	}

	public String doAgent() {
		StringBuilder builder = new StringBuilder();
		builder.append(
				" tracer = com.dominos.cloud.common.util.SpringBeanUtils.getBean(org.springframework.cloud.sleuth.Tracer.class);\r\n"
						+ "		if (tracer != null) {\r\n"
						+ "				org.springframework.cloud.sleuth.Span currentSpan = tracer.getCurrentSpan();\r\n"
						+ "				newSpan = tracer.createSpan(\"testMethod\", currentSpan);\r\n"
						+ "				newSpan.tag(\"time\", \"time\");\r\n"
						+ "				 System.out.println(\"加入span成功：\");\r\n" + "			}");
		return builder.toString();
	}

	public String doAgent(String methodName, List<String> argNameList) {
		StringBuilder builder = new StringBuilder();
		builder.append(
				" tracer = com.dominos.cloud.common.util.SpringBeanUtils.getBean(org.springframework.cloud.sleuth.Tracer.class);\r\n"
						+ "		if (tracer != null) {\r\n"
						+ "				org.springframework.cloud.sleuth.Span currentSpan = tracer.getCurrentSpan();\r\n"
						+ "				newSpan = tracer.createSpan(" + toStr(methodName) + ", currentSpan);\r\n"
						+ " System.out.println(\"加入span成功：\"+" + toStr(methodName) + ");\r\n" + "			}");

		if (argNameList != null) {
			for (int i = 0; i < argNameList.size(); i++) {
				String arg = argNameList.get(i);
				builder.append(
						"newSpan.tag(" + toStr("in." + arg) + ", " + "String.valueOf($args[" + i + "])" + ");\r\n");
				builder.append(
						" System.out.println(\"参数名称:\"+" + toStrto(arg) + "\"参数值：\"+" + "$args[" + i + "]" + ");\r\n");
			}
		}

		return builder.toString();
	}

	public String afterAgent() {
		return afterAgent(null);
	}

	public String afterAgent(String resultName) {
		StringBuilder builder = new StringBuilder();
		if (resultName != null) {
			builder.append(" newSpan.tag(\"out\", " + "String.valueOf(" + resultName + ")" + ");\r\n");
			builder.append(" System.out.println(\"返回值：\"+" + resultName + ");\r\n");
		}

		builder.append(" if (newSpan != null && tracer!=null) {\r\n"
				+ "				newSpan.logEvent(org.springframework.cloud.sleuth.Span.CLIENT_RECV);\r\n"
				+ "				tracer.close(newSpan);\r\n" + "			}");
		return builder.toString();
	}

	@Override
	public String doError(String error) {
		return BaseConstants.NULL;
	}

}