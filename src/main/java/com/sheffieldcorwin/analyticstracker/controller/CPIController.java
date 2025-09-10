package com.sheffieldcorwin.analyticstracker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sheffieldcorwin.analyticstracker.service.CPIService;
import com.sheffieldcorwin.analyticstracker.service.CPIService.SaveResult;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("manualRequest")
public class CPIController {

	private final CPIService cpiService;

	public CPIController(CPIService cpiService) {
		this.cpiService = cpiService;
	}

	@GetMapping("/availableSeriesByName")
	public List<String> fetchAvailableSeriesByName(){
		return cpiService.fetchAvailableSeriesByName();
	}
	
	@GetMapping("/availableSeriesById")
	public List<String> fetchAvailableSeriesBySeriesId(){
		return cpiService.fetchAvailableSeriesBySeriesId();
	}
	
	@GetMapping("/fetch")
	public SaveResult fetchData(@RequestParam String seriesId) {

		SaveResult result = cpiService.fetchSingleSeries(seriesId);
		return result;
	}

	@GetMapping("/fetchLatest")
	public SaveResult fetchLatestData(@RequestParam String seriesId) {
		SaveResult result = cpiService.fetchSingleSeriesLatest(seriesId);
		return result;
	}

	@GetMapping("/fetchAll")
	public Mono<SaveResult> fetchMultipleSeries() {
		return Mono.fromCallable(() -> cpiService.fetchMultipleSeries()).subscribeOn(Schedulers.boundedElastic());

	}
	
	@GetMapping("/fetchAllFromYears")
	public Mono<SaveResult> fetchMultipleSeries(@RequestParam int startYear, @RequestParam int endYear) {
		return Mono.fromCallable(() -> cpiService.fetchMultipleSeries(startYear, endYear)).subscribeOn(Schedulers.boundedElastic());
	}

	@GetMapping("/fetchAllFromYears/{seriesId}")
	public Mono<SaveResult> fetchMultipleSeriesWithUniqueId(@PathVariable String seriesId, @RequestParam int startYear, @RequestParam int endYear) {
		return Mono.fromCallable(() -> cpiService.fetchSingleSeriesWithParameter(seriesId, startYear, endYear)).subscribeOn(Schedulers.boundedElastic());
	}
}
