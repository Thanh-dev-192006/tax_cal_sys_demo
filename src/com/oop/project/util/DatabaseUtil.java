package com.oop.project.util;

import com.oop.project.config.ConfigLoader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtil manages plain JDBC connections for the application.
 */
public final class DatabaseUtil {

    private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    private DatabaseUtil() {}

    private static String getUrl() {
        return ConfigLoader.getDbUrl();
    }

    private static String getUsername() {
        return ConfigLoader.getDbUser();
    }

    private static String getPassword() {
        return ConfigLoader.getDbPassword();
    }

    public static Connection getConnection() throws SQLException {
        String url = getUrl();
        String username = getUsername();
        String password = getPassword();
        String driverClassName = inferDriverClassName(url);

        try {
            if (driverClassName != null) {
                Class.forName(driverClassName);
            }

            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database successfully");
            return conn;
        } catch (Exception e) {
            System.err.println("Failed to connect to database");
            System.err.println("URL: " + url);
            System.err.println("User: " + username);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
            if (e instanceof SQLException sqlEx) {
                throw sqlEx;
            }
            throw new SQLException("Database connection failed", e);
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    private static String inferDriverClassName(String jdbcUrl) {
        if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:mysql:")) {
            return MYSQL_DRIVER_CLASS_NAME;
        }
        throw new IllegalStateException("Only MySQL JDBC URLs are supported. Current db.url: " + jdbcUrl);
    }
}
