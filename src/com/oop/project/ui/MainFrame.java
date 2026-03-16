package com.oop.project.ui;

import com.oop.project.model.User;
import com.oop.project.service.AuthenticationService;
import com.oop.project.util.AppTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame extends JFrame {

    private final User currentUser;

    // Navigation
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPane   = new JPanel(cardLayout);
    private final Map<String, JButton> navButtons = new LinkedHashMap<>();
    private String currentPage = "";
    private JLabel lblBreadcrumb;

    // Panel refs for callbacks
    private DashboardPanel dashboardPanel;
    private ReturnsPanel   returnsPanel;

    public MainFrame(User user) {
        this.currentUser = user;
        AppTheme.applyFlatLafCustomizations();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("VTAX — Vietnam Tax Return Management System");
        setSize(1200, 800);
        setMinimumSize(new Dimension(960, 640));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boolean isAdmin = currentUser != null && "ADMIN".equals(currentUser.getRole());

        // Build content panels
        dashboardPanel = new DashboardPanel();
        ClientPanel         clientPanel = new ClientPanel();
        TaxCalculationPanel taxPanel    = new TaxCalculationPanel();
        returnsPanel                    = new ReturnsPanel();

        contentPane.setBackground(AppTheme.WARM_BG);
        contentPane.add(dashboardPanel, "dashboard");
        contentPane.add(clientPanel,    "clients");
        contentPane.add(taxPanel,       "taxcalc");
        contentPane.add(returnsPanel,   "returns");
        if (isAdmin) {
            contentPane.add(new AdministrationPanel(currentUser), "admin");
        }

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(AppTheme.WARM_BG);
        root.add(buildSidebar(isAdmin), BorderLayout.WEST);
        root.add(buildTopBar(),         BorderLayout.NORTH);
        root.add(contentPane,           BorderLayout.CENTER);
        root.add(buildStatusBar(),      BorderLayout.SOUTH);
        add(root);

        navigateTo("dashboard");
    }

    // ── SIDEBAR ──────────────────────────────────────────────────────
    private JPanel buildSidebar(boolean isAdmin) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(AppTheme.SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(200, 0));

        sidebar.add(buildSidebarBrand(),      BorderLayout.NORTH);
        sidebar.add(buildSidebarNav(isAdmin), BorderLayout.CENTER);
        sidebar.add(buildSidebarUser(),       BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel buildSidebarBrand() {
        JPanel brand = new JPanel(new BorderLayout());
        brand.setBackground(AppTheme.SIDEBAR_BG);
        brand.setBorder(new EmptyBorder(22, 20, 18, 20));

        JLabel lblVtax = new JLabel("VTAX");
        lblVtax.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblVtax.setForeground(AppTheme.SIDEBAR_ACCENT);

        JLabel lblSub = new JLabel("Tax Management System");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSub.setForeground(new Color(0x40, 0x50, 0x60));

        JPanel text = new JPanel(new GridLayout(2, 1, 0, 2));
        text.setOpaque(false);
        text.add(lblVtax);
        text.add(lblSub);
        brand.add(text, BorderLayout.CENTER);

        JPanel line = new JPanel();
        line.setBackground(AppTheme.SIDEBAR_DIVIDER);
        line.setPreferredSize(new Dimension(0, 1));
        brand.add(line, BorderLayout.SOUTH);
        return brand;
    }

    private JPanel buildSidebarNav(boolean isAdmin) {
        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setBackground(AppTheme.SIDEBAR_BG);
        nav.setBorder(new EmptyBorder(10, 0, 10, 0));

        addNavItem(nav, "dashboard", "  Dashboard");
        addNavItem(nav, "clients",   "  Clients");
        addNavItem(nav, "taxcalc",   "  Tax Filing");
        addNavItem(nav, "returns",   "  Returns");
        if (isAdmin) {
            addNavItem(nav, "admin", "  Administration");
        }

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(AppTheme.SIDEBAR_BG);
        wrapper.add(nav, BorderLayout.NORTH);
        return wrapper;
    }

    private void addNavItem(JPanel parent, String key, String label) {
        JButton btn = new JButton(label);
        btn.setFont(AppTheme.FONT_NAV);
        btn.setForeground(AppTheme.SIDEBAR_TEXT);
        btn.setBackground(AppTheme.SIDEBAR_BG);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(11, 20, 11, 16));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!key.equals(currentPage)) {
                    btn.setBackground(AppTheme.SIDEBAR_HOVER);
                    btn.setForeground(Color.WHITE);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!key.equals(currentPage)) {
                    btn.setBackground(AppTheme.SIDEBAR_BG);
                    btn.setForeground(AppTheme.SIDEBAR_TEXT);
                }
            }
        });

        btn.addActionListener(e -> onNavigate(key));
        navButtons.put(key, btn);
        parent.add(btn);
    }

    private void onNavigate(String key) {
        if ("returns".equals(key))   returnsPanel.loadData();
        if ("dashboard".equals(key)) dashboardPanel.refresh();
        navigateTo(key);
    }

    private void navigateTo(String key) {
        // Deactivate previous
        if (navButtons.containsKey(currentPage)) {
            JButton prev = navButtons.get(currentPage);
            prev.setBackground(AppTheme.SIDEBAR_BG);
            prev.setForeground(AppTheme.SIDEBAR_TEXT);
            prev.setBorder(new EmptyBorder(11, 20, 11, 16));
        }
        currentPage = key;

        // Activate current — left gold accent bar
        if (navButtons.containsKey(key)) {
            JButton btn = navButtons.get(key);
            btn.setBackground(AppTheme.SIDEBAR_ACTIVE);
            btn.setForeground(Color.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 3, 0, 0, AppTheme.SIDEBAR_ACCENT),
                    new EmptyBorder(11, 17, 11, 16)));
        }

        cardLayout.show(contentPane, key);
        if (lblBreadcrumb != null) lblBreadcrumb.setText(pageTitle(key));
    }

    private String pageTitle(String key) {
        switch (key) {
            case "dashboard": return "Dashboard";
            case "clients":   return "Client Management";
            case "taxcalc":   return "Tax Filing";
            case "returns":   return "Tax Returns";
            case "admin":     return "Administration";
            default:          return key;
        }
    }

    private JPanel buildSidebarUser() {
        JPanel area = new JPanel(new BorderLayout(8, 0));
        area.setBackground(new Color(0x0C, 0x12, 0x1A));
        area.setBorder(new EmptyBorder(14, 18, 14, 14));

        String displayName = (currentUser != null
                && currentUser.getFullName() != null
                && !currentUser.getFullName().isEmpty())
                        ? currentUser.getFullName()
                        : (currentUser != null ? currentUser.getUsername() : "User");
        String role = currentUser != null ? currentUser.getRole() : "";

        JLabel lblName = new JLabel(displayName);
        lblName.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblName.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel(role);
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRole.setForeground(AppTheme.SIDEBAR_ACCENT);

        JPanel nameStack = new JPanel(new GridLayout(2, 1, 0, 1));
        nameStack.setOpaque(false);
        nameStack.add(lblName);
        nameStack.add(lblRole);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnLogout.setForeground(new Color(0x60, 0x70, 0x80));
        btnLogout.setBackground(new Color(0x0C, 0x12, 0x1A));
        btnLogout.setBorderPainted(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btnLogout.setForeground(Color.WHITE); }
            public void mouseExited(MouseEvent e)  { btnLogout.setForeground(new Color(0x60, 0x70, 0x80)); }
        });
        btnLogout.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                    "Logout from VTAX?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (c == JOptionPane.YES_OPTION) {
                if (currentUser != null)
                    new AuthenticationService().logLogout(currentUser.getUsername());
                new LoginFrame().setVisible(true);
                dispose();
            }
        });

        area.add(nameStack,  BorderLayout.CENTER);
        area.add(btnLogout,  BorderLayout.EAST);
        return area;
    }

    // ── TOP BAR ──────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 0, AppTheme.WARM_BORDER),
                new EmptyBorder(0, 20, 0, 20)));
        bar.setPreferredSize(new Dimension(0, 46));

        lblBreadcrumb = new JLabel("Dashboard");
        lblBreadcrumb.setFont(AppTheme.FONT_H2);
        lblBreadcrumb.setForeground(AppTheme.TEXT_PRIMARY);

        JLabel lblClock = new JLabel();
        lblClock.setFont(AppTheme.FONT_CAPTION);
        lblClock.setForeground(AppTheme.TEXT_SECONDARY);

        Runnable tick = () -> lblClock.setText(
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm  ·  dd/MM/yyyy")));
        tick.run();
        new Timer(30000, e -> tick.run()).start();

        bar.add(lblBreadcrumb, BorderLayout.WEST);
        bar.add(lblClock,      BorderLayout.EAST);
        return bar;
    }

    // ── STATUS BAR ───────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppTheme.WARM_HEADER);
        bar.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 0, 0, 0, AppTheme.WARM_BORDER),
                new EmptyBorder(3, 16, 3, 16)));

        JLabel lbl = new JLabel("VTAX  ·  Vietnam Personal Income Tax Return System  ·  v1.0");
        lbl.setFont(AppTheme.FONT_CAPTION);
        lbl.setForeground(AppTheme.TEXT_SECONDARY);

        bar.add(lbl, BorderLayout.WEST);
        return bar;
    }
}
