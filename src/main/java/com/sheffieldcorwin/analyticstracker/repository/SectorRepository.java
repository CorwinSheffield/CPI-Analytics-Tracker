package com.sheffieldcorwin.analyticstracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sheffieldcorwin.analyticstracker.entity.Sector;

public interface SectorRepository extends JpaRepository<Sector, String>{
	
	Optional<Sector> findBySeriesId(String seriesId);
	
	@Query(value = "SELECT * FROM finance.sector where name_tsv @@ to_tsquery(:term || ':*')", nativeQuery = true)
	List<Sector> searchByNameFullText(@Param("term") String term);
	

	
}
