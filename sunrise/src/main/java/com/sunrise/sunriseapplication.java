package com.sunrise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SunriseApplication {

	public static void main(String[] args) {
		log.info("Main Application started !");
		SpringApplication.run(SunriseApplication.class, args);
	}

}
