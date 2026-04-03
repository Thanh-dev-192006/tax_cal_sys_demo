package com.oop.project.ui;

import com.oop.project.model.Client;
import com.oop.project.model.TaxReturn;
import com.oop.project.service.ClientService;
import com.oop.project.service.TaxReturnService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.TaxCalculator;
import com.oop.project.util.VndFormatter;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TaxCalculationPanel extends JPanel {

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

    private Client currentClient = null;

    // UI refs
    private JTextField txtSearchId;
    private JLabel     lblSearchResult;
    private JTextField txtIncome;
    private JSpinner   spnDependents;
    private JComboBox<String> cmbMarital;
    private JEditorPane txtReceipt;
    private JButton    btnFileReturn;

    // Summary card value labels
    private JLabel lblCardIncome;
    private JLabel lblCardTax;
    private JLabel lblCardNet;

    public TaxCalculationPanel() {
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
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titlePanel.setOpaque(false);
        ImageIcon logoIcon = loadLogo(28, 28);
        if (logoIcon != null) {
            titlePanel.add(new JLabel(logoIcon));
        }
        JLabel title = new JLabel("Tax Filing");
        title.setFont(AppTheme.FONT_H1);
        title.setForeground(AppTheme.PRIMARY_DARK_RED);
        titlePanel.add(title);
        p.add(titlePanel, BorderLayout.WEST);
        return p;
    }

    // ── BODY ─────────────────────────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout(0, 0));
        body.setBackground(AppTheme.WARM_BG);

        // North: search + summary cards + inputs
        JPanel topBlock = new JPanel(new BorderLayout(0, AppTheme.PAD_MD));
        topBlock.setBackground(AppTheme.WARM_BG);
        topBlock.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG));

        topBlock.add(buildSearchCard(), BorderLayout.NORTH);

        JPanel midRow = new JPanel(new BorderLayout(AppTheme.PAD_MD, 0));
        midRow.setBackground(AppTheme.WARM_BG);
        midRow.add(buildSummaryCards(), BorderLayout.NORTH);
        midRow.add(buildInputCard(),    BorderLayout.CENTER);
        topBlock.add(midRow, BorderLayout.CENTER);

        body.add(topBlock,          BorderLayout.NORTH);
        body.add(buildReceiptCard(), BorderLayout.CENTER);
        return body;
    }

    // ── STEP 1: SEARCH ───────────────────────────────────────────────
    private JPanel buildSearchCard() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        card.setBackground(AppTheme.WARM_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER),
                new EmptyBorder(AppTheme.PAD_SM, AppTheme.PAD_MD, AppTheme.PAD_SM, AppTheme.PAD_MD)));

        JLabel stepLabel = new JLabel("STEP 1");
        stepLabel.setFont(AppTheme.FONT_CAPTION.deriveFont(Font.BOLD));
        stepLabel.setForeground(AppTheme.SIDEBAR_ACCENT);

        JLabel sepLabel = new JLabel("·");
        sepLabel.setForeground(AppTheme.WARM_BORDER);

        JLabel instrLabel = new JLabel("Find Client by Tax ID");
        instrLabel.setFont(AppTheme.FONT_H3);
        instrLabel.setForeground(AppTheme.TEXT_PRIMARY);

        txtSearchId = new JTextField(16);
        txtSearchId.setFont(AppTheme.FONT_BODY);
        txtSearchId.putClientProperty("JTextField.placeholderText", "XXX-XX-XXXX");
        txtSearchId.addActionListener(e -> performSearch());

        JButton btnSearch = AppTheme.primaryBtn("Load Data");
        btnSearch.addActionListener(e -> performSearch());

        lblSearchResult = new JLabel("No client selected");
        lblSearchResult.setFont(AppTheme.FONT_BODY.deriveFont(Font.ITALIC));
        lblSearchResult.setForeground(AppTheme.TEXT_SECONDARY);

        JButton btnClear = AppTheme.ghostBtn("Clear");
        btnClear.addActionListener(e -> clearForm());

        card.add(stepLabel);
        card.add(sepLabel);
        card.add(instrLabel);
        card.add(Box.createHorizontalStrut(8));
        card.add(txtSearchId);
        card.add(btnSearch);
        card.add(Box.createHorizontalStrut(12));
        card.add(lblSearchResult);
        card.add(Box.createHorizontalStrut(8));
        card.add(btnClear);
        return card;
    }

    // ── SUMMARY CARDS ────────────────────────────────────────────────
    private JPanel buildSummaryCards() {
        JPanel row = new JPanel(new GridLayout(1, 3, 10, 0));
        row.setBackground(AppTheme.WARM_BG);
        row.setBorder(new EmptyBorder(0, 0, AppTheme.PAD_SM, 0));

        lblCardIncome = buildMiniCard(row, "Annual Income (VND)",    "0 VND", AppTheme.ACCENT_BLUE);
        lblCardTax    = buildMiniCard(row, "Annual Tax (VND)",       "0 VND", AppTheme.ALERT_RED);
        lblCardNet    = buildMiniCard(row, "Net Annual Income (VND)","0 VND", AppTheme.ACCENT_GREEN);
        return row;
    }

    private JLabel buildMiniCard(JPanel parent, String title, String initVal, Color accent) {
        JPanel card = AppTheme.createStatCard("", title, initVal, accent);
        card.setPreferredSize(new Dimension(0, 90));
        parent.add(card);
        return (JLabel) ((BorderLayout) card.getLayout()).getLayoutComponent(BorderLayout.CENTER);
    }

    // ── STEP 2: INPUTS ───────────────────────────────────────────────
    private JPanel buildInputCard() {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(AppTheme.WARM_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER),
                new EmptyBorder(AppTheme.PAD_MD, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG)));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(4, 0, 4, 10);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill    = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets  = new Insets(4, 0, 4, 20);

        // Step label
        JLabel stepLbl = new JLabel("STEP 2  ·  Adjust Parameters");
        stepLbl.setFont(AppTheme.FONT_H3);
        stepLbl.setForeground(AppTheme.TEXT_SECONDARY);
        GridBagConstraints hc = new GridBagConstraints();
        hc.gridx = 0; hc.gridy = 0; hc.gridwidth = 6;
        hc.anchor = GridBagConstraints.WEST;
        hc.insets = new Insets(0, 0, 10, 0);
        card.add(stepLbl, hc);

        // Row 1 — income + dependents
        lc.gridx = 0; lc.gridy = 1;
        card.add(lbl("Monthly Income (VND):"), lc);
        fc.gridx = 1; fc.gridy = 1;
        txtIncome = new JTextField(14);
        txtIncome.setFont(AppTheme.FONT_BODY);
        card.add(txtIncome, fc);

        lc.gridx = 2; lc.gridy = 1;
        card.add(lbl("Dependents:"), lc);
        fc.gridx = 3; fc.gridy = 1; fc.weightx = 0.5;
        spnDependents = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        spnDependents.setFont(AppTheme.FONT_BODY);
        card.add(spnDependents, fc);

        // Row 2 — marital + deduction info
        lc.gridx = 0; lc.gridy = 2;
        card.add(lbl("Marital Status:"), lc);
        fc.gridx = 1; fc.gridy = 2; fc.weightx = 1.0;
        cmbMarital = new JComboBox<>(new String[]{"SINGLE", "MARRIED", "DIVORCED", "WIDOWED"});
        cmbMarital.setFont(AppTheme.FONT_BODY);
        card.add(cmbMarital, fc);

        lc.gridx = 2; lc.gridy = 2; lc.gridwidth = 2;
        JLabel deductInfo = new JLabel("Personal: 11M / month  ·  Dependent: 4.4M / person");
        deductInfo.setFont(AppTheme.FONT_CAPTION);
        deductInfo.setForeground(AppTheme.ACCENT_GREEN);
        card.add(deductInfo, lc);

        // Live recalculation
        DocumentListener dl = new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { calculate(); }
            public void removeUpdate(DocumentEvent e)  { calculate(); }
            public void changedUpdate(DocumentEvent e) { calculate(); }
        };
        txtIncome.getDocument().addDocumentListener(dl);
        spnDependents.addChangeListener(e -> calculate());
        cmbMarital.addActionListener(e -> calculate());

        return card;
    }

    // ── RECEIPT + FILE BUTTON ────────────────────────────────────────
    private JPanel buildReceiptCard() {
        JPanel wrapper = new JPanel(new BorderLayout(0, 0));
        wrapper.setBackground(AppTheme.WARM_BG);
        wrapper.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_LG));

        txtReceipt = new JEditorPane();
        txtReceipt.setContentType("text/html");
        txtReceipt.setEditable(false);
        txtReceipt.setBackground(AppTheme.WARM_CARD);
        txtReceipt.setText(placeholderHtml());

        JScrollPane scroll = new JScrollPane(txtReceipt);
        scroll.setBorder(new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER));

        // Bottom bar: deadline hint + file button
        JPanel bottom = new JPanel(new BorderLayout(0, 0));
        bottom.setBackground(AppTheme.WARM_BG);
        bottom.setBorder(new EmptyBorder(AppTheme.PAD_SM, 0, 0, 0));

        JLabel deadlineHint = new JLabel("Deadline: 30 April annually  ·  Late filings marked as 'Overdue'");
        deadlineHint.setFont(AppTheme.FONT_CAPTION);
        deadlineHint.setForeground(AppTheme.WARNING_AMBER);

        btnFileReturn = new JButton("File Tax Return  (Save & Finish)");
        btnFileReturn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnFileReturn.setBackground(AppTheme.ACCENT_GREEN);
        btnFileReturn.setForeground(Color.WHITE);
        btnFileReturn.setBorderPainted(false);
        btnFileReturn.setFocusPainted(false);
        btnFileReturn.setPreferredSize(new Dimension(280, 42));
        btnFileReturn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFileReturn.setEnabled(false);
        btnFileReturn.setToolTipText("Complete tax calculation first");
        btnFileReturn.addActionListener(e -> fileTaxReturn());

        bottom.add(deadlineHint,  BorderLayout.WEST);
        bottom.add(btnFileReturn, BorderLayout.EAST);

        wrapper.add(scroll,  BorderLayout.CENTER);
        wrapper.add(bottom,  BorderLayout.SOUTH);
        return wrapper;
    }

    // ── LOGIC ─────────────────────────────────────────────────────────
    private void performSearch() {
        String id = txtSearchId.getText().trim();
        if (id.isEmpty()) return;

        currentClient = clientService.findClientById(id);
        if (currentClient != null) {
            lblSearchResult.setText("Found: " + currentClient.getName() + "  ·  " + currentClient.getCity());
            lblSearchResult.setForeground(AppTheme.ACCENT_GREEN);

            txtIncome.setText(String.format("%.0f", currentClient.getIncome()));
            spnDependents.setValue(currentClient.getDependents());
            String status = currentClient.getMaritalStatus();
            if (status != null) cmbMarital.setSelectedItem(status.toUpperCase());

            calculate();
        } else {
            lblSearchResult.setText("Not found: " + id);
            lblSearchResult.setForeground(AppTheme.ALERT_RED);
            JOptionPane.showMessageDialog(this,
                    "No client found with Tax ID: " + id, "Not Found", JOptionPane.WARNING_MESSAGE);
            currentClient = null;
        }
    }

    private void calculate() {
        try {
            String text = txtIncome.getText().trim().replace(",", "");
            if (text.isEmpty()) {
                resetCards();
                txtReceipt.setText(placeholderHtml());
                btnFileReturn.setEnabled(false);
                return;
            }

            double monthly    = Double.parseDouble(text);
            int    dependents = (int) spnDependents.getValue();
            String marital    = (String) cmbMarital.getSelectedItem();

            double annualTax = TaxCalculator.calculateTax(monthly, dependents);
            double annualInc = monthly * 12;
            double net       = annualInc - annualTax;

            lblCardIncome.setText(VndFormatter.formatShort(annualInc));
            lblCardTax.setText(VndFormatter.formatShort(annualTax));
            lblCardNet.setText(VndFormatter.formatShort(net));

            txtReceipt.setText(TaxCalculator.generateHTMLReceipt(monthly, dependents, marital));
            txtReceipt.setCaretPosition(0);

            boolean canFile = currentClient != null;
            btnFileReturn.setEnabled(canFile);
            btnFileReturn.setToolTipText(canFile ? "Click to file tax return" : "Load a client first");

        } catch (NumberFormatException e) {
            resetCards();
            txtReceipt.setText("<html><body style='color:#A52820; font-family:Segoe UI; padding:16px; text-align:center;'>"
                    + "<b>Invalid input.</b> Please enter numbers only.</body></html>");
            btnFileReturn.setEnabled(false);
        }
    }

    private void fileTaxReturn() {
        if (currentClient == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a client before filing.", "No Client Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            double monthly    = Double.parseDouble(txtIncome.getText().trim().replace(",", ""));
            int    dependents = (int) spnDependents.getValue();
            String marital    = (String) cmbMarital.getSelectedItem();
            double annualTax  = TaxCalculator.calculateTax(monthly, dependents);

            LocalDate today    = LocalDate.now();
            LocalDate deadline = LocalDate.of(today.getYear(), 4, 30);
            String status = today.isAfter(deadline) ? TaxReturn.STATUS_OVERDUE : TaxReturn.STATUS_FILED;

            taxReturnService.fileTaxReturn(currentClient.getId(), monthly, dependents, marital);

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
                            today, status),
                    "Return Saved", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error filing return: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        txtReceipt.setText(placeholderHtml());
        btnFileReturn.setEnabled(false);
        btnFileReturn.setToolTipText("Complete tax calculation first");
    }

    private void resetCards() {
        lblCardIncome.setText("0 VND");
        lblCardTax.setText("0 VND");
        lblCardNet.setText("0 VND");
    }

    private String placeholderHtml() {
        return "<html><body style='color:#7A7265; font-family:Segoe UI; padding:28px; text-align:center;'>" 
                + "Enter a Client Tax ID above and click <b>Load Data</b><br>" 
                + "to view the detailed tax breakdown by income bracket.</body></html>"; 
    }

    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        l.setForeground(AppTheme.TEXT_PRIMARY);
        return l;
    }
}
