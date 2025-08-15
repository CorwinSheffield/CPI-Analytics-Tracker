package com.sheffieldcorwin.analyticstracker.service;

import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;

@Service
public class TransformationService {

	
	public void transform(FinanceTransaction transaction) {
		transaction.setAccount(transaction.getAccount().strip());
		transaction.setDescription(transaction.getDescription().strip());
		transaction.setType(transaction.getType().toUpperCase());
		transaction.setCategory(transaction.getCategory().strip().toUpperCase());
	}
}
