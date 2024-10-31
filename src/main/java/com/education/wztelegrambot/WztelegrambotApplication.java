package com.education.wztelegrambot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WztelegrambotApplication {

	public static void main(String[] args) {
		SpringApplication.run(WztelegrambotApplication.class, args);
	}

}
