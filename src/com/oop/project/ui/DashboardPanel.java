package com.oop.project.ui;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;
import com.oop.project.service.ClientService;
import com.oop.project.service.TaxReturnService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.VndFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final ClientService     clientService    = new ClientService();
    private final TaxReturnService  taxReturnService = new TaxReturnService();

    // 6 stat-card value labels
    private JLabel valTotalClients;
    private JLabel valFiled;
    private JLabel valPending;
    private JLabel valOverdue;
    private JLabel valTotalTax;
    private JLabel valAvgTax;

    // Recent-returns table (populated in refresh())
    private DefaultTableModel recentModel;

    public DashboardPanel() {
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);

        // ── HEADER ──────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.BACKGROUND);
        header.setBorder(new EmptyBorder(16, 20, 8, 20));

        JLabel lblTitle = new JLabel("System Overview");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.PRIMARY_BLUE);

        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(AppTheme.FONT_BUTTON);
        btnRefresh.setBackground(AppTheme.PRIMARY_BLUE);
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setBorder(new EmptyBorder(6, 16, 6, 16));
        btnRefresh.addActionListener(e -> refresh());

        header.add(lblTitle, BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);

        // ── 6 STAT CARDS  (2 rows × 3 cols) ────────────────────────
        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 14, 14));
        cardsPanel.setBackground(AppTheme.BACKGROUND);
        cardsPanel.setBorder(new EmptyBorder(0, 20, 16, 20));

        valTotalClients = addCard(cardsPanel, "Total Clients",
                "0", AppTheme.LIGHT_BLUE_BG,   AppTheme.PRIMARY_BLUE);
        valFiled        = addCard(cardsPanel, "Filed Returns",
                "0", AppTheme.LIGHT_GREEN_BG,  AppTheme.ACCENT_GREEN);
        valPending      = addCard(cardsPanel, "Pending Returns",
                "0", AppTheme.LIGHT_AMBER_BG,  AppTheme.WARNING_AMBER);
        valOverdue      = addCard(cardsPanel, "Overdue",
                "0", AppTheme.LIGHT_RED_BG,    AppTheme.ALERT_RED);
        valTotalTax     = addCard(cardsPanel, "Total Tax Collected (VND)",
                "0 VND", AppTheme.LIGHT_NAVY_BG,  AppTheme.DARK_BLUE);
        valAvgTax       = addCard(cardsPanel, "Avg Tax / Client (VND)",
                "0 VND", AppTheme.LIGHT_PURPLE_BG, AppTheme.PURPLE);

        // ── RECENT RETURNS TABLE ────────────────────────────────────
        JPanel tableSection = new JPanel(new BorderLayout(0, 6));
        tableSection.setBackground(AppTheme.BACKGROUND);
        tableSection.setBorder(new EmptyBorder(0, 20, 20, 20));

        JLabel lblRecent = new JLabel("Recent Tax Returns");
        lblRecent.setFont(AppTheme.FONT_SUBTITLE);
        lblRecent.setForeground(AppTheme.TEXT_PRIMARY);

        String[] cols = {"Client ID", "Filing Date", "Tax Liability (VND)", "Status", "Marital Status"};
        recentModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable recentTable = new JTable(recentModel);
        recentTable.setFont(AppTheme.FONT_BODY);
        recentTable.getTableHeader().setFont(AppTheme.FONT_BODY);
        recentTable.setRowHeight(26);
        recentTable.setGridColor(AppTheme.BORDER_COLOR);
        recentTable.setSelectionBackground(AppTheme.LIGHT_BLUE_BG);

        JScrollPane scroll = new JScrollPane(recentTable);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        scroll.setPreferredSize(new Dimension(0, 180));

        tableSection.add(lblRecent, BorderLayout.NORTH);
        tableSection.add(scroll, BorderLayout.CENTER);

        // ── ASSEMBLE ────────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(AppTheme.BACKGROUND);
        center.add(cardsPanel, BorderLayout.NORTH);
        center.add(tableSection, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private JLabel addCard(JPanel parent, String title, String initVal,
                           Color bg, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(valueColor.darker(), 1),
                new EmptyBorder(14, 16, 14, 16)));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppTheme.FONT_CARD_LABEL);
        lblTitle.setForeground(AppTheme.TEXT_SECONDARY);

        JLabel lblValue = new JLabel(initVal);
        lblValue.setFont(AppTheme.FONT_CARD_VALUE);
        lblValue.setForeground(valueColor);
        lblValue.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        parent.add(card);
        return lblValue;
    }

    public void refresh() {
        List<Client>    clients = clientService.getAllClients();
        List<TaxReturn> returns = taxReturnService.getAllTaxReturns();

        int    total    = clients.size();
        long   filed    = taxReturnService.getFiledCount(returns);
        long   overdue  = taxReturnService.getOverdueCount(returns);
        long   pending  = taxReturnService.getPendingCount(total, returns);
        double totalTax = taxReturnService.getTotalTaxCollected(returns);
        double avgTax   = taxReturnService.getAverageTaxLiability(returns);

        valTotalClients.setText(String.valueOf(total));
        valFiled.setText(String.valueOf(filed));
        valPending.setText(String.valueOf(pending));
        valOverdue.setText(String.valueOf(overdue));
        valTotalTax.setText(VndFormatter.formatShort(totalTax));
        valAvgTax.setText(VndFormatter.formatShort(avgTax));

        // Populate recent returns (up to 10, latest first)
        recentModel.setRowCount(0);
        int limit = Math.min(returns.size(), 10);
        for (int i = returns.size() - 1; i >= returns.size() - limit; i--) {
            TaxReturn tr = returns.get(i);
            recentModel.addRow(new Object[]{
                    tr.getClientId(),
                    tr.getFilingDate(),
                    VndFormatter.format(tr.getTaxLiability()),
                    tr.getStatus(),
                    tr.getMaritalStatus()
            });
        }
    }
}
