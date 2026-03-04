package com.oop.project.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * VndFormatter - Định dạng số tiền VND (Đồng Việt Nam)
 *
 * Ví dụ: 15000000.0 → "15,000,000 VND"
 *        180500000.0 → "180.5 triệu VND" (formatShort)
 */
public final class VndFormatter {

    private static final DecimalFormatSymbols SYMBOLS;
    private static final DecimalFormat FULL_FORMAT;
    private static final DecimalFormat NUMBER_ONLY;

    static {
        SYMBOLS = new DecimalFormatSymbols(Locale.US);
        SYMBOLS.setGroupingSeparator(',');
        SYMBOLS.setDecimalSeparator('.');
        FULL_FORMAT  = new DecimalFormat("#,##0", SYMBOLS);
        NUMBER_ONLY  = new DecimalFormat("#,##0", SYMBOLS);
    }

    /** Định dạng đầy đủ kèm đơn vị VND. Ví dụ: 15,000,000 VND */
    public static String format(double amount) {
        return FULL_FORMAT.format((long) amount) + " VND";
    }

    /** Chỉ format số, không có đơn vị. Ví dụ: 15,000,000 */
    public static String formatNumberOnly(double amount) {
        return NUMBER_ONLY.format((long) amount);
    }

    /**
     * Format ngắn gọn theo đơn vị triệu/tỷ.
     * Ví dụ: 15,000,000 → "15.0 triệu VND"
     *        2,500,000,000 → "2.5 tỷ VND"
     */
    public static String formatShort(double amount) {
        if (amount >= 1_000_000_000) {
            return String.format("%.1f B VND", amount / 1_000_000_000.0);
        } else if (amount >= 1_000_000) {
            return String.format("%.1f M VND", amount / 1_000_000.0);
        }
        return format(amount);
    }

    /**
     * Chuyển chuỗi VND về double.
     * Ví dụ: "15,000,000 VND" → 15000000.0
     */
    public static double parse(String text) {
        if (text == null || text.isBlank()) return 0.0;
        String cleaned = text.replace(",", "")
                             .replace(" VND", "")
                             .replace("VND", "")
                             .trim();
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private VndFormatter() {}
}
