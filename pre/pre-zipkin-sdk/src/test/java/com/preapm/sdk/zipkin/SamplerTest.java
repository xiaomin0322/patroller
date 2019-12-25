package com.preapm.sdk.zipkin;

import com.preapm.sdk.zipkin.sampler.PercentageBasedSampler;
import com.preapm.sdk.zipkin.sampler.Sampler;
import com.preapm.sdk.zipkin.sampler.SamplerProperties;

public class SamplerTest {

	private static Sampler defaultSampler = null;

	public static void main(String[] args) {
		SamplerProperties samplerProperties = new SamplerProperties();
		samplerProperties.setPercentage(0.456f);

		defaultSampler = new PercentageBasedSampler(samplerProperties);

		int count = 0;
		for (int i = 0; i < 10000; i++) {
			if (defaultSampler.isSampled(new Object())) {
				count += 1;
			}
		}
		System.out.println(count);

	}

}
