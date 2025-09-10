package com.sheffieldcorwin.analyticstracker.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CPIApiResponse(
		String status,
		int responseTime,
		List<String> message,
		@JsonProperty("Results") CPISeriesResults results) 
{
	
}
