package com.oop.project.ui;

import com.oop.project.model.User;
import com.oop.project.repository.UserRepository;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdministrationPanel extends JPanel {

    private final User currentUser;
    private final UserRepository userRepository;
    private DefaultTableModel tableModel;

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

        // Top info
        JPanel topPanel = new JPanel(new BorderLayout());
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

        topPanel.add(lblTitle, BorderLayout.NORTH);
        topPanel.add(lblInfo, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Center Table
        String[] cols = { "Username", "Full Name", "Role", "Actions" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xF8, 0xF9, 0xFA));
                }
                return c;
            }
        };
        table.setFont(AppTheme.FONT_BODY);
        table.getTableHeader().setFont(AppTheme.FONT_BODY.deriveFont(Font.BOLD));
        table.setRowHeight(32);
        table.setGridColor(AppTheme.BORDER_COLOR);
        table.setSelectionBackground(AppTheme.LIGHT_BLUE_BG);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(AppTheme.BORDER_COLOR));
        add(scroll, BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bottomPanel.setBackground(AppTheme.BACKGROUND);

        if ("ADMIN".equals(currentUser.getRole())) {
            JButton btnAdd = new JButton("Add User");
            btnAdd.setFont(AppTheme.FONT_BUTTON);
            btnAdd.setBackground(AppTheme.ACCENT_GREEN);
            btnAdd.setForeground(Color.WHITE);
            btnAdd.setFocusPainted(false);
            btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnAdd.setBorder(new EmptyBorder(6, 14, 6, 14));
            btnAdd.addActionListener(e -> JOptionPane.showMessageDialog(this, "Feature coming soon", "Information",
                    JOptionPane.INFORMATION_MESSAGE));

            JButton btnReset = new JButton("Reset Password");
            btnReset.setFont(AppTheme.FONT_BUTTON);
            btnReset.setBackground(AppTheme.WARNING_AMBER);
            btnReset.setForeground(Color.WHITE);
            btnReset.setFocusPainted(false);
            btnReset.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnReset.setBorder(new EmptyBorder(6, 14, 6, 14));
            btnReset.addActionListener(e -> JOptionPane.showMessageDialog(this, "Feature coming soon", "Information",
                    JOptionPane.INFORMATION_MESSAGE));

            bottomPanel.add(btnAdd);
            bottomPanel.add(btnReset);
        }

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<User> users = userRepository.loadUsers();
        for (User u : users) {
            tableModel.addRow(new Object[] {
                    u.getUsername(),
                    u.getFullName(),
                    u.getRole(),
                    "Edit / Delete"
            });
        }
    }
}
