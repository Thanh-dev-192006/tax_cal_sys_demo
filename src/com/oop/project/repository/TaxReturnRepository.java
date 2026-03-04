package com.oop.project.repository;

import com.oop.project.model.TaxReturn;
import com.oop.project.util.FileUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TaxReturnRepository - Lưu/đọc tờ khai thuế từ returns.txt (pipe-separated UTF-8)
 *
 * Format mỗi dòng:
 *   clientId|filingDate|taxLiability|status|maritalStatus
 * Ví dụ:
 *   903-73-9276|2025-04-28|1250000.00|Da nop|MARRIED
 */
public class TaxReturnRepository {

    private static final String FILE_NAME = "returns.txt";
    private static final String SEP = "|";

    public void saveTaxReturns(List<TaxReturn> taxReturns) {
        List<String> lines = new ArrayList<>();
        for (TaxReturn tr : taxReturns) {
            lines.add(String.join(SEP,
                    s(tr.getClientId()),
                    tr.getFilingDate().toString(),
                    String.format("%.2f", tr.getTaxLiability()),
                    s(tr.getStatus()),
                    s(tr.getMaritalStatus())));
        }
        FileUtil.writeLines(lines, FILE_NAME);
    }

    public List<TaxReturn> loadTaxReturns() {
        List<String> lines = FileUtil.readLines(FILE_NAME);
        List<TaxReturn> returns = new ArrayList<>();
        for (String line : lines) {
            String[] p = line.split("\\|", -1);
            if (p.length >= 5) {
                try {
                    LocalDate date  = LocalDate.parse(p[1]);
                    double tax      = Double.parseDouble(p[2]);
                    returns.add(new TaxReturn(p[0], date, tax, p[3], p[4]));
                } catch (Exception ignored) {
                    // Skip malformed lines
                }
            }
        }
        return returns;
    }

    public void addTaxReturn(TaxReturn taxReturn) {
        List<TaxReturn> all = loadTaxReturns();
        all.add(taxReturn);
        saveTaxReturns(all);
    }

    public List<TaxReturn> findTaxReturnsByClientId(String clientId) {
        return loadTaxReturns().stream()
                .filter(tr -> tr.getClientId().equals(clientId))
                .collect(Collectors.toList());
    }

    public List<TaxReturn> findAllTaxReturns() {
        return loadTaxReturns();
    }

    private String s(String v) { return v == null ? "" : v; }
}
