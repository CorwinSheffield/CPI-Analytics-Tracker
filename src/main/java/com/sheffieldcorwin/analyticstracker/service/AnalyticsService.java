package com.sheffieldcorwin.analyticstracker.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.controller.AnalyticsController.SectorDTO;
import com.sheffieldcorwin.analyticstracker.entity.InflationData;
import com.sheffieldcorwin.analyticstracker.entity.Sector;
import com.sheffieldcorwin.analyticstracker.repository.InflationDataRepository;
import com.sheffieldcorwin.analyticstracker.repository.SectorRepository;

@Service
public class AnalyticsService {

	private static final Logger log = LogManager.getLogger();

	private final SectorRepository sectorRepository;
	private final InflationDataRepository dataRepo;

	public AnalyticsService(InflationDataRepository dataRepo, SectorRepository sectorRepository) {
		this.dataRepo = dataRepo;
		this.sectorRepository = sectorRepository;
	}

	public BigDecimal calculateYoYInflation(String seriesId) {
		List<InflationData> last13Months = dataRepo.findTop13BySector_seriesIdOrderByPeriodDesc(seriesId);
		return calculateYoYInflation(last13Months);
	}

	public BigDecimal calculateAverageMonthlyInflation(String seriesId, int months) {
		Pageable limit = PageRequest.of(0, months + 1);
		List<InflationData> data = dataRepo.findBySector_seriesIdOrderByPeriodDesc(seriesId, limit);
		return calculateAverageMonthlyInflation(data);

	}

	public BigDecimal calculateMonthOverMonthChange(String seriesId) {
		log.info("Series ID: {}", seriesId);
		List<InflationData> data = dataRepo.findTop2BySector_seriesIdOrderByPeriodDesc(seriesId);
		return calculateMonthOverMonthChange(data);
	}

	public BigDecimal calculateCumulativeInflation(String seriesId, int months) {
		Pageable limit = PageRequest.of(0, months);
		List<InflationData> data = dataRepo.findBySector_seriesIdOrderByPeriodDesc(seriesId, limit);
		return calculateCumulativeInflation(data);
	}

	public List<SectorDTO> fuzzySearch(String q) {
		List<Sector> sectors = sectorRepository.searchByNameFullText(q);
		List<SectorDTO> dtos = new ArrayList<>();
		for (Sector sector : sectors) {
			List<InflationData> data = fetchTransactions(sector.getSeriesId());
			data.stream().forEach(i -> {
				log.info(i.getValue());
			});
			SectorDTO dto = new SectorDTO(sector.getSeriesId(), sector.getName(), calculateYoYInflation(data),
					calculateMonthOverMonthChange(data), calculateVolatility(data));
			dtos.add(dto);
		}

		return dtos;
	}

	private List<InflationData> fetchTransactions(String seriesId) {
		return dataRepo.findTop13BySector_seriesIdOrderByPeriodDesc(seriesId);
	}

	private BigDecimal calculateCumulativeInflation(List<InflationData> data) {
		if (data.size() < 2)
			return BigDecimal.ZERO;

		BigDecimal latest = data.get(0).getValue();
		BigDecimal earliest = data.get(data.size() - 1).getValue();

		BigDecimal change = latest.subtract(earliest).divide(earliest, 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
		return change;
	}

	private BigDecimal calculateMonthOverMonthChange(List<InflationData> data) {
		InflationData dataPointOne;
		InflationData dataPointTwo;
		if (data.size() >= 2) {
			dataPointOne = data.get(0);
			dataPointTwo = data.get(1);
		} else {
			return BigDecimal.ZERO;
		}
		BigDecimal latest = dataPointOne.getValue();
		BigDecimal previous = dataPointTwo.getValue();
		BigDecimal change = latest.subtract(previous).divide(previous, 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
		return change;
	}

	private BigDecimal calculateAverageMonthlyInflation(List<InflationData> data) {
		if (data.size() <= 1)
			return BigDecimal.ZERO;
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < data.size() - 1; i++) {
			BigDecimal change = data.get(i).getValue().subtract(data.get(i + 1).getValue())
					.divide(data.get(i + 1).getValue(), 4, RoundingMode.HALF_UP);
			sum = sum.add(change);
		}
		BigDecimal change = sum.divide(BigDecimal.valueOf(data.size() - 1), 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
		return change;
	}

	private BigDecimal calculateYoYInflation(List<InflationData> last13Months) {
		if (last13Months.size() < 13)
			return BigDecimal.ZERO;
		BigDecimal latest = last13Months.get(0).getValue();
		BigDecimal value12MonthsAgo = last13Months.get(12).getValue();
		BigDecimal change = latest.subtract(value12MonthsAgo).divide(value12MonthsAgo, 4, RoundingMode.HALF_UP)
				.multiply(BigDecimal.valueOf(100));
		return change;
	}

	public BigDecimal calculateVolatility(List<InflationData> data) {
		List<BigDecimal> changes = new ArrayList<>();
		for (int i = 0; i < data.size() - 1; i++) {
			BigDecimal change = calculateMonthOverMonthChange(data.subList(i, data.size()));
			changes.add(change);
		}

		BigDecimal sum = changes.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal mean = sum.divide(BigDecimal.valueOf(changes.size()), 4, RoundingMode.HALF_UP);

		List<BigDecimal> squaredChanges = new ArrayList<>();
		for (BigDecimal change : changes) {
			BigDecimal difference = change.subtract(mean);
			difference = difference.multiply(difference);
			squaredChanges.add(difference);
		}

		BigDecimal variance = squaredChanges.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(squaredChanges.size()), 4, RoundingMode.HALF_UP);
		BigDecimal standardDeviation = variance.sqrt(new MathContext(4, RoundingMode.HALF_UP));
		return standardDeviation;
	}

}
