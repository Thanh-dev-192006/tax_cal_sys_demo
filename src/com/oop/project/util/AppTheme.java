package com.oop.project.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public final class AppTheme {

    // ===================== ORIGINAL COLORS (kept for compatibility) =====================
    public static final Color BACKGROUND      = new Color(0xF4, 0xF1, 0xEC);
    public static final Color CARD_BG         = Color.WHITE;
    public static final Color BORDER_COLOR    = new Color(0xE2, 0xDD, 0xD6);
    public static final Color TEXT_PRIMARY    = new Color(0x1C, 0x1C, 0x1C);
    public static final Color TEXT_SECONDARY  = new Color(0x7A, 0x72, 0x65);
    public static final Color PRIMARY_BLUE    = new Color(0x1A, 0x3A, 0x5C);
    public static final Color ACCENT_BLUE     = new Color(0x1A, 0x5C, 0xA8);
    public static final Color ACCENT_GREEN    = new Color(0x1E, 0x6B, 0x45);
    public static final Color WARNING_AMBER   = new Color(0x96, 0x5B, 0x00);
    public static final Color ALERT_RED       = new Color(0xA5, 0x28, 0x20);
    public static final Color LIGHT_BLUE_BG   = new Color(0xDC, 0xE8, 0xF7);
    public static final Color SELECTION_BG    = new Color(0xDC, 0xE8, 0xF7);
    public static final Color DARK_BLUE       = new Color(0x0D, 0x47, 0xA1);
    public static final Color PURPLE          = new Color(0x6A, 0x1B, 0x9A);
    public static final Color PANEL_HEADER_BG = new Color(0x10, 0x18, 0x20);
    public static final Color LIGHT_GREEN_BG  = new Color(0xE6, 0xF4, 0xED);
    public static final Color LIGHT_RED_BG    = new Color(0xFD, 0xE9, 0xE7);
    public static final Color LIGHT_AMBER_BG  = new Color(0xFD, 0xF3, 0xD8);
    public static final Color LIGHT_PURPLE_BG = new Color(0xF3, 0xE5, 0xF5);
    public static final Color LIGHT_NAVY_BG   = new Color(0xDC, 0xE8, 0xF7);
    public static final Color TEXT_WHITE      = Color.WHITE;

    // ===================== ORIGINAL FONTS (kept for compatibility) =====================
    public static final Font FONT_TITLE      = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE   = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL      = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON     = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_STAT       = new Font("Consolas", Font.BOLD, 28);
    public static final Font FONT_CARD_VALUE = new Font("Consolas", Font.BOLD, 20);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO       = new Font("Consolas", Font.PLAIN, 12);

    // ===================== NEW DESIGN SYSTEM — SIDEBAR =====================
    public static final Color SIDEBAR_BG      = new Color(0x10, 0x18, 0x20);
    public static final Color SIDEBAR_HOVER   = new Color(0x1A, 0x26, 0x34);
    public static final Color SIDEBAR_ACTIVE  = new Color(0x18, 0x26, 0x38);
    public static final Color SIDEBAR_ACCENT  = new Color(0xC8, 0x94, 0x3A);
    public static final Color SIDEBAR_TEXT    = new Color(0x7A, 0x8A, 0x9A);
    public static final Color SIDEBAR_DIVIDER = new Color(0x1E, 0x28, 0x38);

    // ===================== NEW DESIGN SYSTEM — CONTENT AREA =====================
    public static final Color WARM_BG      = new Color(0xF4, 0xF1, 0xEC);
    public static final Color WARM_BORDER  = new Color(0xE2, 0xDD, 0xD6);
    public static final Color WARM_CARD    = new Color(0xFF, 0xFF, 0xFF);
    public static final Color WARM_STRIPE  = new Color(0xF9, 0xF7, 0xF4);
    public static final Color WARM_HEADER  = new Color(0xF0, 0xEE, 0xE9);

    // ===================== NEW DESIGN SYSTEM — TABLE SELECTION =====================
    public static final Color TABLE_SELECTION_BG = new Color(0xDC, 0xE8, 0xF7);
    public static final Color TABLE_SELECTION_FG = new Color(0x1C, 0x1C, 0x1C);

    // ===================== NEW DESIGN SYSTEM — STATUS BADGES =====================
    public static final Color FILED_TEXT   = new Color(0x1E, 0x6B, 0x45);
    public static final Color FILED_BG     = new Color(0xE6, 0xF4, 0xED);
    public static final Color PENDING_TEXT = new Color(0x96, 0x5B, 0x00);
    public static final Color PENDING_BG   = new Color(0xFD, 0xF3, 0xD8);
    public static final Color OVERDUE_TEXT = new Color(0xA5, 0x28, 0x20);
    public static final Color OVERDUE_BG   = new Color(0xFD, 0xE9, 0xE7);

    // ===================== NEW DESIGN SYSTEM — SPACING =====================
    public static final int PAD_SM = 8;
    public static final int PAD_MD = 16;
    public static final int PAD_LG = 24;
    public static final int PAD_XL = 32;

    // ===================== NEW DESIGN SYSTEM — FONTS =====================
    public static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_H1      = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_H2      = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_H3      = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NAV     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_NUM     = new Font("Consolas", Font.BOLD, 24);

    private AppTheme() {}

    public static void applyFlatLafCustomizations() {
        UIManager.put("Button.arc", 6);
        UIManager.put("Component.arc", 6);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("TableHeader.height", 34);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("TextField.placeholderForeground", new Color(180, 180, 180));
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        // Readable table selection — light bg, dark text
        UIManager.put("Table.selectionBackground", TABLE_SELECTION_BG);
        UIManager.put("Table.selectionForeground", TABLE_SELECTION_FG);
    }

    /**
     * Creates a stat card with a top accent line (3px) and dark monospace value.
     * The returned JPanel's BorderLayout.CENTER contains the value JLabel
     * so callers can retrieve it via ((BorderLayout) p.getLayout()).getLayoutComponent(BorderLayout.CENTER).
     */
    public static JPanel createStatCard(String icon, String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(WARM_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(3, 0, 0, 0, accentColor),
                new EmptyBorder(14, 18, 16, 18)));

        String headerText = icon != null && !icon.isEmpty()
                ? icon + "  " + title.toUpperCase()
                : title.toUpperCase();
        JLabel lblHeader = new JLabel(headerText);
        lblHeader.setFont(FONT_CAPTION.deriveFont(Font.BOLD));
        lblHeader.setForeground(TEXT_SECONDARY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(FONT_NUM);
        lblValue.setForeground(TEXT_PRIMARY);

        card.add(lblHeader, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        return card;
    }

    public static JLabel createStatusBadge(String status) {
        return createStatusLabel(status, false, WARM_CARD, TABLE_SELECTION_BG);
    }

    /**
     * Status badge renderer component — colors only this cell, not the row.
     */
    public static JLabel createStatusLabel(String status, boolean isSelected,
                                            Color rowBg, Color selectionBg) {
        JLabel lbl = new JLabel(status, SwingConstants.LEFT);
        lbl.setOpaque(true);
        lbl.setBorder(new EmptyBorder(2, 7, 2, 7));
        lbl.setFont(FONT_BODY.deriveFont(Font.BOLD, 11f));

        if (isSelected) {
            lbl.setBackground(TABLE_SELECTION_BG);
            lbl.setForeground(TABLE_SELECTION_FG);
        } else {
            if ("Filed".equalsIgnoreCase(status)) {
                lbl.setBackground(FILED_BG);
                lbl.setForeground(FILED_TEXT);
            } else if ("Pending".equalsIgnoreCase(status)) {
                lbl.setBackground(PENDING_BG);
                lbl.setForeground(PENDING_TEXT);
            } else if ("Overdue".equalsIgnoreCase(status)) {
                lbl.setBackground(OVERDUE_BG);
                lbl.setForeground(OVERDUE_TEXT);
            } else {
                lbl.setBackground(rowBg);
                lbl.setForeground(TEXT_PRIMARY);
            }
        }
        return lbl;
    }

    // ── Shared table styling helper ──────────────────────────────────
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.getTableHeader().setFont(FONT_H3);
        table.getTableHeader().setBackground(WARM_HEADER);
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, WARM_BORDER));
        table.setRowHeight(30);
        table.setGridColor(WARM_BORDER);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(TABLE_SELECTION_FG);
    }

    // ── Shared button factories ──────────────────────────────────────
    public static JButton primaryBtn(String text) {
        return styledBtn(text, SIDEBAR_BG, Color.WHITE);
    }

    public static JButton successBtn(String text) {
        return styledBtn(text, ACCENT_GREEN, Color.WHITE);
    }

    public static JButton dangerBtn(String text) {
        return styledBtn(text, ALERT_RED, Color.WHITE);
    }

    public static JButton warnBtn(String text) {
        return styledBtn(text, WARNING_AMBER, Color.WHITE);
    }

    public static JButton ghostBtn(String text) {
        return styledBtn(text, new Color(0xE8, 0xE5, 0xE0), TEXT_PRIMARY);
    }

    private static JButton styledBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        return btn;
    }

    // ── Section header builder ───────────────────────────────────────
    public static JPanel sectionHeader(String title, JComponent rightAction) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, PAD_MD, 0));

        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_H2);
        lbl.setForeground(TEXT_PRIMARY);
        p.add(lbl, BorderLayout.WEST);

        if (rightAction != null) p.add(rightAction, BorderLayout.EAST);
        return p;
    }
}
