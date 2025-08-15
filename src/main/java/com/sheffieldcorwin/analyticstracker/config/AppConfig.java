package com.sheffieldcorwin.analyticstracker.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class AppConfig {
	
	@Value("${obp.api.key}")
	private String apiKey;
	@Value("${obp.api.secret}")
	private String apiSecret;
	@Value("${obp.api.url}")
	private String apiUrl;

}
