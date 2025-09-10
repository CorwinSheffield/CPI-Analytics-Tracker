package com.sheffieldcorwin.analyticstracker.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataIngestionScheduler {
	
	private final DataIngestionRunner dataIngestionRunner;
	
	public DataIngestionScheduler(DataIngestionRunner dataIngestionRunner) {
		this.dataIngestionRunner = dataIngestionRunner;
	}
	
	@Scheduled(cron = "0 0 9 15 * *")
	public void fetchLatestDataForAllSectors() {
		dataIngestionRunner.run();
	}
	
}
