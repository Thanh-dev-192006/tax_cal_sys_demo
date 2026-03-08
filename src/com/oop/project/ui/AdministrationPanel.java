package com.oop.project.ui;

import com.oop.project.model.User;
import com.oop.project.repository.UserRepository;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdministrationPanel extends JPanel {

    private final User currentUser;
    private final UserRepository userRepository;
    private JTable table;
    private UserTableModel tableModel;

    public AdministrationPanel(User user) {
        this.currentUser = user;
        this.userRepository = new UserRepository();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(AppTheme.BACKGROUND);
        setBorder(new EmptyBorder(16, 20, 16, 20));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(0, 4));
        topPanel.setBackground(AppTheme.BACKGROUND);

        JLabel lblTitle = new JLabel("System Administration  (Admin Only)");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.PRIMARY_BLUE);

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String info = String.format("Logged in as: %s  |  Role: %s  |  Session: %s",
                currentUser.getUsername(), currentUser.getRole(), time);
        JLabel lblInfo = new JLabel(info);
        lblInfo.setFont(AppTheme.FONT_BODY);
        lblInfo.setForeground(AppTheme.TEXT_SECONDARY);

        JButton btnRefresh = styledBtn("Refresh", AppTheme.PRIMARY_BLUE);
        btnRefresh.addActionListener(e -> loadData());

        JPanel titleRow = new JPanel(new BorderLayout());
        titleRow.setBackground(AppTheme.BACKGROUND);
        titleRow.add(lblTitle, BorderLayout.WEST);
        titleRow.add(btnRefresh, BorderLayout.EAST);

        topPanel.add(titleRow, BorderLayout.NORTH);
        topPanel.add(lblInfo, BorderLayout.SOUTH);
        return topPanel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new UserTableModel();
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (column != 3) {
                    if (isRowSelected(row)) {
                        c.setBackground(AppTheme.SELECTION_BG);
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xF9, 0xFA));
                        c.setForeground(AppTheme.TEXT_PRIMARY);
                    }
                }
                return c;
            }
        };
        table.setFont(AppTheme.FONT_BODY);
        table.getTableHeader().setFont(AppTheme.FONT_BODY.deriveFont(Font.BOLD));
        table.setRowHeight(36);
        table.setGridColor(AppTheme.BORDER_COLOR);
        table.setSelectionBackground(AppTheme.SELECTION_BG);
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);

        boolean isAdmin = "ADMIN".equals(currentUser.getRole());
        ActionsPanel actionsPanel = new ActionsPanel(isAdmin);
        table.getColumnModel().getColumn(3).setCellRenderer(actionsPanel);
        table.getColumnModel().getColumn(3).setCellEditor(actionsPanel);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        return scroll;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setBorder(new EmptyBorder(8, 0, 0, 0));

        if (!"ADMIN".equals(currentUser.getRole())) {
            JLabel msg = new JLabel("Admin privileges required to manage users.");
            msg.setFont(AppTheme.FONT_BODY);
            msg.setForeground(AppTheme.ALERT_RED);
            panel.add(msg, BorderLayout.WEST);
            return panel;
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(AppTheme.BACKGROUND);

        JButton btnAdd = styledBtn("+ Add User", AppTheme.ACCENT_GREEN);
        JButton btnReset = styledBtn("Reset Password", AppTheme.WARNING_AMBER);

        btnAdd.addActionListener(e -> showAddUserDialog());
        btnReset.addActionListener(e -> showResetPasswordDialog());

        btnPanel.add(btnAdd);
        btnPanel.add(btnReset);
        panel.add(btnPanel, BorderLayout.EAST);
        return panel;
    }

    // ── DATA ──────────────────────────────────────────────────────────
    public void loadData() {
        List<User> users = userRepository.loadUsers();
        tableModel.setUsers(users);
    }

    // ── DIALOGS ───────────────────────────────────────────────────────
    private void showAddUserDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Add User", ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(400, 290);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(new EmptyBorder(16, 20, 8, 20));

        JTextField fldUsername = new JTextField();
        JTextField fldFullName = new JTextField();
        JPasswordField fldPassword = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"TAX_STAFF", "ADMIN"});

        form.add(new JLabel("Username:")); form.add(fldUsername);
        form.add(new JLabel("Full Name:")); form.add(fldFullName);
        form.add(new JLabel("Password:")); form.add(fldPassword);
        form.add(new JLabel("Role:")); form.add(cmbRole);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSave = styledBtn("Save", AppTheme.ACCENT_GREEN);
        JButton btnCancel = styledBtn("Cancel", AppTheme.TEXT_SECONDARY);

        btnSave.addActionListener(e -> {
            String username = fldUsername.getText().trim();
            String fullName = fldFullName.getText().trim();
            String password = new String(fldPassword.getPassword()).trim();
            String role = (String) cmbRole.getSelectedItem();

            if (username.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (userRepository.findUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(dlg, "Username already exists.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<User> existing = userRepository.loadUsers();
            String staffId = String.format("NV%03d", existing.size() + 1);
            User newUser = new User(staffId, username, password, role, fullName, "", "");
            userRepository.addUser(newUser);
            loadData();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "User '" + username + "' added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    void showEditUserDialog(int modelRow) {
        List<User> users = userRepository.loadUsers();
        if (modelRow < 0 || modelRow >= users.size()) return;
        User u = users.get(modelRow);

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Edit User", ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(400, 250);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(new EmptyBorder(16, 20, 8, 20));

        JTextField fldUsernameRO = new JTextField(u.getUsername());
        fldUsernameRO.setEditable(false);
        fldUsernameRO.setBackground(new Color(0xF0, 0xF0, 0xF0));
        JTextField fldFullName = new JTextField(u.getFullName());
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"TAX_STAFF", "ADMIN"});
        cmbRole.setSelectedItem(u.getRole());

        form.add(new JLabel("Username (read-only):")); form.add(fldUsernameRO);
        form.add(new JLabel("Full Name:")); form.add(fldFullName);
        form.add(new JLabel("Role:")); form.add(cmbRole);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSave = styledBtn("Save Changes", AppTheme.PRIMARY_BLUE);
        JButton btnCancel = styledBtn("Cancel", AppTheme.TEXT_SECONDARY);

        btnSave.addActionListener(e -> {
            String fullName = fldFullName.getText().trim();
            String role = (String) cmbRole.getSelectedItem();
            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Full name cannot be empty.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            userRepository.updateUser(u.getUsername(), fullName, role);
            loadData();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "User updated successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    void confirmDeleteUser(int modelRow) {
        List<User> users = userRepository.loadUsers();
        if (modelRow < 0 || modelRow >= users.size()) return;
        User u = users.get(modelRow);

        if (u.getUsername().equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user: " + u.getUsername() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            userRepository.deleteUser(u.getUsername());
            loadData();
            JOptionPane.showMessageDialog(this, "User deleted.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showResetPasswordDialog() {
        List<User> users = userRepository.loadUsers();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found.", "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Reset Password", ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(400, 230);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(new EmptyBorder(16, 20, 8, 20));

        String[] usernames = users.stream().map(User::getUsername).toArray(String[]::new);
        JComboBox<String> cmbUser = new JComboBox<>(usernames);
        JPasswordField fldNew = new JPasswordField();
        JPasswordField fldConfirm = new JPasswordField();

        form.add(new JLabel("Select User:")); form.add(cmbUser);
        form.add(new JLabel("New Password:")); form.add(fldNew);
        form.add(new JLabel("Confirm Password:")); form.add(fldConfirm);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnReset = styledBtn("Reset", AppTheme.WARNING_AMBER);
        JButton btnCancel = styledBtn("Cancel", AppTheme.TEXT_SECONDARY);

        btnReset.addActionListener(e -> {
            String username = (String) cmbUser.getSelectedItem();
            String newPwd = new String(fldNew.getPassword());
            String confirm = new String(fldConfirm.getPassword());

            if (newPwd.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Password cannot be empty.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPwd.equals(confirm)) {
                JOptionPane.showMessageDialog(dlg, "Passwords do not match.", "Validation Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            userRepository.updatePassword(username, newPwd);
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Password reset successfully for " + username + ".",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        btnPanel.add(btnReset);
        btnPanel.add(btnCancel);

        dlg.add(form, BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── TABLE MODEL ───────────────────────────────────────────────────
    private class UserTableModel extends AbstractTableModel {
        private final String[] COLS = {"Username", "Full Name", "Role", "Actions"};
        private List<User> users = new ArrayList<>();

        void setUsers(List<User> users) {
            this.users = new ArrayList<>(users);
            fireTableDataChanged();
        }

        List<User> getUsers() { return users; }

        @Override public int getRowCount() { return users.size(); }
        @Override public int getColumnCount() { return COLS.length; }
        @Override public String getColumnName(int col) { return COLS[col]; }
        @Override public boolean isCellEditable(int row, int col) { return col == 3; }

        @Override
        public Object getValueAt(int row, int col) {
            User u = users.get(row);
            switch (col) {
                case 0: return u.getUsername();
                case 1: return u.getFullName();
                case 2: return u.getRole();
                case 3: return "actions";
                default: return "";
            }
        }
    }

    // ── ACTIONS COLUMN RENDERER/EDITOR ────────────────────────────────
    private class ActionsPanel extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
        private final JPanel panel;
        private final JButton btnEdit;
        private final JButton btnDelete;
        private int currentRow = -1;

        ActionsPanel(boolean isAdmin) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 4));
            panel.setOpaque(true);

            btnEdit = new JButton("Edit");
            btnEdit.setFont(AppTheme.FONT_SMALL.deriveFont(Font.BOLD));
            btnEdit.setBackground(AppTheme.PRIMARY_BLUE);
            btnEdit.setForeground(Color.WHITE);
            btnEdit.setFocusPainted(false);
            btnEdit.setBorder(new EmptyBorder(4, 10, 4, 10));
            btnEdit.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btnDelete = new JButton("Delete");
            btnDelete.setFont(AppTheme.FONT_SMALL.deriveFont(Font.BOLD));
            btnDelete.setBackground(AppTheme.ALERT_RED);
            btnDelete.setForeground(Color.WHITE);
            btnDelete.setFocusPainted(false);
            btnDelete.setBorder(new EmptyBorder(4, 10, 4, 10));
            btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btnEdit.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow >= 0) showEditUserDialog(currentRow);
            });
            btnDelete.addActionListener(e -> {
                fireEditingStopped();
                if (currentRow >= 0) confirmDeleteUser(currentRow);
            });

            if (isAdmin) {
                panel.add(btnEdit);
                panel.add(btnDelete);
            } else {
                JLabel noAccess = new JLabel("No access");
                noAccess.setForeground(AppTheme.TEXT_SECONDARY);
                noAccess.setFont(AppTheme.FONT_SMALL);
                panel.add(noAccess);
            }
        }

        private JPanel preparePanel(int row, boolean isSelected) {
            currentRow = row;
            Color bg = isSelected ? AppTheme.SELECTION_BG
                    : (row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xF9, 0xFA));
            panel.setBackground(bg);
            return panel;
        }

        @Override
        public Component getTableCellRendererComponent(JTable tbl, Object val,
                boolean isSelected, boolean hasFocus, int row, int col) {
            return preparePanel(row, isSelected);
        }

        @Override
        public Component getTableCellEditorComponent(JTable tbl, Object val,
                boolean isSelected, int row, int col) {
            return preparePanel(row, true);
        }

        @Override
        public Object getCellEditorValue() { return null; }
    }

    // ── HELPERS ───────────────────────────────────────────────────────
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
