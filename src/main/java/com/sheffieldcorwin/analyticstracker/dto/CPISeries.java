package com.sheffieldcorwin.analyticstracker.dto;

import java.util.List;

public record CPISeries(
		
		String seriesID, List<CPIDataPoint> data) {

}
