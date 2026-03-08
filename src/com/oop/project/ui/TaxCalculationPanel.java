package com.oop.project.ui;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;
import com.oop.project.service.ClientService;
import com.oop.project.service.TaxReturnService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.TaxCalculator;
import com.oop.project.util.VndFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;

public class TaxCalculationPanel extends JPanel {

    private final ClientService clientService = new ClientService();
    private final TaxReturnService taxReturnService = new TaxReturnService();

    private Client currentClient = null;

    // UI components
    private JTextField txtSearchId;
    private JLabel lblSearchResult;
    private JTextField txtIncome;
    private JSpinner spnDependents;
    private JComboBox<String> cmbMarital;
    private JEditorPane txtReceipt;
    private JButton btnFileReturn;

    // Summary cards
    private JLabel lblCardIncome;
    private JLabel lblCardTax;
    private JLabel lblCardNet;

    public TaxCalculationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 12, 10, 12));
        setBackground(AppTheme.BACKGROUND);
        add(buildSearchPanel(), BorderLayout.NORTH);
        add(buildMainContent(), BorderLayout.CENTER);
    }

    // ── SEARCH PANEL ─────────────────────────────────────────────────
    private JPanel buildSearchPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        p.setBackground(AppTheme.CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, AppTheme.ACCENT_BLUE),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(AppTheme.ACCENT_BLUE, 1),
                                "Step 1: Find Client"),
                        new EmptyBorder(4, 8, 4, 8))));

        p.add(lbl("Client Tax ID (XXX-XX-XXXX):"));
        txtSearchId = new JTextField(16);
        txtSearchId.setFont(AppTheme.FONT_BODY);
        txtSearchId.addActionListener(e -> performSearch());
        p.add(txtSearchId);

        JButton btnSearch = styledBtn("Load Data", AppTheme.PRIMARY_BLUE);
        btnSearch.addActionListener(e -> performSearch());
        p.add(btnSearch);

        lblSearchResult = new JLabel("No client selected");
        lblSearchResult.setFont(new Font("Segoe UI", Font.BOLD | Font.ITALIC, 13));
        lblSearchResult.setForeground(AppTheme.TEXT_SECONDARY);
        p.add(lblSearchResult);
        return p;
    }

    // ── MAIN CONTENT ─────────────────────────────────────────────────
    private JPanel buildMainContent() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBackground(AppTheme.BACKGROUND);

        // 3 summary cards
        JPanel cardsRow = new JPanel(new GridLayout(1, 3, 12, 0));
        cardsRow.setBackground(AppTheme.BACKGROUND);
        cardsRow.setBorder(new EmptyBorder(0, 0, 8, 0));

        lblCardIncome = buildCard(cardsRow, "Annual Income (VND)",
                "0 VND", AppTheme.ACCENT_BLUE);
        lblCardTax = buildCard(cardsRow, "Annual Tax (VND)",
                "0 VND", AppTheme.ALERT_RED);
        lblCardNet = buildCard(cardsRow, "Net Annual Income (VND)",
                "0 VND", AppTheme.ACCENT_GREEN);

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.setBackground(AppTheme.BACKGROUND);
        top.add(cardsRow, BorderLayout.NORTH);

        // Parameter inputs
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(AppTheme.CARD_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, AppTheme.PRIMARY_BLUE),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createLineBorder(AppTheme.PRIMARY_BLUE, 1),
                                "Step 2: Adjust Parameters"),
                        new EmptyBorder(8, 12, 8, 12))));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(4, 4, 4, 8);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets = new Insets(4, 0, 4, 16);

        lc.gridx = 0;
        lc.gridy = 0;
        inputPanel.add(lbl("Monthly Income (VND):"), lc);
        fc.gridx = 1;
        fc.gridy = 0;
        txtIncome = new JTextField(14);
        txtIncome.setFont(AppTheme.FONT_BODY);
        inputPanel.add(txtIncome, fc);

        lc.gridx = 2;
        lc.gridy = 0;
        inputPanel.add(lbl("Dependents:"), lc);
        fc.gridx = 3;
        fc.gridy = 0;
        fc.weightx = 0.5;
        spnDependents = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        spnDependents.setFont(AppTheme.FONT_BODY);
        inputPanel.add(spnDependents, fc);

        lc.gridx = 0;
        lc.gridy = 1;
        inputPanel.add(lbl("Marital Status:"), lc);
        fc.gridx = 1;
        fc.gridy = 1;
        fc.weightx = 1.0;
        cmbMarital = new JComboBox<>(new String[] { "SINGLE", "MARRIED", "DIVORCED", "WIDOWED" });
        cmbMarital.setFont(AppTheme.FONT_BODY);
        inputPanel.add(cmbMarital, fc);

        lc.gridx = 2;
        lc.gridy = 1;
        JLabel infoDeduct = new JLabel("Personal deduction: 11M  |  Dependent: 4.4M / person");
        infoDeduct.setFont(AppTheme.FONT_SMALL);
        infoDeduct.setForeground(AppTheme.ACCENT_GREEN);
        inputPanel.add(infoDeduct, lc);

        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 4;
        bc.gridy = 0;
        bc.gridheight = 2;
        bc.insets = new Insets(4, 8, 4, 4);
        JButton btnReset = styledBtn("Clear Form", AppTheme.TEXT_SECONDARY);
        btnReset.addActionListener(e -> clearForm());
        inputPanel.add(btnReset, bc);

        top.add(inputPanel, BorderLayout.CENTER);
        main.add(top, BorderLayout.NORTH);

        // HTML receipt
        txtReceipt = new JEditorPane();
        txtReceipt.setContentType("text/html");
        txtReceipt.setEditable(false);
        txtReceipt.setBackground(Color.WHITE);
        txtReceipt.setText("<html><body style='color:#757575; font-family:Segoe UI; padding:20px; text-align:center;'>"
                + "📋 Enter a Client Tax ID above and click Load Data<br>to view detailed tax breakdown by income bracket.</body></html>");

        JScrollPane scroll = new JScrollPane(txtReceipt);
        scroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                "Tax Breakdown  (Detailed Statement)"));
        main.add(scroll, BorderLayout.CENTER);

        // File return button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        btnPanel.setBackground(AppTheme.BACKGROUND);

        JLabel deadlineHint = new JLabel(
                "Deadline: April 30 annually  —  Late filings will be marked as 'Overdue'");
        deadlineHint.setFont(AppTheme.FONT_SMALL);
        deadlineHint.setForeground(AppTheme.WARNING_AMBER);

        btnFileReturn = new JButton("File Tax Return  (Save & Finish)");
        btnFileReturn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFileReturn.setBackground(AppTheme.ACCENT_GREEN);
        btnFileReturn.setForeground(Color.WHITE);
        btnFileReturn.setPreferredSize(new Dimension(280, 42));
        btnFileReturn.setFocusPainted(false);
        btnFileReturn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFileReturn.setEnabled(false);
        btnFileReturn.setToolTipText("Complete tax calculation first");
        btnFileReturn.addActionListener(e -> fileTaxReturn());

        btnPanel.add(deadlineHint);
        btnPanel.add(btnFileReturn);
        main.add(btnPanel, BorderLayout.SOUTH);

        // Live recalculation
        DocumentListener dl = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calculate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calculate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calculate();
            }
        };
        txtIncome.getDocument().addDocumentListener(dl);
        spnDependents.addChangeListener(e -> calculate());
        cmbMarital.addActionListener(e -> calculate());

        return main;
    }

    // ── LOGIC ─────────────────────────────────────────────────────────
    private void performSearch() {
        String id = txtSearchId.getText().trim();
        if (id.isEmpty())
            return;

        currentClient = clientService.findClientById(id);
        if (currentClient != null) {
            lblSearchResult.setText("Found: " + currentClient.getName()
                    + "   |   " + currentClient.getCity());
            lblSearchResult.setForeground(AppTheme.ACCENT_GREEN);

            txtIncome.setText(String.format("%.0f", currentClient.getIncome()));
            spnDependents.setValue(currentClient.getDependents());

            String status = currentClient.getMaritalStatus();
            if (status != null)
                cmbMarital.setSelectedItem(status.toUpperCase());

            calculate();
        } else {
            lblSearchResult.setText("Not found: " + id);
            lblSearchResult.setForeground(AppTheme.ALERT_RED);
            JOptionPane.showMessageDialog(this,
                    "No client found with Tax ID: " + id,
                    "Not Found", JOptionPane.WARNING_MESSAGE);
            currentClient = null;
        }
    }

    private void calculate() {
        try {
            String text = txtIncome.getText().trim().replace(",", "");
            if (text.isEmpty()) {
                resetCards();
                txtReceipt.setText(
                        "<html><body style='color:#757575; font-family:Segoe UI; padding:20px; text-align:center;'>"
                                + "📋 Enter a Client Tax ID above and click Load Data<br>to view detailed tax breakdown by income bracket.</body></html>");
                btnFileReturn.setEnabled(false);
                btnFileReturn.setToolTipText("Complete tax calculation first");
                return;
            }

            double monthly = Double.parseDouble(text);
            int dependents = (int) spnDependents.getValue();
            String marital = (String) cmbMarital.getSelectedItem();

            double annualTax = TaxCalculator.calculateTax(monthly, dependents);
            double annualInc = monthly * 12;
            double net = annualInc - annualTax;

            lblCardIncome.setText(VndFormatter.formatShort(annualInc));
            lblCardTax.setText(VndFormatter.formatShort(annualTax));
            lblCardNet.setText(VndFormatter.formatShort(net));

            txtReceipt.setText(TaxCalculator.generateHTMLReceipt(monthly, dependents, marital));
            txtReceipt.setCaretPosition(0);

            boolean canFile = currentClient != null;
            btnFileReturn.setEnabled(canFile);
            btnFileReturn.setToolTipText(canFile ? "Click to file tax return" : "Complete tax calculation first");

        } catch (NumberFormatException e) {
            resetCards();
            txtReceipt.setText(
                    "<html><body style='color:#C62828; font-family:Segoe UI; padding:16px; text-align:center;'>"
                            + "<b>Invalid input.</b> Please enter numbers only.</body></html>");
            btnFileReturn.setEnabled(false);
            btnFileReturn.setToolTipText("Complete tax calculation first");
        }
    }

    private void fileTaxReturn() {
        if (currentClient == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a client before filing.",
                    "No Client Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double monthly = Double.parseDouble(txtIncome.getText().trim().replace(",", ""));
            int dependents = (int) spnDependents.getValue();
            String marital = (String) cmbMarital.getSelectedItem();
            double annualTax = TaxCalculator.calculateTax(monthly, dependents);

            LocalDate today = LocalDate.now();
            LocalDate deadline = LocalDate.of(today.getYear(), 4, 30);
            String status = today.isAfter(deadline)
                    ? TaxReturn.STATUS_OVERDUE
                    : TaxReturn.STATUS_FILED;

            taxReturnService.fileTaxReturn(
                    currentClient.getId(), monthly, dependents, marital);

            JOptionPane.showMessageDialog(this,
                    String.format("TAX RETURN FILED SUCCESSFULLY!\n\n"
                            + "Client     : %s\n"
                            + "Tax ID     : %s\n"
                            + "Annual Tax : %s\n"
                            + "Filed On   : %s\n"
                            + "Status     : %s",
                            currentClient.getName(),
                            currentClient.getId(),
                            VndFormatter.format(annualTax),
                            today,
                            status),
                    "Return Saved", JOptionPane.INFORMATION_MESSAGE);

            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error filing return: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtSearchId.setText("");
        txtIncome.setText("");
        spnDependents.setValue(0);
        cmbMarital.setSelectedIndex(0);
        currentClient = null;
        lblSearchResult.setText("No client selected");
        lblSearchResult.setForeground(AppTheme.TEXT_SECONDARY);
        resetCards();
        txtReceipt.setText("<html><body style='color:#757575; font-family:Segoe UI; padding:20px; text-align:center;'>"
                + "📋 Enter a Client Tax ID above and click Load Data<br>to view detailed tax breakdown by income bracket.</body></html>");
        btnFileReturn.setEnabled(false);
        btnFileReturn.setToolTipText("Complete tax calculation first");
    }

    private void resetCards() {
        lblCardIncome.setText("0 VND");
        lblCardTax.setText("0 VND");
        lblCardNet.setText("0 VND");
    }

    // ── HELPERS ──────────────────────────────────────────────────────
    private JLabel buildCard(JPanel parent, String title, String initVal, Color c) {
        JPanel p = AppTheme.createStatCard("", title, initVal, c);
        p.setMinimumSize(new Dimension(80, 90));
        p.setPreferredSize(new Dimension(p.getPreferredSize().width, 90));
        parent.add(p);
        return (JLabel) ((BorderLayout) p.getLayout()).getLayoutComponent(BorderLayout.CENTER);
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
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
