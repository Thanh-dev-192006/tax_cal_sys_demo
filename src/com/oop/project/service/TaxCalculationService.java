package com.oop.project.service;

import com.oop.project.model.TaxBracket;

public class TaxCalculationService {
    private TaxBracket taxBracket;

    public TaxCalculationService() {
        // Sample tax brackets for US (simplified)
        double[] thresholds = {10000, 40000, 80000, 160000, Double.MAX_VALUE};
        double[] rates = {0.10, 0.12, 0.22, 0.24, 0.32};
        this.taxBracket = new TaxBracket(thresholds, rates);
    }

    public double calculateTax(double income) {
        double rate = taxBracket.getTaxRate(income);
        return income * rate;
    }

    public double calculateTax(double income, String maritalStatus) {
        // For simplicity, assume same brackets, but in real app, different for married
        return calculateTax(income);
    }
}