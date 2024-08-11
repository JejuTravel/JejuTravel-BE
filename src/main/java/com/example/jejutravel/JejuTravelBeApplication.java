package com.example.jejutravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class JejuTravelBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(JejuTravelBeApplication.class, args);
	}

}
