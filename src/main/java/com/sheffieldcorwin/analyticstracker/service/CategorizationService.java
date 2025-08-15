package com.sheffieldcorwin.analyticstracker.service;

import org.springframework.stereotype.Service;

import com.sheffieldcorwin.analyticstracker.entity.FinanceTransaction;

@Service
public class CategorizationService {
	
	
	public void categorize(FinanceTransaction transaction) {
		String description = transaction.getDescription().toLowerCase();
		
		 if (description.contains("walmart") 
				 || description.contains("kroger") 
				 || description.contains("aldi")) {
	            transaction.setCategory("Groceries");
	        } else if (description.contains("uber") 
	        		|| description.contains("lyft") 
	        		|| description.contains("metro") 
	        		|| description.contains("gas")) {
	            transaction.setCategory("Transportation");
	        } else if (description.contains("netflix") 
	        		|| description.contains("spotify") 
	        		|| description.contains("hulu")) {
	            transaction.setCategory("Entertainment");
	        } else if (description.contains("hospital") 
	        		|| description.contains("pharmacy") 
	        		|| description.contains("doctor")) {
	            transaction.setCategory("Healthcare");
	        } else if (description.contains("airbnb") 
	        		|| description.contains("hotel") 
	        		|| description.contains("delta") 
	        		|| description.contains("united")) {
	            transaction.setCategory("Travel");
	        } else if (description.contains("salary") 
	        		|| description.contains("payroll") 
	        		|| description.contains("deposit") 
	        		|| description.contains("paycheck")) {
	            transaction.setCategory("Income");
	        } else if (description.contains("rent") 
	        		|| description.contains("mortgage")) {
	            transaction.setCategory("Housing");
	        } else if (description.contains("insurance")) {
	            transaction.setCategory("Insurance");
	        } else if (description.contains("loan") 
	        		|| description.contains("credit card")) {
	            transaction.setCategory("Debt Payments");
	        } else {
	            transaction.setCategory("Miscellaneous");
	        }
	}


}

