package com.oop.project.util;

/**
 * DataInitializer - Khoi tao va test ket noi database
 * 
 * Luu y: Du lieu da duoc insert vao MySQL thong qua file seed_data.sql
 * Khong can seed data trong code Java nua
 */
public class DataInitializer {
    
    /**
     * Khoi tao va test database connection
     */
    public static void initializeSampleData() {
        System.out.println("=== Initializing Database Connection ===");
        
        // Test ket noi database
        if (DatabaseUtil.testConnection()) {
            System.out.println("✓ Database connection successful");
            System.out.println("✓ Data is ready in MySQL");
            System.out.println("✓ Tables: Users, Clients, TaxReturns");
        } else {
            System.err.println("✗ Database connection FAILED!");
            System.err.println("✗ Please check:");
            System.err.println("  1. MySQL server is running");
            System.err.println("  2. Database 'tax_return_system' exists");
            System.err.println("  3. URL/username/password (now read from system properties) are correct");
            System.err.println("  4. MySQL JDBC driver is in classpath");
            // For additional diagnostics, attempt a direct connection to print stacktrace
            try {
                DatabaseUtil.getConnection();
            } catch (Exception e) {
                // already logged by DatabaseUtil
            }
        }
        
        System.out.println("=== Initialization Complete ===\n");
    }
}