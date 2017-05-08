package com.pancisin.bookster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmploygerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmploygerApplication.class, args);
	}
}
