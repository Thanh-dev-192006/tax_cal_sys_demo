package com.oop.project.util;

public class DataInitializer {
    
    public static void initializeSampleData() {
        System.out.println("=== Initializing system data ===");
        
        SeedDataInitializer.initializeAllData();
        
        System.out.println("=== Data initialization complete ===\n");
    }
}