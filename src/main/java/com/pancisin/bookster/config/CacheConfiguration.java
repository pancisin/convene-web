package com.pancisin.bookster.config;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {

	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();

		cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("articles"), new ConcurrentMapCache("articlesLists"), new ConcurrentMapCache("events"),
				new ConcurrentMapCache("notifications"), new ConcurrentMapCache("pages"),
				new ConcurrentMapCache("enviroment")));

		return cacheManager;
	}
}
