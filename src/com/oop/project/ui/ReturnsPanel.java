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
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ReturnsPanel extends JPanel {

    private ImageIcon loadLogo(int targetWidth, int targetHeight) {
        ImageIcon icon = null;
        try {
            java.net.URL url = getClass().getResource("/logo.png");
            if (url != null) {
                icon = new ImageIcon(url);
            } else {
                icon = new ImageIcon("logo.png");
            }
            if (icon != null && icon.getIconWidth() > 0) {
                Image scaled = icon.getImage().getScaledInstance(
                    targetWidth, targetHeight, Image.SCALE_SMOOTH
                );
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            System.err.println("Logo not found: " + e.getMessage());
        }
        return null;
    }

    private final ClientService    clientService    = new ClientService();
    private final TaxReturnService taxReturnService = new TaxReturnService();

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField fldSearch;
    private JComboBox<String> cmbStatusFilter;

    // Mini stats
    private JLabel lblStatTotal, lblStatFiled, lblStatPending, lblStatOverdue, lblRowCount;

    public ReturnsPanel() {
        initializeUI();
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
        p.setBorder(new EmptyBorder(AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_SM, AppTheme.PAD_LG));
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titlePanel.setOpaque(false);
        ImageIcon logoIcon = loadLogo(28, 28);
        if (logoIcon != null) {
            titlePanel.add(new JLabel(logoIcon));
        }
        JLabel title = new JLabel("Tax Returns");
        title.setFont(AppTheme.FONT_H1);
        title.setForeground(AppTheme.PRIMARY_DARK_RED);
        titlePanel.add(title);
        p.add(titlePanel, BorderLayout.WEST);
        return p;
    }

    // ── BODY ─────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(AppTheme.WARM_BG);
        body.add(buildToolbar(),   BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);
        return body;
    }

    // ── TOOLBAR: search + filters + stats ────────────────────────────
    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(0, 0));
        toolbar.setBackground(AppTheme.WARM_BG);
        toolbar.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_SM, AppTheme.PAD_LG));

        // Top row: search / filter / buttons
        JPanel topRow = new JPanel(new BorderLayout(0, 0));
        topRow.setBackground(AppTheme.WARM_CARD);
        topRow.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 0, 1, AppTheme.WARM_BORDER),
                new EmptyBorder(8, 12, 8, 12)));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        left.add(lbl("Search:"));
        fldSearch = new JTextField(22);
        fldSearch.setFont(AppTheme.FONT_BODY);
        fldSearch.putClientProperty("JTextField.placeholderText", "Client name, Tax ID, status...");
        fldSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });
        left.add(fldSearch);

        left.add(lbl("  Status:"));
        cmbStatusFilter = new JComboBox<>(new String[]{
                "All", TaxReturn.STATUS_FILED, TaxReturn.STATUS_PENDING, TaxReturn.STATUS_OVERDUE
        });
        cmbStatusFilter.setFont(AppTheme.FONT_BODY);
        cmbStatusFilter.addActionListener(e -> applyFilter());
        left.add(cmbStatusFilter);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        JButton btnRefresh = AppTheme.primaryBtn("Refresh");
        JButton btnCsv     = AppTheme.warnBtn("Export CSV");
        btnRefresh.addActionListener(e -> loadData());
        btnCsv.addActionListener(e -> CsvExporter.exportReturns(taxReturnService.getAllTaxReturns(), this));
        right.add(btnRefresh);
        right.add(btnCsv);

        topRow.add(left,  BorderLayout.WEST);
        topRow.add(right, BorderLayout.EAST);

        // Stats strip
        JPanel statsStrip = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        statsStrip.setBackground(AppTheme.WARM_HEADER);
        statsStrip.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER),
                new EmptyBorder(0, 8, 0, 8)));

        lblStatTotal   = chip("Total: 0",   AppTheme.PRIMARY_BLUE);
        lblStatFiled   = chip("Filed: 0",   AppTheme.FILED_TEXT);
        lblStatPending = chip("Pending: 0", AppTheme.PENDING_TEXT);
        lblStatOverdue = chip("Overdue: 0", AppTheme.OVERDUE_TEXT);

        statsStrip.add(lblStatTotal);
        statsStrip.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(1, 16)); }});
        statsStrip.add(lblStatFiled);
        statsStrip.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(1, 16)); }});
        statsStrip.add(lblStatPending);
        statsStrip.add(new JSeparator(SwingConstants.VERTICAL) {{ setPreferredSize(new Dimension(1, 16)); }});
        statsStrip.add(lblStatOverdue);

        toolbar.add(topRow,     BorderLayout.NORTH);
        toolbar.add(statsStrip, BorderLayout.SOUTH);
        return toolbar;
    }

    // ── TABLE CARD ───────────────────────────────────────────────────
    private JPanel buildTableCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.WARM_BG);
        wrapper.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_LG));

        String[] cols = {"Tax ID", "Full Name", "Filing Date", "Tax Liability (VND)", "Status", "Marital Status"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (col != 4) {
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

        // Status badge — colors ONLY the status cell
        table.getColumnModel().getColumn(4).setCellRenderer(
                (t, val, sel, foc, r, c) -> {
                    String v = val == null ? "" : val.toString();
                    Color rowBg = r % 2 == 0 ? AppTheme.WARM_CARD : AppTheme.WARM_STRIPE;
                    return AppTheme.createStatusLabel(v, sel, rowBg, AppTheme.TABLE_SELECTION_BG);
                });

        int[] widths = {120, 160, 110, 170, 110, 120};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER));

        lblRowCount = new JLabel("Showing 0 of 0 records");
        lblRowCount.setFont(AppTheme.FONT_CAPTION);
        lblRowCount.setForeground(AppTheme.TEXT_SECONDARY);
        lblRowCount.setBorder(new EmptyBorder(5, 2, 0, 0));

        wrapper.add(scroll,      BorderLayout.CENTER);
        wrapper.add(lblRowCount, BorderLayout.SOUTH);
        return wrapper;
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
                    tr.getClientId(), name,
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

        lblStatTotal.setText("Total: " + returns.size());
        lblStatFiled.setText("Filed: " + filed);
        lblStatPending.setText("Pending: " + pending);
        lblStatOverdue.setText("Overdue: " + overdue);

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
            statusFilter = RowFilter.regexFilter("(?i)^" + Pattern.quote(statusSel) + "$", 4);
        }

        if (textFilter != null && statusFilter != null)
            sorter.setRowFilter(RowFilter.andFilter(Arrays.asList(textFilter, statusFilter)));
        else if (textFilter != null)
            sorter.setRowFilter(textFilter);
        else if (statusFilter != null)
            sorter.setRowFilter(statusFilter);
        else
            sorter.setRowFilter(null);

        if (lblRowCount != null)
            lblRowCount.setText("Showing " + table.getRowCount() + " of " + tableModel.getRowCount() + " records");
    }

    // ── HELPERS ──────────────────────────────────────────────────────
    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        l.setForeground(AppTheme.TEXT_PRIMARY);
        return l;
    }

    private JLabel chip(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(color);
        return l;
    }
}
