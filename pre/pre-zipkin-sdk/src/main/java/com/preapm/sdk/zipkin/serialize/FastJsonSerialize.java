package com.preapm.sdk.zipkin.serialize;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


import com.alibaba.fastjson.JSONObject;

public class FastJsonSerialize implements SerializeInterface{

	
	
	@Override
	public String serializa(Object object) {
		if(object == null) {
			return null;
		}
		String className = object.getClass().getName();
		if("javax.servlet.ServletRequest".equals(className) || "javax.servlet.ServletResponse".equals(className) ||
				"org.springframework.web.multipart.MultipartFile".equals(className)) {
			return className;
		}
		if (object instanceof ServletRequest || object instanceof ServletResponse) {
			return className;
		}
		return JSONObject.toJSONString(object);
	}

}
