package com.oop.project.ui;

import com.oop.project.exception.InvalidCredentialException;
import com.oop.project.model.User;
import com.oop.project.service.AuthenticationService;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox showPasswordCheck;
    private final AuthenticationService authService;

    public LoginFrame() {
        this.authService = new AuthenticationService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Tax Return Management System - Login");
        setSize(480, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.BACKGROUND);

        // ── HEADER BANNER ──────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppTheme.PANEL_HEADER_BG);
        header.setBorder(new EmptyBorder(24, 32, 24, 32));

        JLabel lblCompany = new JLabel("VIETNAM TAX CONSULTING COMPANY");
        lblCompany.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblCompany.setForeground(new Color(0xBB, 0xDE, 0xFB));
        lblCompany.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblTitle = new JLabel("Tax Return Management System");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSub = new JLabel("Version 1.0 | DSAI1004 Project 6");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(0xBB, 0xDE, 0xFB));
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel titlePanel = new JPanel(new GridLayout(3, 1, 0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(lblCompany);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSub);
        header.add(titlePanel, BorderLayout.CENTER);

        // ── FORM CARD ──────────────────────────────────────────────
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(AppTheme.CARD_BG);
        card.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel lblLogin = new JLabel("Sign In");
        lblLogin.setFont(AppTheme.FONT_SUBTITLE);
        lblLogin.setForeground(AppTheme.PRIMARY_BLUE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 18, 0);
        card.add(lblLogin, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 2, 0);
        card.add(fieldLabel("Username:"), gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 10, 0);
        usernameField = styledField(300, 36);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter username...");
        card.add(usernameField, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(4, 0, 2, 0);
        card.add(fieldLabel("Password:"), gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 4, 0);
        passwordField = new JPasswordField();
        passwordField.putClientProperty("JTextField.placeholderText", "Enter password...");
        passwordField.setFont(AppTheme.FONT_BODY);
        passwordField.setPreferredSize(new Dimension(300, 36));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                new EmptyBorder(4, 8, 4, 8)));
        card.add(passwordField, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 18, 0);
        showPasswordCheck = new JCheckBox("Show password");
        showPasswordCheck.setFont(AppTheme.FONT_SMALL);
        showPasswordCheck.setOpaque(false);
        showPasswordCheck
                .addActionListener(e -> passwordField.setEchoChar(showPasswordCheck.isSelected() ? (char) 0 : '*'));
        card.add(showPasswordCheck, gbc);

        gbc.gridy = 6;
        gbc.insets = new Insets(4, 0, 10, 0);
        loginButton = new JButton("LOGIN");
        loginButton.setFont(AppTheme.FONT_BUTTON);
        loginButton.setBackground(AppTheme.PRIMARY_BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 42));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> performLogin());
        card.add(loginButton, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 0, 0);
        JLabel hint = new JLabel("Admin: admin / admin123     Staff: nhanvien1 / Staff@123");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hint.setForeground(AppTheme.TEXT_SECONDARY);
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(hint, gbc);

        // ── FOOTER ─────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(AppTheme.BACKGROUND);
        footer.setBorder(new EmptyBorder(8, 0, 12, 0));
        JLabel copy = new JLabel("© 2025 Tax Return Management System  |  DSAI1004 Project 6");
        copy.setFont(AppTheme.FONT_SMALL);
        copy.setForeground(AppTheme.TEXT_SECONDARY);
        footer.add(copy);

        JPanel cardWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        cardWrapper.setBackground(AppTheme.BACKGROUND);
        cardWrapper.setBorder(new EmptyBorder(40, 50, 40, 50));
        cardWrapper.add(card);

        root.add(header, BorderLayout.NORTH);
        root.add(cardWrapper, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        add(root);

        // Enter key triggers login
        KeyAdapter enter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    performLogin();
            }
        };
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);

        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your username and password.",
                    "Missing Information", JOptionPane.WARNING_MESSAGE);
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Verifying...");

        try {
            User user = authService.authenticate(username, password);
            authService.logLogin(username);
            new MainFrame(user).setVisible(true);
            dispose();
        } catch (InvalidCredentialException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password. Please try again.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            passwordField.requestFocusInWindow();
        } finally {
            loginButton.setEnabled(true);
            loginButton.setText("LOGIN");
        }
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppTheme.FONT_BODY);
        return l;
    }

    private JTextField styledField(int w, int h) {
        JTextField f = new JTextField();
        f.setFont(AppTheme.FONT_BODY);
        f.setPreferredSize(new Dimension(w, h));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER_COLOR, 1),
                new EmptyBorder(4, 8, 4, 8)));
        return f;
    }
}
