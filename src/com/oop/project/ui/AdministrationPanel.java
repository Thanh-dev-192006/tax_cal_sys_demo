package com.oop.project.ui;

import com.oop.project.model.User;
import com.oop.project.repository.UserRepository;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
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

    private final User           currentUser;
    private final UserRepository userRepository;
    private JTable         table;
    private UserTableModel tableModel;

    public AdministrationPanel(User user) {
        this.currentUser    = user;
        this.userRepository = new UserRepository();
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.WARM_BG);

        add(buildPageHeader(),  BorderLayout.NORTH);
        add(buildTableCard(),   BorderLayout.CENTER);
        add(buildBottomBar(),   BorderLayout.SOUTH);
    }

    // ── PAGE HEADER ──────────────────────────────────────────────────
    private JPanel buildPageHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.WARM_BG);
        p.setBorder(new EmptyBorder(AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_MD, AppTheme.PAD_LG));

        JLabel title = new JLabel("Administration");
        title.setFont(AppTheme.FONT_H1);
        title.setForeground(AppTheme.TEXT_PRIMARY);

        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        JLabel info = new JLabel("Session: " + currentUser.getUsername() + "  ·  " + currentUser.getRole() + "  ·  " + time);
        info.setFont(AppTheme.FONT_CAPTION);
        info.setForeground(AppTheme.TEXT_SECONDARY);

        JButton btnRefresh = AppTheme.primaryBtn("Refresh");
        btnRefresh.addActionListener(e -> loadData());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
        right.add(btnRefresh);

        JPanel left = new JPanel(new BorderLayout(0, 4));
        left.setOpaque(false);
        left.add(title, BorderLayout.NORTH);
        left.add(info,  BorderLayout.SOUTH);

        p.add(left,  BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    // ── TABLE CARD ───────────────────────────────────────────────────
    private JScrollPane buildTableCard() {
        tableModel = new UserTableModel();
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
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
        table.setRowHeight(38);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);

        boolean isAdmin = "ADMIN".equals(currentUser.getRole());
        ActionsPanel actions = new ActionsPanel(isAdmin);
        table.getColumnModel().getColumn(3).setCellRenderer(actions);
        table.getColumnModel().getColumn(3).setCellEditor(actions);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new MatteBorder(0, AppTheme.PAD_LG, 0, AppTheme.PAD_LG, AppTheme.WARM_BG));
        return scroll;
    }

    // ── BOTTOM BAR ───────────────────────────────────────────────────
    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppTheme.WARM_BG);
        bar.setBorder(new EmptyBorder(AppTheme.PAD_SM, AppTheme.PAD_LG, AppTheme.PAD_LG, AppTheme.PAD_LG));

        if (!"ADMIN".equals(currentUser.getRole())) {
            JLabel msg = new JLabel("Admin privileges required to manage users.");
            msg.setFont(AppTheme.FONT_BODY);
            msg.setForeground(AppTheme.ALERT_RED);
            bar.add(msg, BorderLayout.WEST);
            return bar;
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton btnAdd   = AppTheme.successBtn("+ Add User");
        JButton btnReset = AppTheme.warnBtn("Reset Password");
        btnAdd.addActionListener(e   -> showAddUserDialog());
        btnReset.addActionListener(e -> showResetPasswordDialog());

        btnPanel.add(btnAdd);
        btnPanel.add(btnReset);
        bar.add(btnPanel, BorderLayout.EAST);
        return bar;
    }

    // ── DATA ──────────────────────────────────────────────────────────
    public void loadData() {
        tableModel.setUsers(userRepository.loadUsers());
    }

    // ── DIALOGS ───────────────────────────────────────────────────────
    private void showAddUserDialog() {
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Add User", ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(400, 290);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.setBorder(new EmptyBorder(16, 20, 8, 20));

        JTextField    fldUsername = new JTextField();
        JTextField    fldFullName = new JTextField();
        JPasswordField fldPassword = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"TAX_STAFF", "ADMIN"});

        form.add(new JLabel("Username:"));   form.add(fldUsername);
        form.add(new JLabel("Full Name:"));  form.add(fldFullName);
        form.add(new JLabel("Password:"));   form.add(fldPassword);
        form.add(new JLabel("Role:"));       form.add(cmbRole);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSave   = AppTheme.successBtn("Save");
        JButton btnCancel = AppTheme.ghostBtn("Cancel");

        btnSave.addActionListener(e -> {
            String username = fldUsername.getText().trim();
            String fullName = fldFullName.getText().trim();
            String password = new String(fldPassword.getPassword()).trim();
            String role     = (String) cmbRole.getSelectedItem();

            if (username.isEmpty() || fullName.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "All fields are required.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (userRepository.findUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(dlg, "Username already exists.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            List<User> existing = userRepository.loadUsers();
            String staffId = String.format("NV%03d", existing.size() + 1);
            userRepository.addUser(new User(staffId, username, password, role, fullName, "", ""));
            loadData();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "User '" + username + "' added.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dlg.add(form,     BorderLayout.CENTER);
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
        fldUsernameRO.setBackground(AppTheme.WARM_STRIPE);
        JTextField fldFullName = new JTextField(u.getFullName());
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"TAX_STAFF", "ADMIN"});
        cmbRole.setSelectedItem(u.getRole());

        form.add(new JLabel("Username (read-only):")); form.add(fldUsernameRO);
        form.add(new JLabel("Full Name:"));            form.add(fldFullName);
        form.add(new JLabel("Role:"));                 form.add(cmbRole);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnSave   = AppTheme.primaryBtn("Save Changes");
        JButton btnCancel = AppTheme.ghostBtn("Cancel");

        btnSave.addActionListener(e -> {
            String fullName = fldFullName.getText().trim();
            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Full name cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userRepository.updateUser(u.getUsername(), fullName, (String) cmbRole.getSelectedItem());
            loadData();
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "User updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        dlg.add(form,     BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    void confirmDeleteUser(int modelRow) {
        List<User> users = userRepository.loadUsers();
        if (modelRow < 0 || modelRow >= users.size()) return;
        User u = users.get(modelRow);
        if (u.getUsername().equals(currentUser.getUsername())) {
            JOptionPane.showMessageDialog(this, "You cannot delete your own account.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete user: " + u.getUsername() + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            userRepository.deleteUser(u.getUsername());
            loadData();
            JOptionPane.showMessageDialog(this, "User deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showResetPasswordDialog() {
        List<User> users = userRepository.loadUsers();
        if (users.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No users found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JDialog dlg = new JDialog(SwingUtilities.getWindowAncestor(this), "Reset Password", ModalityType.APPLICATION_MODAL);
        dlg.setLayout(new BorderLayout(10, 10));
        dlg.setSize(400, 230);
        dlg.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
        form.setBorder(new EmptyBorder(16, 20, 8, 20));

        String[] usernames = users.stream().map(User::getUsername).toArray(String[]::new);
        JComboBox<String> cmbUser  = new JComboBox<>(usernames);
        JPasswordField fldNew      = new JPasswordField();
        JPasswordField fldConfirm  = new JPasswordField();

        form.add(new JLabel("Select User:"));       form.add(cmbUser);
        form.add(new JLabel("New Password:"));      form.add(fldNew);
        form.add(new JLabel("Confirm Password:"));  form.add(fldConfirm);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        JButton btnReset  = AppTheme.warnBtn("Reset");
        JButton btnCancel = AppTheme.ghostBtn("Cancel");

        btnReset.addActionListener(e -> {
            String newPwd  = new String(fldNew.getPassword());
            String confirm = new String(fldConfirm.getPassword());
            if (newPwd.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Password cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!newPwd.equals(confirm)) {
                JOptionPane.showMessageDialog(dlg, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            userRepository.updatePassword((String) cmbUser.getSelectedItem(), newPwd);
            dlg.dispose();
            JOptionPane.showMessageDialog(this, "Password reset successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        });
        btnCancel.addActionListener(e -> dlg.dispose());

        btnPanel.add(btnReset);
        btnPanel.add(btnCancel);
        dlg.add(form,     BorderLayout.CENTER);
        dlg.add(btnPanel, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ── TABLE MODEL ───────────────────────────────────────────────────
    private class UserTableModel extends AbstractTableModel {
        private final String[] COLS = {"Username", "Full Name", "Role", "Actions"};
        private List<User> users = new ArrayList<>();

        void setUsers(List<User> u) { this.users = new ArrayList<>(u); fireTableDataChanged(); }
        List<User> getUsers()       { return users; }

        @Override public int    getRowCount()                { return users.size(); }
        @Override public int    getColumnCount()             { return COLS.length; }
        @Override public String getColumnName(int col)       { return COLS[col]; }
        @Override public boolean isCellEditable(int r, int c){ return c == 3; }

        @Override public Object getValueAt(int row, int col) {
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

    // ── ACTIONS COLUMN ────────────────────────────────────────────────
    private class ActionsPanel extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
        private final JPanel  panel;
        private final JButton btnEdit;
        private final JButton btnDelete;
        private int currentRow = -1;

        ActionsPanel(boolean isAdmin) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 5));
            panel.setOpaque(true);

            btnEdit   = AppTheme.primaryBtn("Edit");
            btnDelete = AppTheme.dangerBtn("Delete");
            btnEdit.setFont(AppTheme.FONT_SMALL.deriveFont(Font.BOLD));
            btnDelete.setFont(AppTheme.FONT_SMALL.deriveFont(Font.BOLD));
            btnEdit.setBorder(new EmptyBorder(4, 10, 4, 10));
            btnDelete.setBorder(new EmptyBorder(4, 10, 4, 10));

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
                JLabel na = new JLabel("No access");
                na.setFont(AppTheme.FONT_SMALL);
                na.setForeground(AppTheme.TEXT_SECONDARY);
                panel.add(na);
            }
        }

        private JPanel prep(int row, boolean selected) {
            currentRow = row;
            panel.setBackground(selected ? AppTheme.TABLE_SELECTION_BG
                    : (row % 2 == 0 ? AppTheme.WARM_CARD : AppTheme.WARM_STRIPE));
            return panel;
        }

        @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) { return prep(row, sel); }
        @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) { return prep(row, true); }
        @Override public Object getCellEditorValue() { return null; }
    }
}
