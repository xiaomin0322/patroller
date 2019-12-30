package com.preapm.sdk.zipkin.serialize;

public class SerializeFactory {

	public static SerializeInterface def = new DefaultSerialize();

	public static SerializeInterface fastjson = new DefaultSerialize();

	public static SerializeInterface get(String name) {
		if ("fastjson".equalsIgnoreCase(name)) {
			return fastjson;
		}
		return def;
	}

}
