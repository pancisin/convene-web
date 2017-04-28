package com.pancisin.employger.utils;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnviromentProvider implements EnvironmentAware {
	private static Environment environment;

	@Cacheable("EnvironmentProvider.isProduction")
	public Boolean isProduction() {
		return environment.acceptsProfiles("prod");
	}

	@Cacheable("EnvironmentProvider.isDevelopment")
	public Boolean isDevelopment() {
		return environment.acceptsProfiles("local");
	}

	@Override
	public void setEnvironment(Environment environment) {
		EnviromentProvider.environment = environment;
	}
}
