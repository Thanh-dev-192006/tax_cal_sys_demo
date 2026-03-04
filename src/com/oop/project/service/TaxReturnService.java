package com.oop.project.service;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;
import com.oop.project.repository.TaxReturnRepository;
import com.oop.project.util.TaxCalculator;

import java.time.LocalDate;
import java.util.List;

/**
 * TaxReturnService - Nghiệp vụ quản lý tờ khai thuế TNCN
 *
 * Hạn nộp: 30/4 hàng năm (Luật Quản lý Thuế Việt Nam - Điều 44)
 */
public class TaxReturnService {

    private final TaxReturnRepository taxReturnRepository;

    public TaxReturnService() {
        this.taxReturnRepository = new TaxReturnRepository();
    }

    /**
     * Nộp tờ khai thuế và lưu vào file
     * @param clientId      Mã số thuế khách hàng
     * @param monthlyIncome Thu nhập tháng (VND)
     * @param dependents    Số người phụ thuộc
     * @param maritalStatus Tình trạng hôn nhân
     */
    public void fileTaxReturn(String clientId, double monthlyIncome, int dependents, String maritalStatus) {
        double taxLiability = TaxCalculator.calculateTax(monthlyIncome, dependents);
        LocalDate filingDate = LocalDate.now();
        String status = determineStatus(filingDate);
        TaxReturn taxReturn = new TaxReturn(clientId, filingDate, taxLiability, status, maritalStatus);
        taxReturnRepository.addTaxReturn(taxReturn);
    }

    public List<TaxReturn> getTaxReturnsForClient(String clientId) {
        return taxReturnRepository.findTaxReturnsByClientId(clientId);
    }

    public List<TaxReturn> getAllTaxReturns() {
        return taxReturnRepository.findAllTaxReturns();
    }

    /**
     * Xác định trạng thái tờ khai dựa trên ngày nộp.
     * Hạn nộp: 30/4 hàng năm (FR-3)
     */
    public String determineStatus(LocalDate filingDate) {
        LocalDate deadline = LocalDate.of(filingDate.getYear(), 4, 30);
        return filingDate.isAfter(deadline)
                ? TaxReturn.STATUS_OVERDUE
                : TaxReturn.STATUS_FILED;
    }

    // ===== Statistics for Dashboard =====

    public long getFiledCount(List<TaxReturn> returns) {
        return returns.stream()
                .filter(tr -> TaxReturn.STATUS_FILED.equals(tr.getStatus()))
                .count();
    }

    public long getOverdueCount(List<TaxReturn> returns) {
        return returns.stream()
                .filter(tr -> TaxReturn.STATUS_OVERDUE.equals(tr.getStatus()))
                .count();
    }

    /**
     * Số khách hàng chưa nộp = Tổng clients - Số đã có tờ khai
     */
    public long getPendingCount(int totalClients, List<TaxReturn> returns) {
        long clientsWithReturns = returns.stream()
                .map(TaxReturn::getClientId)
                .distinct()
                .count();
        return Math.max(0, totalClients - clientsWithReturns);
    }

    public double getTotalTaxCollected(List<TaxReturn> returns) {
        return returns.stream().mapToDouble(TaxReturn::getTaxLiability).sum();
    }

    public double getAverageTaxLiability(List<TaxReturn> returns) {
        return returns.stream().mapToDouble(TaxReturn::getTaxLiability).average().orElse(0);
    }

    public double getAverageIncome(List<Client> clients) {
        return clients.stream().mapToDouble(Client::getIncome).average().orElse(0);
    }

    /** @deprecated Use getOverdueCount() */
    @Deprecated
    public long getLateFilingsCount(List<TaxReturn> taxReturns) {
        return getOverdueCount(taxReturns);
    }
}
