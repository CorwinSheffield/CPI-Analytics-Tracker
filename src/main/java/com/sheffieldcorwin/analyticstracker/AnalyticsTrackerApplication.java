package com.sheffieldcorwin.analyticstracker;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;
import com.sheffieldcorwin.analyticstracker.service.CSVImportService;
import com.sheffieldcorwin.analyticstracker.service.FinanceTransactionService;
import com.sheffieldcorwin.analyticstracker.service.TransactionGeneratorService;
import com.sheffieldcorwin.analyticstracker.service.ValidationResult;

@SpringBootApplication
public class AnalyticsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnalyticsTrackerApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runAtStartup(TransactionGeneratorService generatorService, CSVImportService csvImportService, FinanceTransactionService financeService) {
//
//		return args -> {
//			System.out.println("=== Generating Data...===");
//			generatorService.generateCsv("src/main/resources/data/transactions.csv", 10000, 0.07);
//			System.out.println("=== importing CSV at startup ===");
//			List<FinanceTransaction> importedTransactions = csvImportService
//					.importTransactions("src/main/resources/data/transactions.csv");
//			System.out.println("=== CSV import Complete ===");
//			System.out.println("=== Processing Transactions ===");
//			ValidationResult result = financeService.processTransactions(importedTransactions);
//			System.out.println("=== Processing Complete ===");
//
//			System.out.println("Number of valid transactions: " + result.getValid().size()
//					+ "\nNumber of invalid transactions: " + result.getInvalid().size());
//		};
//	}

}
