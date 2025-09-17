package com.sheffieldcorwin.analyticstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnalyticsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsTrackerApplication.class, args);
	}

}
