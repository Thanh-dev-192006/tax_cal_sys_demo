package com.oop.project.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectionTester verifies the live database connection and confirms that
 * all required tables exist in the current MySQL database.
 */
public final class ConnectionTester {

    private static final Logger LOG = Logger.getLogger(ConnectionTester.class.getName());
    private static final String[] REQUIRED_TABLES = {"users", "clients", "tax_returns"};

    private ConnectionTester() {}

    public static boolean testConnection() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConfig.getConnection();

            ps = conn.prepareStatement("SELECT 1");
            rs = ps.executeQuery();
            if (!rs.next()) {
                LOG.severe("Connectivity check failed: SELECT 1 returned no rows.");
                printSchemaHint();
                return false;
            }
            DatabaseConfig.close(null, ps, rs);
            ps = null;
            rs = null;

            for (String table : REQUIRED_TABLES) {
                ps = conn.prepareStatement(
                        "SELECT COUNT(*) AS table_count " +
                                "FROM information_schema.tables " +
                                "WHERE table_schema = DATABASE() AND table_name = ?");
                ps.setString(1, table);
                rs = ps.executeQuery();
                boolean exists = rs.next() && rs.getInt("table_count") > 0;
                DatabaseConfig.close(null, ps, rs);
                ps = null;
                rs = null;

                if (!exists) {
                    String msg = "Required table '" + table + "' does not exist in the current database.";
                    LOG.severe(msg);
                    System.out.println("ERROR: " + msg);
                    printSchemaHint();
                    return false;
                }
            }

            LOG.info("Database connection verified. All tables found.");
            return true;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Connection test failed: " + ex.getMessage(), ex);
            System.out.println("ERROR: Database connection test failed - " + ex.getMessage());
            printSchemaHint();
            return false;
        } finally {
            DatabaseConfig.close(conn, ps, rs);
        }
    }

    private static void printSchemaHint() {
        System.out.println("HINT: Run schema.sql and seed.sql to initialise the database.");
        System.out.println("      Example: mysql -u <username> -p <database> < schema.sql");
        System.out.println("               mysql -u <username> -p <database> < seed.sql");
    }
}
