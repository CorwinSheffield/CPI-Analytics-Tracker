package com.sheffieldcorwin.analyticstracker.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sheffieldcorwin.analyticstracker.entity.InflationData;

public interface InflationDataRepository extends JpaRepository<InflationData, Long>{

	@Query("Select d.period from InflationData d where d.sector.seriesId = :seriesId")
	List<LocalDate> findExistingKeys(@Param("seriesId") String seriesId);
	
	
	
	List<InflationData> findTop2BySector_seriesIdOrderByPeriodDesc(String seriesId);



	List<InflationData> findTop13BySector_seriesIdOrderByPeriodDesc(String seriesId);



	List<InflationData> findBySector_seriesIdOrderByPeriodDesc(String seriesId, Pageable pageable);

}
