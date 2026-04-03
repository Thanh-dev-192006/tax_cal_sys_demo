package com.oop.project.model;

import java.io.Serializable;
import java.time.LocalDate;

public class TaxReturn implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String STATUS_FILED   = "Filed";
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_OVERDUE = "Overdue";

    private String clientId;
    private LocalDate filingDate;
    private double taxLiability;
    private String status;       // STATUS_FILED | STATUS_PENDING | STATUS_OVERDUE
    private String maritalStatus;

    public TaxReturn(String clientId, LocalDate filingDate, double taxLiability,
                     String status, String maritalStatus) {
        this.clientId      = clientId;
        this.filingDate    = filingDate;
        this.taxLiability  = taxLiability;
        this.status        = status;
        this.maritalStatus = maritalStatus;
    }

    // Getters & Setters
    public String getClientId()            { return clientId; }
    public void setClientId(String v)      { this.clientId = v; }

    public LocalDate getFilingDate()       { return filingDate; }
    public void setFilingDate(LocalDate v) { this.filingDate = v; }

    public double getTaxLiability()        { return taxLiability; }
    public void setTaxLiability(double v)  { this.taxLiability = v; }

    public String getStatus()              { return status; }
    public void setStatus(String v)        { this.status = v; }

    public String getMaritalStatus()       { return maritalStatus; }
    public void setMaritalStatus(String v) { this.maritalStatus = v; }

    @Override
    public String toString() {
        return "TaxReturn{clientId='" + clientId + "', date=" + filingDate
                + ", tax=" + taxLiability + ", status='" + status + "'}";
    }
}
