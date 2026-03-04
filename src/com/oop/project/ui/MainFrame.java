package com.oop.project.ui;

import com.oop.project.model.User;
import com.oop.project.service.AuthenticationService;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private final User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;
        initializeUI();
    }

    private void initializeUI() {
        String role = (currentUser != null) ? currentUser.getRole() : "User";
        setTitle("Tax Return Management System  [" + role + "]");
        setSize(1100, 750);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BACKGROUND);

        root.add(buildHeaderBar(), BorderLayout.NORTH);

        // ── TABS ────────────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setFont(AppTheme.FONT_BODY);
        tabs.setBackground(AppTheme.BACKGROUND);

        DashboardPanel dashboardPanel = new DashboardPanel();
        tabs.addTab("  Dashboard  ", dashboardPanel);

        ClientPanel clientPanel = new ClientPanel();
        tabs.addTab("  Clients  ", clientPanel);

        TaxCalculationPanel taxPanel = new TaxCalculationPanel();
        tabs.addTab("  Tax Calculation  ", taxPanel);

        ReturnsPanel returnsPanel = new ReturnsPanel();
        tabs.addTab("  Returns  ", returnsPanel);

        if (currentUser != null && "ADMIN".equals(currentUser.getRole())) {
            tabs.addTab("  Administration  ", buildAdminPlaceholder());
        }

        tabs.addChangeListener(e -> {
            Component sel = tabs.getSelectedComponent();
            if (sel == returnsPanel)   returnsPanel.loadData();
            else if (sel == dashboardPanel) dashboardPanel.refresh();
        });

        root.add(tabs, BorderLayout.CENTER);
        root.add(buildStatusBar(), BorderLayout.SOUTH);
        add(root);
    }

    private JPanel buildHeaderBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppTheme.PANEL_HEADER_BG);
        bar.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel lblApp = new JLabel("Personal Income Tax Return Management System");
        lblApp.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblApp.setForeground(Color.WHITE);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);

        String fullName = (currentUser != null
                && currentUser.getFullName() != null
                && !currentUser.getFullName().isEmpty())
                ? currentUser.getFullName()
                : (currentUser != null ? currentUser.getUsername() : "?");
        String roleDisplay = (currentUser != null) ? currentUser.getRole() : "";

        JLabel lblUser = new JLabel(fullName + "  (" + roleDisplay + ")");
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblUser.setForeground(new Color(0xBB, 0xDE, 0xFB));

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(AppTheme.FONT_BUTTON);
        btnLogout.setBackground(AppTheme.ALERT_RED);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(new EmptyBorder(6, 14, 6, 14));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to logout?",
                    "Confirm Logout", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (currentUser != null)
                    new AuthenticationService().logLogout(currentUser.getUsername());
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        right.add(lblUser);
        right.add(btnLogout);

        bar.add(lblApp, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        bar.setBackground(new Color(0xEC, 0xEF, 0xF5));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.BORDER_COLOR));

        JLabel status = new JLabel(
                "Ready  |  Vietnam Personal Income Tax System  |  Filing Deadline: April 30 annually");
        status.setFont(AppTheme.FONT_SMALL);
        status.setForeground(AppTheme.TEXT_SECONDARY);
        bar.add(status);
        return bar;
    }

    private JPanel buildAdminPlaceholder() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.BACKGROUND);
        p.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel lbl = new JLabel("System Administration  (Admin Only)", SwingConstants.CENTER);
        lbl.setFont(AppTheme.FONT_TITLE);
        lbl.setForeground(AppTheme.TEXT_SECONDARY);

        JLabel sub = new JLabel("Manage staff accounts, view login history", SwingConstants.CENTER);
        sub.setFont(AppTheme.FONT_BODY);
        sub.setForeground(AppTheme.TEXT_SECONDARY);

        JPanel inner = new JPanel(new GridLayout(2, 1, 0, 8));
        inner.setOpaque(false);
        inner.add(lbl);
        inner.add(sub);
        p.add(inner, BorderLayout.CENTER);
        return p;
    }
}
