package com.sheffieldcorwin.analyticstracker.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sheffieldcorwin.analyticstracker.dto.CPISeriesResults;
import com.sheffieldcorwin.analyticstracker.entity.InflationData;
import com.sheffieldcorwin.analyticstracker.entity.Sector;
import com.sheffieldcorwin.analyticstracker.repository.InflationDataRepository;
import com.sheffieldcorwin.analyticstracker.repository.SectorRepository;

@Service
public class CPIService {

	private final CPIClient cpiClient;
	private final InflationDataRepository dataRepository;
	private final SectorRepository sectorRepo;
	private final CPITransformer transformer;

	public CPIService(CPIClient cpiClient, InflationDataRepository dataRepository, SectorRepository sectorRepo, CPITransformer transformer) {
		this.cpiClient = cpiClient;
		this.dataRepository = dataRepository;
		this.sectorRepo = sectorRepo;
		this.transformer = transformer;
	}

	public List<String> fetchAvailableSeriesByName() {
		return sectorRepo.findAll().stream().map(s -> s.getName()).toList();
	}
	
	public List<String> fetchAvailableSeriesBySeriesId(){
		return sectorRepo.findAll().stream().map(s -> s.getSeriesId()).toList();
	}

	@Transactional
	public SaveResult fetchSingleSeries(String seriesId) {
		CPISeriesResults results = cpiClient.fetchSingleSeriesData(seriesId);
		Sector sector = sectorRepo.findBySeriesId(seriesId)
				.orElseThrow(() -> new IllegalArgumentException("No sector for seriesId: " + seriesId));
		List<InflationData> dataList = transformer.transform(results, sector);
		List<InflationData> saved = dataRepository.saveAll(dataList);
		return new SaveResult(sector.getName(), saved.size());
	}

	@Transactional
	public SaveResult fetchSingleSeriesLatest(String seriesId) {
		CPISeriesResults results = cpiClient.fetchLatestSingleSeriesData(seriesId).block();
		Sector sector = sectorRepo.findBySeriesId(seriesId)
				.orElseThrow(() -> new IllegalArgumentException("No sector for seriesId: " + seriesId));
		List<InflationData> dataList = transformer.transform(results, sector);
		List<InflationData> saved = dataRepository.saveAll(dataList);
		return new SaveResult(sector.getName(), saved.size());
	}

	public SaveResult fetchSingleSeriesWithParameter(String seriesId, int startYear, int endYear) {
		CPISeriesResults results = cpiClient.fetchSingleSeriesData(seriesId, startYear, endYear);
		Sector sector = sectorRepo.findBySeriesId(seriesId)
				.orElseThrow(() -> new IllegalArgumentException("No sector for seriesId: " + seriesId));
		List<InflationData> dataList = transformer.transform(results, sector);
		List<InflationData> saved = dataRepository.saveAll(dataList);
		return new SaveResult(sector.getName(), saved.size());
	}

	@Transactional
	public SaveResult fetchMultipleSeries() {
		List<Sector> sectors = sectorRepo.findAll();
		Map<String, Sector> sectorMap = sectors.stream().collect(Collectors.toMap(Sector::getSeriesId, s -> s));
		System.out.println("Series ids: " + sectorMap.keySet());
		CPISeriesResults results = cpiClient.fetchMultipleSeries(List.copyOf(sectorMap.keySet())).block();
		List<InflationData> dataPoints = transformer.transform(results, sectorMap);
		List<InflationData> saved = dataRepository.saveAll(dataPoints);
		return new SaveResult("Multiple", saved.size());
	}

	@Transactional
	public SaveResult fetchMultipleSeries(int startYear, int endYear) {
		List<Sector> sectors = sectorRepo.findAll();
		Map<String, Sector> sectorMap = sectors.stream().collect(Collectors.toMap(Sector::getSeriesId, s -> s));
		System.out.println("Series ids: " + sectorMap.keySet());
		CPISeriesResults results = cpiClient.fetchMultipleSeries(List.copyOf(sectorMap.keySet()), startYear, endYear)
				.block();
		List<InflationData> dataPoints = transformer.transform(results, sectorMap);
		List<InflationData> saved = dataRepository.saveAll(dataPoints);
		return new SaveResult("Multiple",  saved.size());
	}
	
	public record SaveResult(String sectorName, int recordsSaved) {}
}
