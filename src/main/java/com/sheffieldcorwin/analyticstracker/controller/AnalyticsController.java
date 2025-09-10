package com.sheffieldcorwin.analyticstracker.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sheffieldcorwin.analyticstracker.service.AnalyticsService;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
	
	private final AnalyticsService analyticsService;
	
	public AnalyticsController(AnalyticsService analyticsService) {
		this.analyticsService = analyticsService;
	}
	
	@GetMapping("/m-o-m/{seriesId}")
	public BigDecimal monthOverMonthInflation(@PathVariable String seriesId) {
		return analyticsService.calculateMonthOverMonthChange(seriesId);
	}
	
	@GetMapping("/y-o-y/{seriesId}")
	public BigDecimal getYoY(@PathVariable String seriesId) {
		return analyticsService.calculateYoYInflation(seriesId);
	}
	
	@GetMapping("/average/{seriesId}")
	public BigDecimal getAverage(@PathVariable String seriesId, @RequestParam(defaultValue = "12") int months) {
		return analyticsService.calculateAverageMonthlyInflation(seriesId, months);
	}
	
	@GetMapping("/cumulative/{seriesId}")
	public BigDecimal getCumulative(@PathVariable String seriesId, @RequestParam(defaultValue = "12") int months) {
		return analyticsService.calculateCumulativeInflation(seriesId, months);
	}
	
	@GetMapping("/search")
	public List<SectorDTO> searchSectors(@RequestParam String q){
		q = q.toLowerCase();
		return analyticsService.fuzzySearch(q);		
	}
	
	public record SectorDTO(String seriesId, String name, BigDecimal yoYInflation, BigDecimal moMInflation, BigDecimal standardDeviationForTheYear) {}

}
