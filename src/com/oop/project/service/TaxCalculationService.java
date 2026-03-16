package com.oop.project.service;

import com.oop.project.model.TaxBracket;

public class TaxCalculationService {
    private TaxBracket taxBracket;

    public TaxCalculationService() {
        // Vietnam personal income tax — 7 brackets (monthly income thresholds, VND)
        // Bracket boundaries: ≤5M, ≤10M, ≤18M, ≤32M, ≤52M, ≤80M, >80M
        double[] thresholds = {
            5_000_000, 10_000_000, 18_000_000,
            32_000_000, 52_000_000, 80_000_000, Double.MAX_VALUE
        };
        double[] rates = {0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35};
        this.taxBracket = new TaxBracket(thresholds, rates);
    }

    public double calculateTax(double income) {
        double rate = taxBracket.getTaxRate(income);
        return income * rate;
    }

    public double calculateTax(double income, String maritalStatus) {
        return calculateTax(income);
    }
}