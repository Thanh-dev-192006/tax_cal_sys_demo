package com.oop.project.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
//
/**
 * TaxCalculator — Vietnamese Progressive Income Tax (Thue TNCN) 
 * 
 * Formula: 
 *   Monthly taxable = max(0, monthly income − personal deduction − dependent deductions) 
 *   Annual taxable  = monthly taxable × 12 
 *   Tax             = apply 7-bracket progressive rates to annual taxable income 
 * 
 * FR-2.1: Parallel arrays for income thresholds and tax rates 
 * FR-2.2: Determine applicable tax rate based on income 
 * FR-2.3: Calculate final tax liability using progressive brackets 
 */  
public final class TaxCalculator { 

    // ================================================================ 
    // FR-2.1  PARALLEL ARRAYS — Annual taxable income thresholds (VND) 
    // ================================================================ 
    private static final double[] ANNUAL_THRESHOLDS = {
         60_000_000,   // Bracket 1: up to  60M/year  ( ≤ 5M/month)   →  5% 
        120_000_000,   // Bracket 2: 60–120M/year     (5–10M/month)    → 10% 
        216_000_000,   // Bracket 3: 120–216M/year    (10–18M/month)   → 15% 
        384_000_000,   // Bracket 4: 216–384M/year    (18–32M/month)   → 20% 
        624_000_000,   // Bracket 5: 384–624M/year    (32–52M/month)   → 25% 
        960_000_000,   // Bracket 6: 624–960M/year    (52–80M/month)   → 30% 
        Double.MAX_VALUE // Bracket 7: over 960M/year (>80M/month)     → 35% 
    }; 
 
    private static final double[] RATES = { 
        0.05, 0.10, 0.15, 0.20, 0.25, 0.30, 0.35 
    }; 
 
    private static final String[] BRACKET_LABELS = { 
        "Bracket 1 (up to 5M/month)", 
        "Bracket 2 (5–10M/month)", 
        "Bracket 3 (10–18M/month)", 
        "Bracket 4 (18–32M/month)", 
        "Bracket 5 (32–52M/month)", 
        "Bracket 6 (52–80M/month)", 
        "Bracket 7 (over 80M/month)" 
    }; 
 
    /** Personal deduction: 11,000,000 VND / month */ 
    public static final double PERSONAL_DEDUCTION_MONTHLY  = 11_000_000.0; 
    /** Dependent deduction: 4,400,000 VND / month / person */ 
    public static final double DEPENDENT_DEDUCTION_MONTHLY = 4_400_000.0; 
 
    private static final DecimalFormat DF; 
    static { 
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.US); 
        sym.setGroupingSeparator(','); 
        sym.setDecimalSeparator('.'); 
        DF = new DecimalFormat("#,##0", sym); 
    }

    // ================================================================
    // FR-2.2 / FR-2.3  Core tax calculation
    // ================================================================

    /**
     * Calculate annual income tax (VND).
     * @param monthlyIncome Monthly gross income (VND)
     * @param dependents    Number of dependents
     * @return Annual tax liability (VND)
     */
    public static double calculateTax(double monthlyIncome, int dependents) {
        if (monthlyIncome <= 0) return 0.0;
        double totalDeduction  = PERSONAL_DEDUCTION_MONTHLY + DEPENDENT_DEDUCTION_MONTHLY * dependents;
        double monthlyTaxable  = Math.max(0.0, monthlyIncome - totalDeduction);
        double annualTaxable   = monthlyTaxable * 12;
        return applyProgressiveBrackets(annualTaxable);
    }

    /**
     * Apply 7-bracket progressive rates to annual taxable income.
     */
    public static double applyProgressiveBrackets(double annualTaxable) {
        if (annualTaxable <= 0) return 0.0;
        double tax  = 0.0;
        double prev = 0.0;
        for (int i = 0; i < ANNUAL_THRESHOLDS.length; i++) {
            double limit     = ANNUAL_THRESHOLDS[i];
            double inBracket = annualTaxable > limit ? limit - prev : annualTaxable - prev;
            if (inBracket <= 0) break;
            tax  += inBracket * RATES[i];
            prev  = limit;
            if (annualTaxable <= limit) break;
        }
        return tax;
    }

    // ================================================================
    // HTML Receipt (displayed in JEditorPane)
    // ================================================================

    /**
     * Generate a detailed HTML tax statement.
     * @param monthlyIncome  Monthly gross income (VND)
     * @param dependents     Number of dependents
     * @param maritalStatus  Marital status string (for display only)
     * @return HTML string for JEditorPane
     */
    public static String generateHTMLReceipt(double monthlyIncome, int dependents,
                                             String maritalStatus) {
        double totalDeduct    = PERSONAL_DEDUCTION_MONTHLY + DEPENDENT_DEDUCTION_MONTHLY * dependents;
        double monthlyTaxable = Math.max(0.0, monthlyIncome - totalDeduct);
        double annualTaxable  = monthlyTaxable * 12;
        double annualIncome   = monthlyIncome  * 12;
        double totalTax       = applyProgressiveBrackets(annualTaxable);
        double monthlyTax     = totalTax / 12;
        double netAnnual      = annualIncome - totalTax;
        double effectiveRate  = annualIncome > 0 ? (totalTax / annualIncome) * 100 : 0;

        StringBuilder h = new StringBuilder();
        h.append("<html><body style='font-family:Segoe UI,Arial,sans-serif;"
                + " padding:10px; color:#212121; font-size:12pt;'>");

        // Header
        h.append("<h2 style='color:#1565C0; margin:0 0 4px 0;'>Personal Income Tax Statement</h2>");
        h.append("<p style='color:#757575; margin:0 0 8px 0; font-size:10pt;'>"
                + "Progressive tax calculation — Vietnam (7-bracket system)</p>");
        h.append("<hr style='border:2px solid #1565C0; margin:0 0 10px 0;'>");

        // Input summary
        h.append("<table style='width:100%; border-collapse:collapse; margin-bottom:10px;'>");
        h.append("<tr style='background:#E3F2FD;'>");
        h.append("<td style='padding:6px; width:50%;'><b>Marital Status:</b> ")
                .append(safe(maritalStatus)).append("</td>");
        h.append("<td style='padding:6px;'><b>Dependents:</b> ").append(dependents).append("</td>");
        h.append("</tr><tr>");
        h.append("<td style='padding:6px;'><b>Monthly Income:</b> "
                + "<span style='color:#1565C0; font-weight:bold;'>")
                .append(DF.format((long) monthlyIncome)).append(" VND</span></td>");
        h.append("<td style='padding:6px;'><b>Annual Income:</b> "
                + "<span style='color:#1565C0; font-weight:bold;'>")
                .append(DF.format((long) annualIncome)).append(" VND</span></td>");
        h.append("</tr></table>");

        // Deductions
        h.append("<div style='background:#E8F5E9; border-left:4px solid #2E7D32;"
                + " padding:8px; margin-bottom:10px;'>");
        h.append("<b style='color:#2E7D32;'>Deductions:</b><br>");
        h.append("&bull; Personal deduction: <b>")
                .append(DF.format((long) PERSONAL_DEDUCTION_MONTHLY)).append(" VND / month</b><br>");
        h.append("&bull; Dependent deduction (")
                .append(dependents).append(" &times; 4,400,000): <b>")
                .append(DF.format((long)(DEPENDENT_DEDUCTION_MONTHLY * dependents)))
                .append(" VND / month</b><br>");
        h.append("&bull; <b>Monthly taxable income: "
                + "<span style='color:#2E7D32; font-size:13pt;'>")
                .append(DF.format((long) monthlyTaxable)).append(" VND</span></b><br>");
        h.append("&bull; <b>Annual taxable income: "
                + "<span style='color:#2E7D32; font-size:13pt;'>")
                .append(DF.format((long) annualTaxable)).append(" VND</span></b>");
        h.append("</div>");

        // Bracket table
        h.append("<b style='color:#C62828;'>Progressive Tax Bracket Breakdown:</b>");
        h.append("<table border='1' cellspacing='0' cellpadding='5'"
                + " style='width:100%; border-collapse:collapse; margin-top:4px; margin-bottom:10px;'>");
        h.append("<tr style='background:#FFEBEE; color:#C62828;'>");
        h.append("<th>Bracket</th><th>Taxable Amount (VND)</th><th>Rate</th><th>Tax Due (VND)</th>");
        h.append("</tr>");

        if (annualTaxable > 0) {
            double prev = 0.0;
            int row = 0;
            for (int i = 0; i < ANNUAL_THRESHOLDS.length; i++) {
                double limit     = ANNUAL_THRESHOLDS[i];
                double inBracket = annualTaxable > limit ? limit - prev : annualTaxable - prev;
                if (inBracket <= 0) break;
                double taxForBracket = inBracket * RATES[i];
                String bg = (row++ % 2 == 0) ? "#FFFFFF" : "#FFF9F9";
                h.append("<tr style='background:").append(bg).append(";'>");
                h.append("<td style='padding:4px 6px;'>").append(BRACKET_LABELS[i]).append("</td>");
                h.append("<td align='right' style='padding:4px 6px;'>")
                        .append(DF.format((long) inBracket)).append("</td>");
                h.append("<td align='center'>").append((int)(RATES[i] * 100)).append("%</td>");
                h.append("<td align='right' style='font-weight:bold;'>")
                        .append(DF.format((long) taxForBracket)).append("</td>");
                h.append("</tr>");
                prev = limit;
                if (annualTaxable <= limit) break;
            }
        } else {
            h.append("<tr><td colspan='4' align='center'"
                    + " style='color:#2E7D32; padding:8px;'>"
                    + "Income is below the taxable threshold &mdash; No tax due</td></tr>");
        }
        h.append("</table>");

        // Summary box
        h.append("<div style='background:#E3F2FD; border:2px solid #1565C0;"
                + " border-radius:4px; padding:10px;'>");
        h.append("<table style='width:100%;'>");
        h.append("<tr><td><b>Total Annual Tax:</b></td>"
                + "<td align='right'><span style='color:#C62828; font-size:15pt; font-weight:bold;'>")
                .append(DF.format((long) totalTax)).append(" VND</span></td></tr>");
        h.append("<tr><td><b>Monthly Tax:</b></td>"
                + "<td align='right'><span style='color:#C62828; font-weight:bold;'>")
                .append(DF.format((long) monthlyTax)).append(" VND</span></td></tr>");
        h.append("<tr><td><b>Effective Tax Rate:</b></td><td align='right'>")
                .append(String.format("%.2f%%", effectiveRate)).append("</td></tr>");
        h.append("<tr style='border-top:1px solid #1565C0;'>"
                + "<td><b>Net Annual Income:</b></td>"
                + "<td align='right'><span style='color:#2E7D32; font-size:14pt; font-weight:bold;'>")
                .append(DF.format((long) netAnnual)).append(" VND</span></td></tr>");
        h.append("</table></div>");

        h.append("</body></html>");
        return h.toString();
    }

    /** Format amount as "#,##0 VND" */
    public static String formatCurrency(double amount) {
        return DF.format((long) amount) + " VND";
    }

    private static String safe(String v) { return v == null ? "" : v; }

    private TaxCalculator() {}
}
