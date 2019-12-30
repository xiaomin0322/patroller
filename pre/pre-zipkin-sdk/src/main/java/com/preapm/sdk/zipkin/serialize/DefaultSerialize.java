package com.preapm.sdk.zipkin.serialize;

public class DefaultSerialize implements SerializeInterface{

	@Override
	public String serializa(Object object) {
		return String.valueOf(object);
	}

}
