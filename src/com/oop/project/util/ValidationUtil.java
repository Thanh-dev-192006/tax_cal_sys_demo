package com.oop.project.util;

public class ValidationUtil {

    /**
     * Validate SSN format: XXX-XX-XXXX
     * Vi du: 903-73-9276
     */
    public static boolean isValidSSN(String ssn) {
        return ssn != null && ssn.matches("\\d{3}-\\d{2}-\\d{4}");
    }
    
    /**
     * Validate Tax ID format: 10 digits (no dashes)
     * Vi du: 9037392763
     */
    public static boolean isValidTaxID(String taxId) {
        return taxId != null && taxId.matches("\\d{10}");
    }
    
    /**
     * Convert Tax ID (10 digits) to SSN format (XXX-XX-XXXX)
     * Vi du: 9037392763 -> 903-73-9276
     */
    public static String taxIdToSSN(String taxId) {
        if (taxId == null || taxId.length() != 10) {
            return taxId;
        }
        return  taxId.substring(0, 3) + "-" + 
                taxId.substring(3, 5) + "-" + 
                taxId.substring(5);
    }
    
    /**
     * Convert SSN format (XXX-XX-XXXX) to Tax ID (10 digits)
     * Vi du: 903-73-9276 -> 9037392763
     */
    public static String ssnToTaxId(String ssn) {
        if (ssn == null) {
            return ssn;
        }
        return ssn.replace("-", "");
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