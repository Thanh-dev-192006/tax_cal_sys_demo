package com.oop.project.ui;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;
import com.oop.project.service.ClientService;
import com.oop.project.service.TaxReturnService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.CsvExporter;
import com.oop.project.util.VndFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ReturnsPanel extends JPanel {

    private final ClientService    clientService    = new ClientService();
    private final TaxReturnService taxReturnService = new TaxReturnService();

    private JTable                          table;
    private DefaultTableModel               tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField                      fldSearch;
    private JComboBox<String>               cmbStatusFilter;

    // Mini stats
    private JLabel lblStatTotal, lblStatFiled, lblStatPending, lblStatOverdue;

    public ReturnsPanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
        add(buildTopBar(),    BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
    }

    // ── TOP BAR ──────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(12, 0));
        bar.setBackground(AppTheme.BACKGROUND);
        bar.setBorder(new EmptyBorder(10, 12, 6, 12));

        // Search + status filter
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        left.setBackground(AppTheme.BACKGROUND);
        left.add(lbl("Search:"));

        fldSearch = new JTextField(22);
        fldSearch.setFont(AppTheme.FONT_BODY);
        fldSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });
        left.add(fldSearch);

        left.add(lbl("  Status:"));
        cmbStatusFilter = new JComboBox<>(new String[]{
                "All",
                TaxReturn.STATUS_FILED,
                TaxReturn.STATUS_PENDING,
                TaxReturn.STATUS_OVERDUE
        });
        cmbStatusFilter.setFont(AppTheme.FONT_BODY);
        cmbStatusFilter.addActionListener(e -> applyFilter());
        left.add(cmbStatusFilter);

        // Action buttons
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        right.setBackground(AppTheme.BACKGROUND);

        JButton btnRefresh = styledBtn("Refresh",    AppTheme.PRIMARY_BLUE);
        JButton btnCsv     = styledBtn("Export CSV", AppTheme.WARNING_AMBER);
        btnRefresh.addActionListener(e -> loadData());
        btnCsv.addActionListener(e ->
                CsvExporter.exportReturns(taxReturnService.getAllTaxReturns(), this));
        right.add(btnRefresh);
        right.add(btnCsv);

        bar.add(left,  BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);

        // Stats mini-bar
        JPanel statsBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 3));
        statsBar.setBackground(new Color(0xEC, 0xEF, 0xF5));
        statsBar.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, AppTheme.BORDER_COLOR));

        lblStatTotal   = statChip("Total returns: 0",  AppTheme.PRIMARY_BLUE);
        lblStatFiled   = statChip("Filed: 0",          AppTheme.ACCENT_GREEN);
        lblStatPending = statChip("Pending: 0",        AppTheme.WARNING_AMBER);
        lblStatOverdue = statChip("Overdue: 0",        AppTheme.ALERT_RED);

        statsBar.add(lblStatTotal);
        statsBar.add(lblStatFiled);
        statsBar.add(lblStatPending);
        statsBar.add(lblStatOverdue);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.BACKGROUND);
        wrapper.add(bar,      BorderLayout.NORTH);
        wrapper.add(statsBar, BorderLayout.SOUTH);
        return wrapper;
    }

    // ── TABLE ─────────────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setBorder(new EmptyBorder(0, 12, 12, 12));

        String[] cols = {
            "Tax ID", "Full Name", "Filing Date",
            "Tax Liability (VND)", "Status", "Marital Status"
        };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(AppTheme.FONT_BODY);
        table.getTableHeader().setFont(AppTheme.FONT_BODY);
        table.setRowHeight(26);
        table.setGridColor(AppTheme.BORDER_COLOR);
        table.setSelectionBackground(AppTheme.LIGHT_BLUE_BG);

        int[] widths = {120, 160, 110, 170, 110, 120};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Colour-code the Status column (index 4)
        table.getColumnModel().getColumn(4).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    String v = val == null ? "" : val.toString();
                    if (TaxReturn.STATUS_FILED.equals(v))   setForeground(AppTheme.ACCENT_GREEN);
                    else if (TaxReturn.STATUS_OVERDUE.equals(v)) setForeground(AppTheme.ALERT_RED);
                    else setForeground(AppTheme.WARNING_AMBER);
                }
                return this;
            }
        });

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // ── DATA ──────────────────────────────────────────────────────────
    public void loadData() {
        tableModel.setRowCount(0);
        List<TaxReturn> returns = taxReturnService.getAllTaxReturns();

        for (TaxReturn tr : returns) {
            String name = "Unknown";
            Client c = clientService.findClientById(tr.getClientId());
            if (c != null) name = c.getName();

            tableModel.addRow(new Object[]{
                    tr.getClientId(),
                    name,
                    tr.getFilingDate(),
                    VndFormatter.format(tr.getTaxLiability()),
                    tr.getStatus(),
                    tr.getMaritalStatus()
            });
        }

        int totalClients = clientService.getAllClients().size();
        long filed   = taxReturnService.getFiledCount(returns);
        long overdue = taxReturnService.getOverdueCount(returns);
        long pending = taxReturnService.getPendingCount(totalClients, returns);

        lblStatTotal.setText("Total returns: "  + returns.size());
        lblStatFiled.setText("Filed: "          + filed);
        lblStatPending.setText("Pending: "      + pending);
        lblStatOverdue.setText("Overdue: "      + overdue);

        applyFilter();
    }

    private void applyFilter() {
        String text      = fldSearch.getText().trim();
        String statusSel = (String) cmbStatusFilter.getSelectedItem();

        RowFilter<DefaultTableModel, Object> textFilter   = null;
        RowFilter<DefaultTableModel, Object> statusFilter = null;

        if (!text.isEmpty()) {
            try { textFilter = RowFilter.regexFilter("(?i)" + text, 0, 1, 4, 5); }
            catch (java.util.regex.PatternSyntaxException ignored) {}
        }
        if (statusSel != null && !"All".equals(statusSel)) {
            statusFilter = RowFilter.regexFilter(
                    "(?i)^" + Pattern.quote(statusSel) + "$", 4);
        }

        if      (textFilter != null && statusFilter != null)
            sorter.setRowFilter(RowFilter.andFilter(Arrays.asList(textFilter, statusFilter)));
        else if (textFilter   != null) sorter.setRowFilter(textFilter);
        else if (statusFilter != null) sorter.setRowFilter(statusFilter);
        else                           sorter.setRowFilter(null);
    }

    // ── HELPERS ──────────────────────────────────────────────────────
    private JLabel lbl(String text) {
        JLabel l = new JLabel(text); l.setFont(AppTheme.FONT_BODY); return l;
    }

    private JLabel statChip(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(color);
        return l;
    }

    private JButton styledBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(AppTheme.FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 14, 6, 14));
        return btn;
    }
}
