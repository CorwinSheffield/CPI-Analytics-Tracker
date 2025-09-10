package com.sheffieldcorwin.analyticstracker.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Sector {

	@Id
	private String seriesId;
	private String name;
	
	@OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<InflationData> dataPoints = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSeriesId() {
		return seriesId;
	}

	public void setSeriesId(String seriesId) {
		this.seriesId = seriesId;
	}

	public Set<InflationData> getDataPoints() {
		return dataPoints;
	}

	public void setDataPoints(Set<InflationData> dataPoints) {
		this.dataPoints = dataPoints;
	}

}
