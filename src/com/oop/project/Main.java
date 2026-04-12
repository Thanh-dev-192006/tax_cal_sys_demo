package com.oop.project;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    private static final String BOOTSTRAP_FLAG = "tax.app.bootstrapped";
    private static final String MAIN_CLASS = "com.oop.project.Main";
    private static final String DATA_INITIALIZER_CLASS = "com.oop.project.util.DataInitializer";
    private static final String LOGIN_FRAME_CLASS = "com.oop.project.ui.LoginFrame";

    public static void main(String[] args) {
        try {
            if (shouldBootstrap()) {
                Path projectRoot = findProjectRoot();
                compileProject(projectRoot);
                relaunch(projectRoot, args);
                return;
            }

            startApplication();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    private static boolean shouldBootstrap() {
        if (Boolean.getBoolean(BOOTSTRAP_FLAG)) {
            return false;
        }

        try {
            Class.forName(DATA_INITIALIZER_CLASS);
            Class.forName(LOGIN_FRAME_CLASS);
            Class.forName("com.mysql.cj.jdbc.Driver");
            return false;
        } catch (ClassNotFoundException e) {
            return true;
        }
    }

    private static Path findProjectRoot() throws URISyntaxException {
        List<Path> candidates = List.of(
                Paths.get("").toAbsolutePath(),
                Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).toAbsolutePath()
        );

        for (Path candidate : candidates) {
            Path root = ascendToProjectRoot(candidate);
            if (root != null) {
                return root;
            }
        }

        throw new IllegalStateException("Could not locate the project root.");
    }

    private static Path ascendToProjectRoot(Path start) {
        Path current = Files.isDirectory(start) ? start : start.getParent();
        while (current != null) {
            if (Files.exists(current.resolve("src/com/oop/project/Main.java"))
                    && Files.isDirectory(current.resolve("lib"))) {
                return current;
            }
            current = current.getParent();
        }
        return null;
    }

    private static void compileProject(Path projectRoot) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new IllegalStateException("A JDK is required to compile the project automatically.");
        }

        Path sourceRoot = projectRoot.resolve("src");
        Path outputRoot = projectRoot.resolve("bin");
        Path libRoot = projectRoot.resolve("lib");

        List<Path> sourceFiles;
        try (var paths = Files.walk(sourceRoot)) {
            sourceFiles = paths
                    .filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }

        if (sourceFiles.isEmpty()) {
            throw new IllegalStateException("No Java source files found under " + sourceRoot);
        }

        Files.createDirectories(outputRoot);

        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            fileManager.setLocationFromPaths(StandardLocation.CLASS_OUTPUT, List.of(outputRoot));

            List<String> options = new ArrayList<>();
            options.add("-encoding");
            options.add("UTF-8");
            options.add("--release");
            options.add("17");
            options.add("-classpath");
            options.add(buildCompileClasspath(libRoot));

            Boolean success = compiler.getTask(
                    null,
                    fileManager,
                    null,
                    options,
                    null,
                    fileManager.getJavaFileObjectsFromPaths(sourceFiles)
            ).call();

            if (!Boolean.TRUE.equals(success)) {
                throw new IllegalStateException("Automatic compilation failed.");
            }
        }
    }

    private static String buildCompileClasspath(Path libRoot) throws IOException {
        if (!Files.isDirectory(libRoot)) {
            throw new IllegalStateException("Library directory not found: " + libRoot);
        }

        try (var paths = Files.list(libRoot)) {
            List<String> jars = paths
                    .filter(path -> path.toString().endsWith(".jar"))
                    .map(path -> path.toAbsolutePath().toString())
                    .collect(Collectors.toList());

            if (jars.isEmpty()) {
                throw new IllegalStateException("No dependency JARs found under " + libRoot);
            }

            return String.join(File.pathSeparator, jars);
        }
    }

    private static void relaunch(Path projectRoot, String[] args) throws IOException, InterruptedException {
        String javaExecutable = Paths.get(System.getProperty("java.home"), "bin", "java").toString();
        String runtimeClasspath = projectRoot.resolve("lib") + File.separator + "*"
                + File.pathSeparator
                + projectRoot.resolve("bin");

        List<String> command = new ArrayList<>();
        command.add(javaExecutable);
        command.add("-D" + BOOTSTRAP_FLAG + "=true");
        command.add("-cp");
        command.add(runtimeClasspath);
        command.add(MAIN_CLASS);
        command.addAll(List.of(args));

        Process process = new ProcessBuilder(command)
                .directory(projectRoot.toFile())
                .inheritIO()
                .start();

        int exitCode = process.waitFor();
        System.exit(exitCode);
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
