package com.oop.project.model;

import java.io.Serializable;

public class TaxBracket implements Serializable {
    private static final long serialVersionUID = 1L;

    private double[] thresholds;
    private double[] rates;

    public TaxBracket(double[] thresholds, double[] rates) {
        this.thresholds = thresholds;
        this.rates = rates;
    }

    public double getTaxRate(double income) {
        for (int i = 0; i < thresholds.length; i++) {
            if (income <= thresholds[i]) {
                return rates[i];
            }
        }
        return rates[rates.length - 1]; // Highest rate for income above last threshold
    }

    // Getters
    public double[] getThresholds() { return thresholds; }
    public double[] getRates() { return rates; }
}