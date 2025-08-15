package com.sheffieldcorwin.analyticstracker.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;
import com.sheffieldcorwin.analyticstracker.entity.InvalidTransaction;

@Service
public class TransactionValidationService {

	public ValidationResult validate(List<FinanceTransaction> transactions) {
		List<FinanceTransaction> valid = new ArrayList<>();
		List<InvalidTransaction> invalid = new ArrayList<>();

		for (FinanceTransaction transaction : transactions) {
			List<String> warnings = new ArrayList<>();
			if (transaction.getDate() == null || transaction.getDate().isAfter(LocalDate.now())) {
				warnings.add("Invalid Date");
			}
			if (transaction.getAccount() == null) {
				warnings.add("Invalid Account Number");
			}
			if (transaction.getType() == null || (!transaction.getType().equalsIgnoreCase("credit")
					&& !transaction.getType().equalsIgnoreCase("debit"))) {
				warnings.add("Invalid Transaction Type");
			}
			if (transaction.getAmount() == null || transaction.getAmount().equals(BigDecimal.ZERO)) {
				warnings.add("Invalid Amount");
			}

			if (transaction.getDescription() == null) {
				transaction.setDescription("");
			}

			if (transaction.getCategory() == null) {
				transaction.setCategory("");
			}

			if (warnings.isEmpty()) {
				valid.add(transaction);
			} else {
				InvalidTransaction bad = new InvalidTransaction();
				bad.setRawData(transaction.toString());
				bad.setReason(String.join(",", warnings));
				bad.setImportedAt(LocalDateTime.now());
				invalid.add(bad);
			}
		}
		return new ValidationResult(valid, invalid);
	}

}
