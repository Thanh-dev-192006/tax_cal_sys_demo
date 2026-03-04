package com.oop.project.ui;

import com.oop.project.exception.InvalidDataException;
import com.oop.project.model.Client;
import com.oop.project.service.ClientService;
import com.oop.project.util.AppTheme;
import com.oop.project.util.CsvExporter;
import com.oop.project.util.VndFormatter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class ClientPanel extends JPanel {

    private final ClientService clientService = new ClientService();

    // Form fields
    private JTextField   fldId, fldName, fldIncome, fldPhone, fldEmail, fldCity;
    private JComboBox<String> cmbMarital;
    private JSpinner     spnDependents;

    // Table
    private JTable                      table;
    private DefaultTableModel           tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    // Search
    private JTextField fldSearch;

    public ClientPanel() {
        initializeUI();
        loadClients();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BACKGROUND);
        add(buildFormPanel(),  BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
    }

    // ── FORM ─────────────────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.BACKGROUND);
        wrapper.setBorder(new EmptyBorder(10, 12, 6, 12));

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.PRIMARY_BLUE, 1),
                "Client Information");
        border.setTitleFont(AppTheme.FONT_SUBTITLE);
        border.setTitleColor(AppTheme.PRIMARY_BLUE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(AppTheme.CARD_BG);
        form.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(8, 12, 8, 12)));

        GridBagConstraints lc = new GridBagConstraints();
        lc.anchor = GridBagConstraints.WEST;
        lc.insets = new Insets(4, 4, 4, 6);

        GridBagConstraints fc = new GridBagConstraints();
        fc.fill    = GridBagConstraints.HORIZONTAL;
        fc.weightx = 1.0;
        fc.insets  = new Insets(4, 0, 4, 12);

        // Row 0 — Tax ID | Name
        lc.gridx = 0; lc.gridy = 0; form.add(lbl("Tax ID (XXX-XX-XXXX):"), lc);
        fc.gridx = 1; fc.gridy = 0;
        fldId = new JTextField(14); form.add(fldId, fc);

        lc.gridx = 2; lc.gridy = 0; form.add(lbl("Full Name:"), lc);
        fc.gridx = 3; fc.gridy = 0;
        fldName = new JTextField(14); form.add(fldName, fc);

        // Row 1 — Income | Marital Status
        lc.gridx = 0; lc.gridy = 1; form.add(lbl("Monthly Income (VND):"), lc);
        fc.gridx = 1; fc.gridy = 1;
        fldIncome = new JTextField(14); form.add(fldIncome, fc);

        lc.gridx = 2; lc.gridy = 1; form.add(lbl("Marital Status:"), lc);
        fc.gridx = 3; fc.gridy = 1;
        cmbMarital = new JComboBox<>(new String[]{"SINGLE", "MARRIED", "DIVORCED", "WIDOWED"});
        cmbMarital.setFont(AppTheme.FONT_BODY);
        form.add(cmbMarital, fc);

        // Row 2 — Dependents | Phone
        lc.gridx = 0; lc.gridy = 2; form.add(lbl("Dependents:"), lc);
        fc.gridx = 1; fc.gridy = 2;
        spnDependents = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
        spnDependents.setFont(AppTheme.FONT_BODY);
        form.add(spnDependents, fc);

        lc.gridx = 2; lc.gridy = 2; form.add(lbl("Phone Number:"), lc);
        fc.gridx = 3; fc.gridy = 2;
        fldPhone = new JTextField(14); form.add(fldPhone, fc);

        // Row 3 — Email | City
        lc.gridx = 0; lc.gridy = 3; form.add(lbl("Email:"), lc);
        fc.gridx = 1; fc.gridy = 3;
        fldEmail = new JTextField(14); form.add(fldEmail, fc);

        lc.gridx = 2; lc.gridy = 3; form.add(lbl("City / Province:"), lc);
        fc.gridx = 3; fc.gridy = 3;
        fldCity = new JTextField(14); form.add(fldCity, fc);

        // Row 4 — Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setOpaque(false);

        JButton btnAdd    = styledBtn("+ Add New",     AppTheme.ACCENT_GREEN);
        JButton btnUpdate = styledBtn("Save Update",   AppTheme.PRIMARY_BLUE);
        JButton btnDelete = styledBtn("Delete",        AppTheme.ALERT_RED);
        JButton btnClear  = styledBtn("Clear Form",    AppTheme.TEXT_SECONDARY);
        JButton btnCsv    = styledBtn("Export CSV",    AppTheme.WARNING_AMBER);

        btnAdd.addActionListener(e    -> addClient());
        btnUpdate.addActionListener(e -> updateClient());
        btnDelete.addActionListener(e -> deleteClient());
        btnClear.addActionListener(e  -> clearForm());
        btnCsv.addActionListener(e    -> CsvExporter.exportClients(clientService.getAllClients(), this));

        btnRow.add(btnAdd);
        btnRow.add(btnUpdate);
        btnRow.add(btnDelete);
        btnRow.add(btnClear);
        btnRow.add(Box.createHorizontalStrut(20));
        btnRow.add(btnCsv);

        GridBagConstraints bc = new GridBagConstraints();
        bc.gridx = 0; bc.gridy = 4; bc.gridwidth = 4;
        bc.insets  = new Insets(8, 0, 4, 0);
        bc.anchor  = GridBagConstraints.WEST;
        form.add(btnRow, bc);

        wrapper.add(form, BorderLayout.CENTER);
        return wrapper;
    }

    // ── TABLE ────────────────────────────────────────────────────────
    private JPanel buildTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setBorder(new EmptyBorder(0, 12, 12, 12));

        // Search bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        searchBar.setBackground(AppTheme.BACKGROUND);
        searchBar.add(lbl("Search:"));
        fldSearch = new JTextField(24);
        fldSearch.setFont(AppTheme.FONT_BODY);
        fldSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override public void insertUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e)  { applyFilter(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { applyFilter(); }
        });
        searchBar.add(fldSearch);

        JLabel hint = new JLabel("(search by name, tax ID, marital status, city)");
        hint.setFont(AppTheme.FONT_SMALL);
        hint.setForeground(AppTheme.TEXT_SECONDARY);
        searchBar.add(hint);

        // Table
        String[] cols = {
            "Tax ID", "Full Name", "Income (VND/month)",
            "Dependents", "Marital Status", "Email", "Phone", "City"
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
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        int[] widths = {120, 160, 150, 80, 110, 180, 110, 90};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        // Click row → fill form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0)
                populateFormFromRow(table.convertRowIndexToModel(table.getSelectedRow()));
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));

        panel.add(searchBar, BorderLayout.NORTH);
        panel.add(scroll,    BorderLayout.CENTER);
        return panel;
    }

    // ── CRUD ─────────────────────────────────────────────────────────
    private void addClient() {
        try {
            clientService.addClient(buildClientFromForm());
            loadClients();
            clearForm();
            JOptionPane.showMessageDialog(this,
                    "Client added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (InvalidDataException | NumberFormatException ex) {
            showError(ex.getMessage());
        }
    }

    private void updateClient() {
        try {
            clientService.updateClient(buildClientFromForm());
            loadClients();
            JOptionPane.showMessageDialog(this,
                    "Client updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (InvalidDataException | NumberFormatException ex) {
            showError(ex.getMessage());
        }
    }

    private void deleteClient() {
        String id = fldId.getText().trim();
        if (id.isEmpty()) {
            showError("Please enter the Tax ID of the client you want to delete.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to DELETE client:\n"
                        + fldName.getText() + "  (" + id + ")?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            clientService.deleteClient(id);
            loadClients();
            clearForm();
            JOptionPane.showMessageDialog(this,
                    "Client deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ── DATA ─────────────────────────────────────────────────────────
    private void loadClients() {
        tableModel.setRowCount(0);
        for (Client c : clientService.getAllClients()) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    VndFormatter.formatNumberOnly(c.getIncome()),
                    c.getDependents(),
                    c.getMaritalStatus(),
                    c.getEmail(),
                    c.getPhoneNumber(),
                    c.getCity()
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
        cmbMarital.setSelectedItem(c.getMaritalStatus() != null
                ? c.getMaritalStatus().toUpperCase() : "SINGLE");
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

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
