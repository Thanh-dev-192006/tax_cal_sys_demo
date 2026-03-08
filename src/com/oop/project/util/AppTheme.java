package com.oop.project.util;

import javax.swing.*;
import java.awt.*;

/**
 * AppTheme - Bảng màu và font chữ thống nhất toàn hệ thống
 * Áp dụng theo phong cách Material Design nhẹ
 */
public final class AppTheme {

    // ===================== COLORS =====================
    public static final Color BACKGROUND      = new Color(0xF5, 0xF7, 0xFA);
    public static final Color CARD_BG         = Color.WHITE;
    public static final Color BORDER_COLOR    = new Color(0xE0, 0xE0, 0xE0);
    public static final Color TEXT_PRIMARY    = new Color(0x1A, 0x1A, 0x2E);
    public static final Color TEXT_SECONDARY  = new Color(0x6B, 0x72, 0x80);
    public static final Color PRIMARY_BLUE    = new Color(0x1E, 0x3A, 0x5F);
    public static final Color ACCENT_BLUE     = new Color(0x21, 0x96, 0xF3);
    public static final Color ACCENT_GREEN    = new Color(0x4C, 0xAF, 0x50);
    public static final Color WARNING_AMBER   = new Color(0xFF, 0x98, 0x00);
    public static final Color ALERT_RED       = new Color(0xF4, 0x43, 0x36);
    public static final Color LIGHT_BLUE_BG   = new Color(0xE3, 0xF2, 0xFD);

    // Existing colors kept to avoid breaking other files before refactoring
    public static final Color DARK_BLUE       = new Color(0x0D, 0x47, 0xA1);
    public static final Color PURPLE          = new Color(0x6A, 0x1B, 0x9A);
    public static final Color PANEL_HEADER_BG = new Color(0x1A, 0x23, 0x7E);
    public static final Color LIGHT_GREEN_BG  = new Color(0xE8, 0xF5, 0xE9);
    public static final Color LIGHT_RED_BG    = new Color(0xFF, 0xEB, 0xEE);
    public static final Color LIGHT_AMBER_BG  = new Color(0xFF, 0xF8, 0xE1);
    public static final Color LIGHT_PURPLE_BG = new Color(0xF3, 0xE5, 0xF5);
    public static final Color LIGHT_NAVY_BG   = new Color(0xE8, 0xEA, 0xF6);
    public static final Color TEXT_WHITE      = Color.WHITE;

    // ===================== FONTS =====================
    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_STAT     = new Font("Segoe UI", Font.BOLD, 32);

    // Existing fonts kept to avoid breaking other files before refactoring
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO       = new Font("Consolas", Font.PLAIN, 12);

    private AppTheme() {}

    public static void applyFlatLafCustomizations() {
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 6);
        UIManager.put("Table.rowHeight", 32);
        UIManager.put("TableHeader.height", 36);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("TextField.placeholderForeground", new Color(180, 180, 180));
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
    }

    public static JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            new javax.swing.border.EmptyBorder(16, 20, 16, 20)
        ));

        JLabel lblTitle = new JLabel(icon + " " + title);
        lblTitle.setFont(FONT_SMALL);
        lblTitle.setForeground(TEXT_SECONDARY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_STAT);
        lblValue.setForeground(accentColor);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    public static JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(status, SwingConstants.CENTER);
        badge.setOpaque(true);
        badge.setBorder(new javax.swing.border.EmptyBorder(3, 10, 3, 10));
        badge.setFont(FONT_SMALL.deriveFont(Font.BOLD));
        badge.setForeground(Color.WHITE);

        if ("Filed".equalsIgnoreCase(status)) {
            badge.setBackground(new Color(0x4CAF50));
        } else if ("Pending".equalsIgnoreCase(status)) {
            badge.setBackground(new Color(0xFF9800));
        } else if ("Overdue".equalsIgnoreCase(status)) {
            badge.setBackground(new Color(0xF44336));
        } else {
            badge.setBackground(Color.GRAY);
        }
        return badge;
    }
}
