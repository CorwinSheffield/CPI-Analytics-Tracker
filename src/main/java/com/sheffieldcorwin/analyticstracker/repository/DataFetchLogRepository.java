package com.sheffieldcorwin.analyticstracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sheffieldcorwin.analyticstracker.entity.DataFetchLog;

public interface DataFetchLogRepository extends JpaRepository<DataFetchLog, Long> {

	@Query(nativeQuery = true, 
			value = """
					SELECT *
					FROM FINANCE.DATA_FETCH_LOG D1
					WHERE
					D1.SUCCESS = TRUE
					AND FETCH_TIME = (
						SELECT
							MAX(D2.FETCH_TIME) from finance.data_fetch_log D2 where d2.series_id = D1.series_id
					) AND D1.series_id IN :seriesIds
					""")
	List<DataFetchLog> findLatestSuccess(@Param("seriesIds") List<String> seriesIds);

}
