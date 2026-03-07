package com.oop.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseUtil - Quan ly ket noi MySQL
 */
public class DatabaseUtil {
    
    // Thong tin ket noi - SUA LAI cho phu hop voi may ban
    // Giờ đây hỗ trợ đọc từ System properties hoặc environment variables để dễ cấu hình:
    //   -Ddb.url=jdbc:mysql://host:port/dbname?useSSL=false&serverTimezone=UTC
    //   -Ddb.user=youruser
    //   -Ddb.pass=yourpass
    private static final String URL = System.getProperty("db.url",
            "jdbc:mysql://localhost:3306/tax_return_system?useSSL=false&serverTimezone=UTC");
    private static final String USERNAME = System.getProperty("db.user", "root");
    private static final String PASSWORD = System.getProperty("db.pass", "chongcuavy24/7");
    
    // Driver class (MySQL by default)
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    static {
        try {
            // Load MySQL JDBC Driver
            Class.forName(DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found! Make sure the connector JAR is on the classpath");
            System.err.println("- If using Maven, run via `mvn compile exec:java` or ensure your IDE adds dependencies to the run configuration.");
            System.err.println("- If you're launching manually, add mysql-connector-java-*.jar to the classpath or place it in the project lib folder.");
            e.printStackTrace();
        }
    }
    
    /**
     * Tao connection moi den database
     * @return Connection object
     * @throws SQLException neu khong ket noi duoc
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connected to database successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database!");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USERNAME);
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
            throw e;
        }
    }
    
    /**
     * Dong connection
     * @param conn Connection can dong
     */
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
    
    /**
     * Test connection
     * @return true neu ket noi thanh cong
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}