package com.sheffieldcorwin.analyticstracker.dto;

import java.util.List;

public record CPIDataPoint(String year, 
		String period, 
		String periodName, 
		String value,
		List<Footnote> footnotes
		) {

}
