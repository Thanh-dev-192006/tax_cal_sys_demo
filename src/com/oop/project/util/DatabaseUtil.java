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
    private static final String URL = ConfigLoader.getDbUrl();
    private static final String USERNAME = ConfigLoader.getDbUser();
    private static final String PASSWORD = ConfigLoader.getDbPassword();

    private DatabaseUtil() {}

    static {
        String driverClassName = inferDriverClassName(URL);
        if (driverClassName != null) {
            try {
                Class.forName(driverClassName);
                System.out.println("JDBC driver loaded successfully: " + driverClassName);
            } catch (ClassNotFoundException e) {
                System.err.println("JDBC driver not found: " + driverClassName);
                System.err.println("Make sure the correct driver JAR is on the classpath.");
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USERNAME);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
            throw e;
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
