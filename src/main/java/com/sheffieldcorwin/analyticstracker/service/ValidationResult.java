package com.sheffieldcorwin.analyticstracker.service;

import java.util.List;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;
import com.sheffieldcorwin.analyticstracker.entity.InvalidTransaction;

public class ValidationResult {
	private final List<FinanceTransaction> valid;
	private final List<InvalidTransaction> invalid;
	
	public ValidationResult(List<FinanceTransaction> valid, List<InvalidTransaction> invalid) {
		this.valid = valid;
		this.invalid = invalid;
	}

	public List<FinanceTransaction> getValid() {
		return valid;
	}

	public List<InvalidTransaction> getInvalid() {
		return invalid;
	}
	
}
