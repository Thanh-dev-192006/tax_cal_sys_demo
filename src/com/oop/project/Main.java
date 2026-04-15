package com.oop.project;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Method;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    private static final String DATA_INITIALIZER_CLASS = "com.oop.project.util.DataInitializer";
    private static final String LOGIN_FRAME_CLASS = "com.oop.project.ui.LoginFrame";

    public static void main(String[] args) {
        try {
            startApplication();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static void startApplication() throws Exception {
        applyLookAndFeel();
        invokeStatic(DATA_INITIALIZER_CLASS, "initializeSampleData");
        SwingUtilities.invokeLater(() -> {
            try {
                Class<?> loginFrameClass = Class.forName(LOGIN_FRAME_CLASS);
                Object frame = loginFrameClass.getConstructor().newInstance();
                Method setVisible = loginFrameClass.getMethod("setVisible", boolean.class);
                setVisible.invoke(frame, true);
            } catch (Exception e) {
                throw new RuntimeException("Could not launch the login screen.", e);
            }
        });
    }

    private static void applyLookAndFeel() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", new Color(0xF5, 0xF5, 0xF5));
            UIManager.put("nimbusBase", new Color(0x8B, 0x1A, 0x1A));
            UIManager.put("nimbusBlueGrey", new Color(0xE0, 0xE0, 0xE0));
            UIManager.put("nimbusFocus", new Color(0x8B, 0x1A, 0x1A));
            UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
        }
    }

    private static void invokeStatic(String className, String methodName) throws Exception {
        Class<?> clazz = Class.forName(className);
        Method method = clazz.getMethod(methodName);
        method.invoke(null);
    }
}
