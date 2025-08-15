package com.sheffieldcorwin.analyticstracker.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class TransactionGeneratorService {

    private final Random rand = new Random();

    public Path generateCsv(String fileName, int total, double invalidRatio) throws IOException {
        String[] accounts = {"AC123", "AC124", "AC125"};
        String[] types = {"Credit", "Debit"};
        String[] categories = {"Salary", "Food", "Utilities", "Shopping", "Rent", "Transport", "Entertainment"};

        Path filePath = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write("date,account,type,category,amount,description\n");

            for (int i = 0; i < total; i++) {
                LocalDate date = LocalDate.of(2025, 8, rand.nextInt(31) + 1);
                String account = accounts[rand.nextInt(accounts.length)];
                String type = types[rand.nextInt(types.length)];
                String category = categories[rand.nextInt(categories.length)];
                double amount = 5 + rand.nextDouble() * 5000;

                String description = switch (category) {
                    case "Salary" -> "Monthly paycheck";
                    case "Food" -> "Lunch at cafe";
                    case "Utilities" -> "Utility bill";
                    case "Shopping" -> "Purchased items";
                    case "Rent" -> "Monthly rent";
                    case "Transport" -> "Taxi or bus fare";
                    case "Entertainment" -> "Movie or event";
                    default -> "Misc expense";
                };

                // Inject some invalid transactions
                if (rand.nextDouble() < invalidRatio) {
                    if (rand.nextBoolean()) type = "";
                    else if (rand.nextBoolean()) amount = -Math.abs(amount);
                    else description = "";
                }

                writer.write(String.format("%s,%s,%s,%s,%.2f,%s\n",
                        date, account, type, category, amount, description));
            }
        }

        return filePath;
    }
}
