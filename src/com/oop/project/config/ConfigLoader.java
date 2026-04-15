package com.oop.project.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final String LEGACY_CONFIG_FILE_NAME = "application.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigLoader() {}

    private static Properties loadProperties() {
        Properties properties = new Properties();
        loadPropertiesFromPath(properties, Paths.get(CONFIG_FILE_NAME));
        if (properties.isEmpty()) {
            loadPropertiesFromPath(properties, Paths.get(LEGACY_CONFIG_FILE_NAME));
        }
        if (properties.isEmpty()) {
            loadPropertiesFromResource(properties, CONFIG_FILE_NAME);
        }
        if (properties.isEmpty()) {
            loadPropertiesFromResource(properties, LEGACY_CONFIG_FILE_NAME);
        }
        return properties;
    }

    private static void loadPropertiesFromPath(Properties properties, Path path) {
        if (!Files.exists(path)) {
            return;
        }
        try (InputStream inputStream = Files.newInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + path.getFileName(), e);
        }
    }

    private static void loadPropertiesFromResource(Properties properties, String resourceName) {
        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load resource " + resourceName, e);
        }
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return PROPERTIES.getProperty(key, defaultValue);
    }

    public static String getRequiredProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing required config key: " + key);
        }
        return value.trim();
    }

    public static int getIntProperty(String key, int defaultValue) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String getDbUrl() {
        return getRequiredProperty("db.url");
    }

    public static String getDbUser() {
        return getRequiredProperty("db.user");
    }

    public static String getDbPassword() {
        return getRequiredProperty("db.password");
    }

    public static int getAppPort() {
        return getIntProperty("app.port", 8080);
    }
}
