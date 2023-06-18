package com.pyonsnalcolor.alarm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.pyonsnalcolor.domain"})
@EnableJpaRepositories(basePackages = {"com.pyonsnalcolor.domain"})
@SpringBootApplication
public class AlarmApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlarmApplication.class, args);
	}

}
