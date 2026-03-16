package com.oop.project.ui;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;
import com.oop.project.service.ClientService;
import com.oop.project.service.TaxReturnService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.VndFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private final ClientService    clientService    = new ClientService();
    private final TaxReturnService taxReturnService = new TaxReturnService();

    // Stat card value labels
    private JLabel valTotalClients;
    private JLabel valFiled;
    private JLabel valPending;
    private JLabel valOverdue;
    private JLabel valTotalTax;
    private JLabel valAvgTax;

    private DefaultTableModel recentModel;
    private JLabel lblShowingCount;

    public DashboardPanel() {
        initializeUI();
        refresh();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.WARM_BG);

        add(buildPageHeader(), BorderLayout.NORTH);
        add(buildBody(),       BorderLayout.CENTER);
    }

    // ── PAGE HEADER ──────────────────────────────────────────────────
    private JPanel buildPageHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.WARM_BG);
        p.setBorder(new EmptyBorder(AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG));

        JLabel title = new JLabel("System Overview");
        title.setFont(AppTheme.FONT_H1);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        JButton btnRefresh = AppTheme.primaryBtn("Refresh");
        btnRefresh.addActionListener(e -> refresh());

        p.add(title,      BorderLayout.WEST);
        p.add(btnRefresh, BorderLayout.EAST);
        return p;
    }

    // ── BODY ─────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(AppTheme.WARM_BG);

        body.add(buildStatsGrid(),    BorderLayout.NORTH);
        body.add(buildRecentSection(), BorderLayout.CENTER);
        return body;
    }

    // ── 6 STAT CARDS ─────────────────────────────────────────────────
    private JPanel buildStatsGrid() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.WARM_BG);
        wrapper.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG));

        JPanel grid = new JPanel(new GridLayout(2, 3, 12, 12));
        grid.setBackground(AppTheme.WARM_BG);

        valTotalClients = buildCard(grid, "\u25A0", "Total Clients",            "0",     AppTheme.PRIMARY_BLUE);
        valFiled        = buildCard(grid, "\u25A0", "Filed Returns",            "0",     AppTheme.ACCENT_GREEN);
        valPending      = buildCard(grid, "\u25A0", "Pending Returns",          "0",     AppTheme.WARNING_AMBER);
        valOverdue      = buildCard(grid, "\u25A0", "Overdue",                  "0",     AppTheme.ALERT_RED);
        valTotalTax     = buildCard(grid, "\u25A0", "Total Tax Collected (VND)","0 VND", AppTheme.ACCENT_BLUE);
        valAvgTax       = buildCard(grid, "\u25A0", "Avg Tax / Client (VND)",   "0 VND", new Color(0x6A, 0x1B, 0x9A));

        wrapper.add(grid, BorderLayout.CENTER);
        return wrapper;
    }

    private JLabel buildCard(JPanel parent, String icon, String title, String val, Color accent) {
        JPanel card = AppTheme.createStatCard(icon, title, val, accent);
        parent.add(card);
        return (JLabel) ((BorderLayout) card.getLayout()).getLayoutComponent(BorderLayout.CENTER);
    }

    // ── RECENT RETURNS TABLE ──────────────────────────────────────────
    private JPanel buildRecentSection() {
        JPanel section = new JPanel(new BorderLayout(0, 8));
        section.setBackground(AppTheme.WARM_BG);
        section.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_LG));

        // Section heading
        JPanel heading = new JPanel(new BorderLayout());
        heading.setOpaque(false);
        heading.setBorder(new EmptyBorder(0, 0, AppTheme.PAD_SM, 0));

        JLabel lblTitle = new JLabel("Recent Tax Returns");
        lblTitle.setFont(AppTheme.FONT_H2);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);

        lblShowingCount = new JLabel("Showing 0 recent records");
        lblShowingCount.setFont(AppTheme.FONT_CAPTION);
        lblShowingCount.setForeground(AppTheme.TEXT_SECONDARY);

        heading.add(lblTitle,       BorderLayout.WEST);
        heading.add(lblShowingCount, BorderLayout.EAST);

        // Table
        String[] cols = {"Client ID", "Filing Date", "Tax Liability (VND)", "Status", "Marital Status"};
        recentModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(recentModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (col != 3) {
                    if (isRowSelected(row)) {
                        c.setBackground(AppTheme.TABLE_SELECTION_BG);
                        c.setForeground(AppTheme.TABLE_SELECTION_FG);
                    } else {
                        c.setBackground(row % 2 == 0 ? AppTheme.WARM_CARD : AppTheme.WARM_STRIPE);
                        c.setForeground(AppTheme.TEXT_PRIMARY);
                    }
                }
                return c;
            }
        };
        AppTheme.styleTable(table);

        // Status badge renderer — colors ONLY the status cell
        table.getColumnModel().getColumn(3).setCellRenderer(
                (tbl, value, isSelected, hasFocus, row, column) -> {
                    String status = value == null ? "" : value.toString();
                    Color rowBg = row % 2 == 0 ? AppTheme.WARM_CARD : AppTheme.WARM_STRIPE;
                    return AppTheme.createStatusLabel(status, isSelected, rowBg, AppTheme.TABLE_SELECTION_BG);
                });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER));
        scroll.setBackground(AppTheme.WARM_CARD);

        section.add(heading, BorderLayout.NORTH);
        section.add(scroll,  BorderLayout.CENTER);
        return section;
    }

    // ── REFRESH ──────────────────────────────────────────────────────
    public void refresh() {
        List<Client>    clients = clientService.getAllClients();
        List<TaxReturn> returns = taxReturnService.getAllTaxReturns();

        int    total   = clients.size();
        long   filed   = taxReturnService.getFiledCount(returns);
        long   overdue = taxReturnService.getOverdueCount(returns);
        long   pending = taxReturnService.getPendingCount(total, returns);
        double totalTax = taxReturnService.getTotalTaxCollected(returns);
        double avgTax   = taxReturnService.getAverageTaxLiability(returns);

        valTotalClients.setText(String.valueOf(total));
        valFiled.setText(String.valueOf(filed));
        valPending.setText(String.valueOf(pending));
        valOverdue.setText(String.valueOf(overdue));
        valTotalTax.setText(VndFormatter.formatShort(totalTax));
        valAvgTax.setText(VndFormatter.formatShort(avgTax));

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
        lblShowingCount.setText("Showing " + limit + " recent records");
    }
}
