package com.sheffieldcorwin.analyticstracker.entity;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Entity
public class InvalidTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String rawData;
	private String reason;
	
	private LocalDateTime importedAt;

	public String getRawData() {
		return rawData;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getImportedAt() {
		return importedAt;
	}

	public void setImportedAt(LocalDateTime importedAt) {
		this.importedAt = importedAt;
	}

	public Long getId() {
		return id;
	}
}
