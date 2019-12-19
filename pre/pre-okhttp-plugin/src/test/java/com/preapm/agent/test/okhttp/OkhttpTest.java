package com.preapm.agent.test.okhttp;

import com.preapm.agent.plugin.interceptor.filter.OkHttpFilter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpTest {

	public static void main(String[] args) throws Exception {
		OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new OkHttpFilter()).build();

		Request request = new Request.Builder().url("https://www.cnblogs.com/liyutian/p/9489016.html")
				.header("User-Agent", "OkHttp Example").build();

		Response response = client.newCall(request).execute();
		String s = response.body().byteString().toString();
		System.out.println(s);
	}

}
