package com.sheffieldcorwin.analyticstracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;

public interface FinanceTransactionRepository extends JpaRepository<FinanceTransaction, Long>{
	
}
