package com.sheffieldcorwin.analyticstracker.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;

@Service
public class CSVImportService {

	public List<FinanceTransaction> importTransactions(String filePath) throws IOException {
		List<FinanceTransaction> importedTransactions = new ArrayList<>();
		List<String> lines = Files.readAllLines(Paths.get(filePath));

		for (String line : lines.subList(1, lines.size())) {
			String[] columns = line.split(",", -1);
			FinanceTransaction t = new FinanceTransaction();

			try {
				t.setDate(columns.length > 0 && !columns[0].isEmpty() ? LocalDate.parse(columns[0])
						: LocalDate.of(1970, 1, 1));
			} catch (Exception e) {
				t.setDate(LocalDate.of(1970, 1, 1));
			}
			t.setAccount(columns.length > 1 ? columns[1] : "UNKNOWN");
			t.setType(columns.length > 2 ? columns[2] : "");
			t.setCategory(columns.length > 3 ? columns[3] : "Uncategorized");

			try {
				t.setAmount(columns.length > 4 && !columns[4].isEmpty() ? new BigDecimal(columns[4])
						: BigDecimal.valueOf(-1));
			} catch (NumberFormatException e) {
				t.setAmount(BigDecimal.valueOf(-1));
			}

			t.setDescription(columns.length > 5 ? columns[5] : "");
			importedTransactions.add(t);
		}
		return importedTransactions;
	}
}
