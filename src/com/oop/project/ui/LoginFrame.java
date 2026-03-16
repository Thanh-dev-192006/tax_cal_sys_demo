package com.oop.project.ui;

import com.oop.project.exception.InvalidCredentialException;
import com.oop.project.model.User;
import com.oop.project.service.AuthenticationService;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
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
        setTitle("VTAX — Tax Return Management System");
        setSize(720, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.add(buildBrandPanel(), BorderLayout.WEST);
        root.add(buildFormPanel(),  BorderLayout.CENTER);
        add(root);

        KeyAdapter enter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) performLogin();
            }
        };
        usernameField.addKeyListener(enter);
        passwordField.addKeyListener(enter);
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    // ── LEFT: dark branding panel ────────────────────────────────────
    private JPanel buildBrandPanel() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Subtle diagonal grid lines for texture
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(255, 255, 255, 10));
                g2.setStroke(new BasicStroke(1f));
                int w = getWidth(), h = getHeight();
                for (int i = -h; i < w + h; i += 32) {
                    g2.drawLine(i, 0, i + h, h);
                }
                g2.dispose();
            }
        };
        panel.setBackground(AppTheme.SIDEBAR_BG);
        panel.setPreferredSize(new Dimension(265, 0));
        panel.setBorder(new EmptyBorder(40, 30, 40, 30));

        // Top: brand name + tagline
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setOpaque(false);

        JLabel lblBrand = new JLabel("VTAX");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblBrand.setForeground(AppTheme.SIDEBAR_ACCENT);
        lblBrand.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Gold divider line
        JPanel accentBar = new JPanel();
        accentBar.setBackground(AppTheme.SIDEBAR_ACCENT);
        accentBar.setMaximumSize(new Dimension(48, 3));
        accentBar.setPreferredSize(new Dimension(48, 3));
        accentBar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel("<html>Vietnam Tax<br>Management<br>System</html>");
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblTitle.setForeground(new Color(0xB8, 0xC4, 0xD0));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(lblBrand);
        top.add(Box.createVerticalStrut(14));
        top.add(accentBar);
        top.add(Box.createVerticalStrut(18));
        top.add(lblTitle);

        // Bottom: copyright / hint
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);

        JLabel lblTagline = new JLabel("<html><i>Trusted by tax professionals<br>across Vietnam.</i></html>");
        lblTagline.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTagline.setForeground(new Color(0x4A, 0x5A, 0x6A));
        lblTagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblVersion = new JLabel("Version 1.0  ·  AI66A Project 6");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblVersion.setForeground(new Color(0x30, 0x3E, 0x4C));
        lblVersion.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottom.add(lblTagline);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(lblVersion);

        // Right edge divider
        JPanel divider = new JPanel();
        divider.setBackground(new Color(0x1E, 0x28, 0x38));
        divider.setPreferredSize(new Dimension(1, 0));

        panel.add(top,     BorderLayout.NORTH);
        panel.add(bottom,  BorderLayout.SOUTH);
        panel.add(divider, BorderLayout.EAST);
        return panel;
    }

    // ── RIGHT: white form panel ──────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(Color.WHITE);

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setMaximumSize(new Dimension(320, Integer.MAX_VALUE));

        JLabel lblTitle = new JLabel("Sign In");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Enter your credentials to continue");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(AppTheme.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(lblTitle);
        form.add(Box.createVerticalStrut(6));
        form.add(lblSub);
        form.add(Box.createVerticalStrut(30));

        // Username
        form.add(fieldLabel("Username"));
        form.add(Box.createVerticalStrut(5));
        usernameField = new JTextField();
        usernameField.setFont(AppTheme.FONT_BODY);
        usernameField.putClientProperty("JTextField.placeholderText", "Enter username...");
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(usernameField);
        form.add(Box.createVerticalStrut(16));

        // Password
        form.add(fieldLabel("Password"));
        form.add(Box.createVerticalStrut(5));
        passwordField = new JPasswordField();
        passwordField.setFont(AppTheme.FONT_BODY);
        passwordField.putClientProperty("JTextField.placeholderText", "Enter password...");
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(passwordField);
        form.add(Box.createVerticalStrut(8));

        // Show password
        showPasswordCheck = new JCheckBox("Show password");
        showPasswordCheck.setFont(AppTheme.FONT_SMALL);
        showPasswordCheck.setOpaque(false);
        showPasswordCheck.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPasswordCheck.addActionListener(e ->
                passwordField.setEchoChar(showPasswordCheck.isSelected() ? (char) 0 : '*'));
        form.add(showPasswordCheck);
        form.add(Box.createVerticalStrut(24));

        // Login button
        loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setBackground(AppTheme.SIDEBAR_BG);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.addActionListener(e -> performLogin());
        form.add(loginButton);
        form.add(Box.createVerticalStrut(14));

        // Hint
        JLabel hint = new JLabel("<html><span style='color:#7A7265'>Admin: admin / admin123 &nbsp;·&nbsp; Staff: nhanvien1 / Staff@123</span></html>");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(hint);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 48, 0, 48);
        outer.add(form, gbc);
        return outer;
    }

    private JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(AppTheme.TEXT_PRIMARY);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
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
}
