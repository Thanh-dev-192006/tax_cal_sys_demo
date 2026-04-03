package com.oop.project.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class ConfigLoader {

    private static final String CONFIG_FILE_NAME = "config.properties";
    private static final Properties PROPERTIES = loadProperties();

    private ConfigLoader() {}

    private static Properties loadProperties() {
        Properties properties = new Properties();
        Path configPath = Paths.get(CONFIG_FILE_NAME);

        try {
            if (Files.exists(configPath)) {
                try (InputStream inputStream = Files.newInputStream(configPath)) {
                    properties.load(inputStream);
                }
            } else {
                try (InputStream inputStream = ConfigLoader.class.getClassLoader()
                        .getResourceAsStream(CONFIG_FILE_NAME)) {
                    if (inputStream != null) {
                        properties.load(inputStream);
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not load " + CONFIG_FILE_NAME, e);
        }

        return properties;
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
