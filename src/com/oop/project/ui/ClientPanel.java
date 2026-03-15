package com.oop.project.ui;

import com.oop.project.exception.InvalidDataException;
import com.oop.project.model.Client;
import com.oop.project.service.ClientService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.CsvExporter;
import com.oop.project.util.VndFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {

    private final ClientService clientService = new ClientService();

    // Form fields
    private JTextField fldId, fldName, fldIncome, fldPhone, fldEmail, fldCity;
    private JComboBox<String> cmbMarital;
    private JSpinner spnDependents;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField fldSearch;

    public ClientPanel() {
        initializeUI();
        loadClients();
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

        JLabel title = new JLabel("Client Management");
        title.setFont(AppTheme.FONT_H1);
        title.setForeground(AppTheme.TEXT_PRIMARY);
        p.add(title, BorderLayout.WEST);
        return p;
    }

    // ── BODY: form card + table ───────────────────────────────────────
    private JPanel buildBody() {
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(AppTheme.WARM_BG);
        body.add(buildFormCard(),  BorderLayout.NORTH);
        body.add(buildTableCard(), BorderLayout.CENTER);
        return body;
    }

    // ── FORM CARD ────────────────────────────────────────────────────
    private JPanel buildFormCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.WARM_BG);
        wrapper.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG));

        JPanel card = new JPanel(new BorderLayout(0, AppTheme.PAD_MD));
        card.setBackground(AppTheme.WARM_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, AppTheme.WARM_BORDER),
                new EmptyBorder(AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG)));

        // Card header
        JLabel cardTitle = new JLabel("Client Information");
        cardTitle.setFont(AppTheme.FONT_H3);
        cardTitle.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel sep = new JPanel();
        sep.setBackground(AppTheme.WARM_BORDER);
        sep.setPreferredSize(new Dimension(0, 1));

        JPanel cardHead = new JPanel(new BorderLayout(0, AppTheme.PAD_SM));
        cardHead.setOpaque(false);
        cardHead.add(cardTitle, BorderLayout.NORTH);
        cardHead.add(sep,       BorderLayout.SOUTH);

        card.add(cardHead,       BorderLayout.NORTH);
        card.add(buildFormGrid(), BorderLayout.CENTER);
        card.add(buildBtnRow(),  BorderLayout.SOUTH);

        wrapper.add(card, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildFormGrid() {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(5, 0, 5, 10);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill    = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets  = new Insets(5, 0, 5, 20);

        // Row 0 — Tax ID | Name
        lc.gridx = 0; lc.gridy = 0;
        grid.add(lbl("Tax ID (XXX-XX-XXXX):"), lc);
        fc.gridx = 1; fc.gridy = 0;
        fldId = field("e.g. 123-45-6789", 14);
        grid.add(fldId, fc);

        lc.gridx = 2; lc.gridy = 0;
        grid.add(lbl("Full Name:"), lc);
        fc.gridx = 3; fc.gridy = 0;
        fldName = field("Full name", 14);
        grid.add(fldName, fc);

        // Row 1 — Income | Marital
        lc.gridx = 0; lc.gridy = 1;
        grid.add(lbl("Monthly Income (VND):"), lc);
        fc.gridx = 1; fc.gridy = 1;
        JPanel incPanel = new JPanel(new BorderLayout(0, 2));
        incPanel.setOpaque(false);
        fldIncome = field("e.g. 15000000", 14);
        JLabel hint = new JLabel("No commas — e.g. 15000000 = 15 triệu");
        hint.setFont(AppTheme.FONT_CAPTION);
        hint.setForeground(AppTheme.WARNING_AMBER);
        incPanel.add(fldIncome, BorderLayout.CENTER);
        incPanel.add(hint,      BorderLayout.SOUTH);
        grid.add(incPanel, fc);

        lc.gridx = 2; lc.gridy = 1;
        grid.add(lbl("Marital Status:"), lc);
        fc.gridx = 3; fc.gridy = 1;
        cmbMarital = new JComboBox<>(new String[]{"SINGLE", "MARRIED", "DIVORCED", "WIDOWED"});
        cmbMarital.setFont(AppTheme.FONT_BODY);
        grid.add(cmbMarital, fc);

        // Row 2 — Dependents | Phone
        lc.gridx = 0; lc.gridy = 2;
        grid.add(lbl("Dependents:"), lc);
        fc.gridx = 1; fc.gridy = 2;
        spnDependents = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        spnDependents.setFont(AppTheme.FONT_BODY);
        grid.add(spnDependents, fc);

        lc.gridx = 2; lc.gridy = 2;
        grid.add(lbl("Phone Number:"), lc);
        fc.gridx = 3; fc.gridy = 2;
        fldPhone = field("e.g. 0912345678", 14);
        grid.add(fldPhone, fc);

        // Row 3 — Email | City
        lc.gridx = 0; lc.gridy = 3;
        grid.add(lbl("Email:"), lc);
        fc.gridx = 1; fc.gridy = 3;
        fldEmail = field("name@email.com", 14);
        grid.add(fldEmail, fc);

        lc.gridx = 2; lc.gridy = 3;
        grid.add(lbl("City / Province:"), lc);
        fc.gridx = 3; fc.gridy = 3;
        fldCity = field("Ha Noi, Ho Chi Minh...", 14);
        grid.add(fldCity, fc);

        return grid;
    }

    private JPanel buildBtnRow() {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(AppTheme.PAD_SM, 0, 0, 0));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);

        JButton btnAdd    = AppTheme.successBtn("+ Add New");
        JButton btnUpdate = AppTheme.primaryBtn("Save Update");
        JButton btnDelete = AppTheme.dangerBtn("Delete");
        JButton btnClear  = AppTheme.ghostBtn("Clear Form");

        btnAdd.addActionListener(e    -> addClient());
        btnUpdate.addActionListener(e -> updateClient());
        btnDelete.addActionListener(e -> deleteClient());
        btnClear.addActionListener(e  -> clearForm());

        left.add(btnAdd);
        left.add(btnUpdate);
        left.add(btnDelete);
        left.add(btnClear);

        JButton btnCsv = AppTheme.warnBtn("Export CSV");
        btnCsv.addActionListener(e -> CsvExporter.exportClients(clientService.getAllClients(), this));

        row.add(left,   BorderLayout.WEST);
        row.add(btnCsv, BorderLayout.EAST);
        return row;
    }

    // ── TABLE CARD ───────────────────────────────────────────────────
    private JPanel buildTableCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.WARM_BG);
        wrapper.setBorder(new EmptyBorder(0, AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_LG));

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        searchBar.setBackground(AppTheme.WARM_CARD);
        searchBar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 0, 1, AppTheme.WARM_BORDER),
                new EmptyBorder(4, 8, 4, 8)));

        searchBar.add(lbl("Search:"));
        fldSearch = new JTextField(24);
        fldSearch.setFont(AppTheme.FONT_BODY);
        fldSearch.putClientProperty("JTextField.placeholderText", "Name, Tax ID, city, marital status...");
        fldSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });
        searchBar.add(fldSearch);

        // Table
        String[] cols = {"Tax ID", "Full Name", "Income (VND/month)", "Dependents", "Marital Status", "Email", "Phone", "City"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int col) {
                Component c = super.prepareRenderer(renderer, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(AppTheme.TABLE_SELECTION_BG);
                    c.setForeground(AppTheme.TABLE_SELECTION_FG);
                } else {
                    c.setBackground(row % 2 == 0 ? AppTheme.WARM_CARD : AppTheme.WARM_STRIPE);
                    c.setForeground(AppTheme.TEXT_PRIMARY);
                }
                return c;
            }
        };
        AppTheme.styleTable(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        int[] widths = {110, 170, 140, 85, 105, 170, 110, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                populateFormFromRow(table.convertRowIndexToModel(table.getSelectedRow()));
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new MatteBorder(0, 1, 1, 1, AppTheme.WARM_BORDER));

        wrapper.add(searchBar, BorderLayout.NORTH);
        wrapper.add(scroll,    BorderLayout.CENTER);
        return wrapper;
    }

    // ── CRUD ─────────────────────────────────────────────────────────
    private void addClient() {
        try {
            clientService.addClient(buildClientFromForm());
            loadClients();
            clearForm();
            JOptionPane.showMessageDialog(this, "Client added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (InvalidDataException | NumberFormatException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateClient() {
        try {
            clientService.updateClient(buildClientFromForm());
            loadClients();
            JOptionPane.showMessageDialog(this, "Client updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (InvalidDataException | NumberFormatException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteClient() {
        String id = fldId.getText().trim();
        if (id.isEmpty()) { showError("Please enter the Tax ID of the client to delete."); return; }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete client:\n" + fldName.getText() + "  (" + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            clientService.deleteClient(id);
            loadClients();
            clearForm();
            JOptionPane.showMessageDialog(this, "Client deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ── DATA ─────────────────────────────────────────────────────────
    private void loadClients() {
        tableModel.setRowCount(0);
        for (Client c : clientService.getAllClients()) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getName(),
                    VndFormatter.formatNumberOnly(c.getIncome()),
                    c.getDependents(), c.getMaritalStatus(),
                    c.getEmail(), c.getPhoneNumber(), c.getCity()
            });
        }
    }

    private void populateFormFromRow(int modelRow) {
        List<Client> clients = clientService.getAllClients();
        if (modelRow < 0 || modelRow >= clients.size()) return;
        Client c = clients.get(modelRow);
        fldId.setText(c.getId());
        fldName.setText(c.getName());
        fldIncome.setText(String.format("%.0f", c.getIncome()));
        spnDependents.setValue(c.getDependents());
        cmbMarital.setSelectedItem(c.getMaritalStatus() != null ? c.getMaritalStatus().toUpperCase() : "SINGLE");
        fldPhone.setText(c.getPhoneNumber());
        fldEmail.setText(c.getEmail());
        fldCity.setText(c.getCity());
    }

    private void applyFilter() {
        String text = fldSearch.getText().trim();
        if (text.isEmpty()) { sorter.setRowFilter(null); return; }
        try {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1, 4, 5, 7));
        } catch (java.util.regex.PatternSyntaxException ignored) {
            sorter.setRowFilter(null);
        }
    }

    private Client buildClientFromForm() throws InvalidDataException {
        String id   = fldId.getText().trim();
        String name = fldName.getText().trim();
        if (id.isEmpty())   throw new InvalidDataException("Tax ID is required.");
        if (name.isEmpty()) throw new InvalidDataException("Full name is required.");
        double income;
        try {
            income = Double.parseDouble(fldIncome.getText().trim().replace(",", ""));
        } catch (NumberFormatException e) {
            throw new InvalidDataException("Invalid income value. Please enter a number.");
        }
        if (income < 0) throw new InvalidDataException("Income cannot be negative.");
        return new Client(id, name, income,
                (int) spnDependents.getValue(),
                (String) cmbMarital.getSelectedItem(),
                fldEmail.getText().trim(),
                fldPhone.getText().trim(),
                fldCity.getText().trim());
    }

    private void clearForm() {
        fldId.setText(""); fldName.setText(""); fldIncome.setText("");
        fldPhone.setText(""); fldEmail.setText(""); fldCity.setText("");
        spnDependents.setValue(0);
        cmbMarital.setSelectedIndex(0);
        table.clearSelection();
    }

    // ── HELPERS ──────────────────────────────────────────────────────
    private JLabel lbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        l.setForeground(AppTheme.TEXT_PRIMARY);
        return l;
    }

    private JTextField field(String placeholder, int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(AppTheme.FONT_BODY);
        f.putClientProperty("JTextField.placeholderText", placeholder);
        return f;
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
