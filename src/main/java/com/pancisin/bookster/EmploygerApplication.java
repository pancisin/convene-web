package com.pancisin.bookster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableZuulProxy
//@EnableDiscoveryClient
@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableAutoConfiguration
public class EmploygerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmploygerApplication.class, args);
	}
}
