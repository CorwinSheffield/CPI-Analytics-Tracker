package com.sheffieldcorwin.analyticstracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.sheffieldcorwin.analyticstracker.dto.CPIDataPoint;
import com.sheffieldcorwin.analyticstracker.dto.CPISeries;
import com.sheffieldcorwin.analyticstracker.dto.CPISeriesResults;
import com.sheffieldcorwin.analyticstracker.entity.InflationData;
import com.sheffieldcorwin.analyticstracker.entity.Sector;
import com.sheffieldcorwin.analyticstracker.repository.InflationDataRepository;

@Component
public class CPITransformer {
	
	
	private final InflationDataRepository dataRepo;

	public CPITransformer(InflationDataRepository dataRepo) {
		this.dataRepo = dataRepo;
	}
	
	public List<InflationData> transform(CPISeriesResults results, Sector sector){
		return transform(results, Map.of(sector.getSeriesId(),sector));
	}

	public List<InflationData> transform(CPISeriesResults results, Map<String,Sector> sectorMap) {
		List<InflationData> dataPoints = new ArrayList<>();
		for (CPISeries series : results.series()) {
			Sector sector = sectorMap.get(series.seriesID());
			if (sector != null) {
				List<LocalDate> existingKeys = dataRepo.findExistingKeys(sector.getSeriesId());
				for (CPIDataPoint dataPoint : series.data()) {
					InflationData data = buildEntity(dataPoint, sector);
					if (!existingKeys.contains(data.getPeriod())) {
						dataPoints.add(data);
					}
				}
			}
		}
		return dataPoints;
	}

	public InflationData buildEntity(CPIDataPoint dataPoint, Sector sector) {
		InflationData data = new InflationData();
		data.setSector(sector);
		data.setPeriod(calculatePeriod(dataPoint));
		data.setValue(new BigDecimal(dataPoint.value()));
		return data;
	}

	private LocalDate calculatePeriod(CPIDataPoint data) {
		int month = Integer.valueOf(data.period().substring(1));
		int year = Integer.valueOf(data.year());
		return LocalDate.of(year, month, 1);
	}

}
