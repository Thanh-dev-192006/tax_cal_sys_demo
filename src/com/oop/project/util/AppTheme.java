package com.oop.project.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

public final class AppTheme {

    // ===================== NEW COLOR PALETTE =====================
    public static final Color PRIMARY_DARK_RED = new Color(0x8B, 0x1A, 0x1A);  // #8B1A1A
    public static final Color PRIMARY_RED = new Color(0xC0, 0x39, 0x2B);       // #C0392B
    public static final Color ACCENT_TERRACOTTA = new Color(0xC0, 0x6B, 0x3A); // #C06B3A
    public static final Color ACCENT_GOLD = new Color(0xD4, 0xA0, 0x17);       // #D4A017
    public static final Color BACKGROUND_WHITE = new Color(0xFF, 0xFF, 0xFF);  // #FFFFFF
    public static final Color BACKGROUND_LIGHT = new Color(0xF5, 0xF5, 0xF5);  // #F5F5F5
    public static final Color BACKGROUND_WARM = new Color(0xFA, 0xF0, 0xE6);   // #FAF0E6
    public static final Color BORDER_LIGHT = new Color(0xE0, 0xE0, 0xE0);      // #E0E0E0
    public static final Color STATUS_SUCCESS = new Color(0x27, 0xAE, 0x60);    // #27AE60
    public static final Color STATUS_WARNING = new Color(0xE6, 0x7E, 0x22);    // #E67E22
    public static final Color STATUS_ERROR = new Color(0xC0, 0x39, 0x2B);      // #C0392B

    // ===================== COMPATIBILITY ALIASES =====================
    public static final Color BACKGROUND = BACKGROUND_LIGHT;
    public static final Color CARD_BG = BACKGROUND_WHITE;
    public static final Color BORDER_COLOR = BORDER_LIGHT;
    public static final Color TEXT_PRIMARY = new Color(0x21, 0x21, 0x21);    // #212121
    public static final Color TEXT_SECONDARY = new Color(0x75, 0x75, 0x75);  // #757575
    public static final Color PRIMARY_BLUE = PRIMARY_DARK_RED;
    public static final Color ACCENT_BLUE = ACCENT_TERRACOTTA;
    public static final Color ACCENT_GREEN = STATUS_SUCCESS;
    public static final Color WARNING_AMBER = STATUS_WARNING;
    public static final Color ALERT_RED = STATUS_ERROR;
    public static final Color LIGHT_BLUE_BG = BACKGROUND_WARM;
    public static final Color SELECTION_BG = BACKGROUND_WARM;
    public static final Color DARK_BLUE = PRIMARY_DARK_RED;
    public static final Color PURPLE = ACCENT_GOLD;
    public static final Color PANEL_HEADER_BG = PRIMARY_DARK_RED;
    public static final Color LIGHT_GREEN_BG = new Color(0xE8, 0xF5, 0xE9);
    public static final Color LIGHT_RED_BG = new Color(0xFD, 0xE9, 0xE7);
    public static final Color LIGHT_AMBER_BG = new Color(0xFD, 0xF3, 0xD8);
    public static final Color LIGHT_PURPLE_BG = new Color(0xF3, 0xE5, 0xF5);
    public static final Color LIGHT_NAVY_BG = BACKGROUND_WARM;
    public static final Color TEXT_WHITE = Color.WHITE;
    public static final Color TEXT_ON_DARK = Color.WHITE;

    // ===================== ORIGINAL FONTS (kept for compatibility) =====================
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_STAT = new Font("Consolas", Font.BOLD, 28);
    public static final Font FONT_CARD_VALUE = new Font("Consolas", Font.BOLD, 20);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 12);

    // ===================== SIDEBAR =====================
    public static final Color SIDEBAR_BG = PRIMARY_DARK_RED;
    public static final Color SIDEBAR_HOVER = BACKGROUND_LIGHT;
    public static final Color SIDEBAR_ACTIVE = BACKGROUND_WARM;
    public static final Color SIDEBAR_ACCENT = PRIMARY_DARK_RED;
    public static final Color SIDEBAR_TEXT = new Color(0x75, 0x75, 0x75);
    public static final Color SIDEBAR_DIVIDER = BORDER_LIGHT;

    // ===================== CONTENT AREA =====================
    public static final Color WARM_BG = BACKGROUND_LIGHT;
    public static final Color WARM_BORDER = BORDER_LIGHT;
    public static final Color WARM_CARD = BACKGROUND_WHITE;
    public static final Color WARM_STRIPE = BACKGROUND_LIGHT;
    public static final Color WARM_HEADER = PRIMARY_DARK_RED;

    // ===================== TABLE SELECTION =====================
    public static final Color TABLE_SELECTION_BG = new Color(0xE8, 0xC8, 0xB8);  // warmer, more visible
    public static final Color TABLE_SELECTION_FG = TEXT_PRIMARY;

    // ===================== STATUS BADGES =====================
    public static final Color FILED_TEXT = STATUS_SUCCESS;
    public static final Color FILED_BG = new Color(0xE8, 0xF5, 0xE9);
    public static final Color PENDING_TEXT = STATUS_WARNING;
    public static final Color PENDING_BG = new Color(0xFD, 0xF3, 0xD8);
    public static final Color OVERDUE_TEXT = STATUS_ERROR;
    public static final Color OVERDUE_BG = new Color(0xFD, 0xE9, 0xE7);

    // ===================== SPACING =====================
    public static final int PAD_SM = 8;
    public static final int PAD_MD = 16;
    public static final int PAD_LG = 24;
    public static final int PAD_XL = 32;

    // ===================== FONTS =====================
    public static final Font FONT_DISPLAY = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_H1 = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_H2 = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_H3 = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_NAV = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_NUM = new Font("Consolas", Font.BOLD, 24);

    private AppTheme() {
    }

    public static void applyFlatLafCustomizations() {
        UIManager.put("Button.arc", 6);
        UIManager.put("Component.arc", 6);
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("TableHeader.height", 34);
        UIManager.put("ScrollBar.thumbArc", 999);
        UIManager.put("ScrollBar.width", 8);
        UIManager.put("TextField.placeholderForeground", new Color(180, 180, 180));
        UIManager.put("TabbedPane.selectedBackground", Color.WHITE);
        // Readable table selection — warm bg, dark text
        UIManager.put("Table.selectionBackground", TABLE_SELECTION_BG);
        UIManager.put("Table.selectionForeground", TABLE_SELECTION_FG);
    }

    /**
     * Creates a stat card with a top accent line (3px) and dark monospace value.
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
     * Status badge renderer — foreground-only coloring for status.
     * CRITICAL: status colors are setForeground() ONLY — no setBackground() on status cells.
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
            // Background matches the row — status color is foreground only
            lbl.setBackground(rowBg);
            if ("Filed".equalsIgnoreCase(status)) {
                lbl.setForeground(FILED_TEXT);
            } else if ("Pending".equalsIgnoreCase(status)) {
                lbl.setForeground(PENDING_TEXT);
            } else if ("Overdue".equalsIgnoreCase(status)) {
                lbl.setForeground(OVERDUE_TEXT);
            } else {
                lbl.setForeground(TEXT_PRIMARY);
            }
        }
        return lbl;
    }

    // ── Shared table styling helper ──────────────────────────────────
    public static void styleTable(JTable table) {
        table.setFont(FONT_BODY);
        table.getTableHeader().setFont(FONT_H3);
        table.getTableHeader().setBackground(PRIMARY_DARK_RED);
        table.getTableHeader().setForeground(TEXT_ON_DARK);
        table.getTableHeader().setOpaque(true);
        // Nimbus ignores setBackground/setForeground on headers — force via custom renderer
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                lbl.setBackground(PRIMARY_DARK_RED);
                lbl.setForeground(TEXT_ON_DARK);
                lbl.setFont(FONT_H3);
                lbl.setOpaque(true);
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 1, 1, new Color(0xA0, 0x30, 0x30)),
                        new EmptyBorder(4, 8, 4, 8)));
                return lbl;
            }
        });
        table.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, BORDER_LIGHT));
        table.setRowHeight(30);
        table.setGridColor(BORDER_LIGHT);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(TABLE_SELECTION_BG);
        table.setSelectionForeground(TABLE_SELECTION_FG);
    }

    // ── Shared button factories ──────────────────────────────────────
    public static JButton primaryBtn(String text) {
        return styledBtn(text, PRIMARY_DARK_RED, TEXT_ON_DARK);
    }

    public static JButton successBtn(String text) {
        return styledBtn(text, STATUS_SUCCESS, TEXT_ON_DARK);
    }

    public static JButton dangerBtn(String text) {
        return styledBtn(text, STATUS_ERROR, TEXT_ON_DARK);
    }

    public static JButton warnBtn(String text) {
        return styledBtn(text, STATUS_WARNING, TEXT_ON_DARK);
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
        lbl.setForeground(PRIMARY_DARK_RED);
        p.add(lbl, BorderLayout.WEST);

        if (rightAction != null)
            p.add(rightAction, BorderLayout.EAST);
        return p;
    }
}
