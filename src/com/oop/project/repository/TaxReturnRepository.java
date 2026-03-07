package com.oop.project.repository;

import com.oop.project.model.TaxReturn;
import com.oop.project.util.DatabaseUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TaxReturnRepository - Su dung MySQL
 */
public class TaxReturnRepository {
    
    /**
     * Luu danh sach tax returns
     */
    public void saveTaxReturns(List<TaxReturn> taxReturns) {
        String sql = "INSERT INTO TaxReturns (client_id, filing_date, tax_liability, status, marital_status) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            for (TaxReturn tr : taxReturns) {
                stmt.setString(1, tr.getClientId());
                stmt.setDate(2, Date.valueOf(tr.getFilingDate()));
                stmt.setDouble(3, tr.getTaxLiability());
                stmt.setString(4, tr.getStatus());
                stmt.setString(5, tr.getMaritalStatus());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            System.out.println("Saved " + taxReturns.size() + " tax returns");
            
        } catch (SQLException e) {
            System.err.println("Error saving tax returns: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Load tat ca tax returns
     */
    public List<TaxReturn> loadTaxReturns() {
        List<TaxReturn> taxReturns = new ArrayList<>();
        String sql = "SELECT * FROM TaxReturns ORDER BY filing_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                TaxReturn tr = new TaxReturn(
                    rs.getString("client_id"),
                    rs.getDate("filing_date").toLocalDate(),
                    rs.getDouble("tax_liability"),
                    rs.getString("status"),
                    rs.getString("marital_status")
                );
                taxReturns.add(tr);
            }
            
            System.out.println("Loaded " + taxReturns.size() + " tax returns");
            
        } catch (SQLException e) {
            System.err.println("Error loading tax returns: " + e.getMessage());
            e.printStackTrace();
        }
        
        return taxReturns;
    }
    
    /**
     * Them 1 tax return
     */
    public void addTaxReturn(TaxReturn taxReturn) {
        String sql = "INSERT INTO TaxReturns (client_id, filing_date, tax_liability, status, marital_status) " +
                    "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, taxReturn.getClientId());
            stmt.setDate(2, Date.valueOf(taxReturn.getFilingDate()));
            stmt.setDouble(3, taxReturn.getTaxLiability());
            stmt.setString(4, taxReturn.getStatus());
            stmt.setString(5, taxReturn.getMaritalStatus());
            
            stmt.executeUpdate();
            System.out.println("Added tax return for client: " + taxReturn.getClientId());
            
        } catch (SQLException e) {
            System.err.println("Error adding tax return: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tim tax returns theo client ID
     */
    public List<TaxReturn> findTaxReturnsByClientId(String clientId) {
        List<TaxReturn> taxReturns = new ArrayList<>();
        String sql = "SELECT * FROM TaxReturns WHERE client_id = ? ORDER BY filing_date DESC";
        
        try (Connection conn = DatabaseUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, clientId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TaxReturn tr = new TaxReturn(
                        rs.getString("client_id"),
                        rs.getDate("filing_date").toLocalDate(),
                        rs.getDouble("tax_liability"),
                        rs.getString("status"),
                        rs.getString("marital_status")
                    );
                    taxReturns.add(tr);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error finding tax returns: " + e.getMessage());
            e.printStackTrace();
        }
        
        return taxReturns;
    }
    
    /**
     * Lay tat ca tax returns
     */
    public List<TaxReturn> findAllTaxReturns() {
        return loadTaxReturns();
    }
}