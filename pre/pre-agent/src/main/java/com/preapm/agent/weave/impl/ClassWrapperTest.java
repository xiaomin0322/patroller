package com.preapm.agent.weave.impl;

import java.util.List;

import com.preapm.agent.constant.BaseConstants;
import com.preapm.agent.weave.ClassWrapper;

public class ClassWrapperTest extends ClassWrapper {


	public String beforAgent(String methodName, List<String> argNameList) {
		StringBuilder builder = new StringBuilder();
		builder.append(" zzm.test.JoddTest j = new zzm.test.JoddTest(); " + " j.get();");
		return builder.toString();
	}

	public String afterAgent() {
		return afterAgent(null);
	}

	public String afterAgent(String resultName) {
		return BaseConstants.NULL;
	}

	@Override
	public String doError(String error) {
		return BaseConstants.NULL;
	}

}