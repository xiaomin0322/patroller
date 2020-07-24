package com.preapm.agent.enums;

public enum PatternEnum {
	Around("around", "拦截器"), Class("class", "修改class"),ALL("all", "所有类型");

	private String code;
	private String msg;

	PatternEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static PatternEnum getEnum(String msg) {
		PatternEnum resultEnum = null;
		PatternEnum[] enumArray = PatternEnum.values();

		for (int i = 0; i < enumArray.length; i++) {
			if (enumArray[i].getMsg().equals(msg)) {
				return enumArray[i];
			}
		}
		return resultEnum;
	}

	public static PatternEnum getEnumByCode(String code) {
		PatternEnum resultEnum = null;
		PatternEnum[] enumArray = PatternEnum.values();

		for (int i = 0; i < enumArray.length; i++) {
			if (enumArray[i].getCode().equals(code)) {
				return enumArray[i];
			}
		}
		return resultEnum;
	}
}
