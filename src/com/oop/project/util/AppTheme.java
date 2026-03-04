package com.oop.project.util;

import java.awt.*;

/**
 * AppTheme - Bảng màu và font chữ thống nhất toàn hệ thống
 * Áp dụng theo phong cách Material Design nhẹ
 */
public final class AppTheme {

    // ===================== COLORS =====================
    public static final Color PRIMARY_BLUE    = new Color(0x15, 0x65, 0xC0);
    public static final Color DARK_BLUE       = new Color(0x0D, 0x47, 0xA1);
    public static final Color ACCENT_GREEN    = new Color(0x2E, 0x7D, 0x32);
    public static final Color ALERT_RED       = new Color(0xC6, 0x28, 0x28);
    public static final Color WARNING_AMBER   = new Color(0xF5, 0x7F, 0x17);
    public static final Color PURPLE          = new Color(0x6A, 0x1B, 0x9A);

    // Background / Surface
    public static final Color BACKGROUND      = new Color(0xF5, 0xF7, 0xFA);
    public static final Color CARD_BG         = Color.WHITE;
    public static final Color PANEL_HEADER_BG = new Color(0x1A, 0x23, 0x7E);

    // Light card backgrounds
    public static final Color LIGHT_BLUE_BG   = new Color(0xE3, 0xF2, 0xFD);
    public static final Color LIGHT_GREEN_BG  = new Color(0xE8, 0xF5, 0xE9);
    public static final Color LIGHT_RED_BG    = new Color(0xFF, 0xEB, 0xEE);
    public static final Color LIGHT_AMBER_BG  = new Color(0xFF, 0xF8, 0xE1);
    public static final Color LIGHT_PURPLE_BG = new Color(0xF3, 0xE5, 0xF5);
    public static final Color LIGHT_NAVY_BG   = new Color(0xE8, 0xEA, 0xF6);

    // Text
    public static final Color TEXT_PRIMARY    = new Color(0x21, 0x21, 0x21);
    public static final Color TEXT_SECONDARY  = new Color(0x75, 0x75, 0x75);
    public static final Color TEXT_WHITE      = Color.WHITE;
    public static final Color BORDER_COLOR    = new Color(0xE0, 0xE0, 0xE0);

    // ===================== FONTS =====================
    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO       = new Font("Consolas", Font.PLAIN, 12);

    private AppTheme() {}
}
