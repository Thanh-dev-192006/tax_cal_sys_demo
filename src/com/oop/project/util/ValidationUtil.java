package com.oop.project.util;

public class ValidationUtil {

    /**
     * Validate Tax ID format: 9 digits
     * Vi du: 903739276
     */
    public static boolean isValidTaxID(String taxId) {
        return taxId != null && taxId.matches("\\d{9}");
    }

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    
    /**
     * Validate phone number format (Vietnamese)
     * Vi du: 0978796918, 0901000001
     */
    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("0\\d{9}");
    }

    /**
     * Validate positive number
     */
    public static boolean isPositiveNumber(double number) {
        return number >= 0;
    }
    
    /**
     * Validate marital status
     */
    public static boolean isValidMaritalStatus(String status) {
        if (status == null) return false;
        String  normalized = status.toUpperCase();
        return  normalized.equals("SINGLE") || 
                normalized.equals("MARRIED") || 
                normalized.equals("DIVORCED") || 
                normalized.equals("WIDOWED");
    }
    
    /**
     * Validate dependents (0-10)
     */
    public static boolean isValidDependents(int dependents) {
        return dependents >= 0 && dependents <= 10;
    }
}