package com.pancisin.bookster.utils;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnviromentProvider implements EnvironmentAware {
	private static Environment environment;

	@Cacheable("enviroment")
	public Boolean isProduction() {
		return environment.acceptsProfiles("production");
	}

	@Cacheable("enviroment")
	public Boolean isDevelopment() {
		return environment.acceptsProfiles("local");
	}

	@Override
	public void setEnvironment(Environment environment) {
		EnviromentProvider.environment = environment;
	}
}
