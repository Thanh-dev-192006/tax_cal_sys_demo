package com.oop.project;

import com.oop.project.ui.LoginFrame;
import com.oop.project.util.DataInitializer;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Áp dụng Nimbus Look & Feel (hiện đại hơn Metal mặc định)
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // Tùy chỉnh màu nền và font chữ toàn cục
            UIManager.put("control", new java.awt.Color(0xF5, 0xF5, 0xF5));
            UIManager.put("nimbusBase", new java.awt.Color(0x8B, 0x1A, 0x1A));
            UIManager.put("nimbusBlueGrey", new java.awt.Color(0xE0, 0xE0, 0xE0));
            UIManager.put("nimbusFocus", new java.awt.Color(0x8B, 0x1A, 0x1A));
            UIManager.put("defaultFont", new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        } catch (Exception e) {
            // Fallback: dùng System Look & Feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
        }

        DataInitializer.initializeSampleData();

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
        
    }
}
