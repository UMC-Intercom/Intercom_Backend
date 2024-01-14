package com.umc.intercom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class IntercomApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntercomApplication.class, args);
	}

}
