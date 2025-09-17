package com.sheffieldcorwin.analyticstracker.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.entity.DataFetchLog;
import com.sheffieldcorwin.analyticstracker.entity.Sector;
import com.sheffieldcorwin.analyticstracker.repository.DataFetchLogRepository;
import com.sheffieldcorwin.analyticstracker.repository.SectorRepository;
import com.sheffieldcorwin.analyticstracker.service.CPIService.SaveResult;

@Service
public class DataIngestionRunner {

	private static final Logger log = LogManager.getLogger();
	private final ExecutorService executor = Executors.newFixedThreadPool(5);
	private final SectorRepository sectorRepo;
	private final CPIService cpiService;
	private final DataFetchLogRepository fetchLogRepo;
	private final Clock clock;

	public DataIngestionRunner(SectorRepository sectorRepo, CPIService cpiService,
			DataFetchLogRepository fetchLogRepo, Clock clock) {
		this.sectorRepo = sectorRepo;
		this.cpiService = cpiService;
		this.fetchLogRepo = fetchLogRepo;
		this.clock = clock;

	}
	
	public void run() {
		List<Sector> sectors = sectorRepo.findAll();
		Map<String, DataFetchLog> fetchLogs = fetchLogRepo
				.findLatestSuccess(sectors.stream().map(Sector::getSeriesId).toList()).stream()
				.collect(Collectors.toMap(DataFetchLog::getSeriesId, s -> s));
		
		List<Sector> sectorsToFetch = sectors.stream().filter(sector -> {
			DataFetchLog fetchLog = fetchLogs.get(sector.getSeriesId());
			return fetchLog == null || !fetchLog.getFetchTime().toLocalDate().isEqual(LocalDate.now(clock));
		}).toList();
		
		List<CompletableFuture<SaveResult>> futures = sectorsToFetch.stream().map(sector -> CompletableFuture.supplyAsync(() -> {
			try {
				SaveResult result = cpiService.fetchSingleSeriesLatest(sector.getSeriesId());
				logFetch(sector.getSeriesId(), true, "");
				return result;
			} catch (Exception e) {
				log.error("Failed to fetch data for seriesId {}: {} ", sector.getSeriesId(), e.getMessage());
				logFetch(sector.getSeriesId(), false, e.getMessage());
				return new SaveResult(sector.getSeriesId(), 0);
			}
		}, executor)).toList();
		futures.forEach(t -> t.thenAccept(t1 -> log.info("Fetched {}: {} records saved", t1.sectorName(), t1.recordsSaved())));
	}

	private void logFetch(String seriesId, boolean success, String message) {
		DataFetchLog logItem = new DataFetchLog();
		logItem.setSuccess(success);
		logItem.setFetchTime(LocalDateTime.now(clock));
		logItem.setSeriesId(seriesId);
		logItem.setErrorMessage(message);
		fetchLogRepo.save(logItem);
	}
}
