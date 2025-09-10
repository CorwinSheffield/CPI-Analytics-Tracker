package com.sheffieldcorwin.analyticstracker.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "data_fetch_log")
public class DataFetchLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDateTime fetchTime;
	private String seriesId;
	private boolean success;
	private String errorMessage;
	
	
	public LocalDateTime getFetchTime() {
		return fetchTime;
	}
	public void setFetchTime(LocalDateTime fetchTime) {
		this.fetchTime = fetchTime;
	}
	public String getSeriesId() {
		return seriesId;
	}
	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Long getId() {
		return id;
	}
	
}
