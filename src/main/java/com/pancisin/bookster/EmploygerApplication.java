package com.pancisin.bookster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@EnableAutoConfiguration
public class
EmploygerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmploygerApplication.class, args);
	}
}
