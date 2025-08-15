package com.sheffieldcorwin.analyticstracker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;
import com.sheffieldcorwin.analyticstracker.entity.InvalidTransaction;
import com.sheffieldcorwin.analyticstracker.service.FinanceTransactionService;

@RestController
public class TransactionController {

	@Autowired
	FinanceTransactionService transactionService;
	
	@GetMapping("/transactions")
	public List<FinanceTransaction> getAllTransactions() {
		return transactionService.getAllTransactions();
	}
	
	@GetMapping("/invalidtransactions")
	public List<InvalidTransaction> getAllInvalidTransactions(){
		return transactionService.getAllInvalidTransactions();
	}
	
}
