package com.sheffieldcorwin.analyticstracker.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;
import com.sheffieldcorwin.analyticstracker.entity.InvalidTransaction;
import com.sheffieldcorwin.analyticstracker.repository.FinanceTransactionRepository;
import com.sheffieldcorwin.analyticstracker.repository.InvalidTransactionRepository;

@Service
public class FinanceTransactionService {

	private final FinanceTransactionRepository validRepository;
	private final InvalidTransactionRepository invalidRepository;
	private final TransactionValidationService validator;
	private final TransformationService transformer;
	private final CategorizationService categorizer;

	public FinanceTransactionService(FinanceTransactionRepository validRepository, TransactionValidationService validator, TransformationService transformer, CategorizationService categorizer, InvalidTransactionRepository invalidRepository) {
		this.validRepository = validRepository;
		this.invalidRepository = invalidRepository;
		this.validator = validator;
		this.transformer = transformer;
		this.categorizer = categorizer;
		
	}

	
	public ValidationResult processTransactions(List<FinanceTransaction> transactions) {
		ValidationResult result = validator.validate(transactions);
		List<FinanceTransaction> valid = result.getValid();
		valid.forEach(transformer::transform);
		valid.forEach(categorizer::categorize);
		validRepository.saveAll(valid);
		invalidRepository.saveAll(result.getInvalid());
		return result;
	}
	
	public List<FinanceTransaction> getAllTransactions(){
		return validRepository.findAll();
	}
	
	public List<InvalidTransaction> getAllInvalidTransactions(){
		return invalidRepository.findAll();
	}

}
