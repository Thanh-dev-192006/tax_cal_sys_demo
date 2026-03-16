package com.oop.project.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ConnectionTester — verifies the live database connection and confirms that
 * all three application tables exist in the {@code public} schema.
 *
 * <p>Call {@link #testConnection()} once after {@link DatabaseConfig#initialize()}
 * to catch configuration problems (missing schema, wrong database, etc.) before
 * the UI is shown to the user.
 *
 * <p>This class uses only plain JDBC — no Spring, no ORM.
 */
public final class ConnectionTester {

    private static final Logger LOG = Logger.getLogger(ConnectionTester.class.getName());

    /** Names of the tables that must exist for the application to function. */
    private static final String[] REQUIRED_TABLES = {"users", "clients", "tax_returns"};

    // Private constructor — utility class, not to be instantiated.
    private ConnectionTester() {}

    /**
     * Tests the database connection and verifies that all required tables are present.
     *
     * <p>Steps performed:
     * <ol>
     *   <li>Executes {@code SELECT 1} to confirm the connection is live.</li>
     *   <li>For each required table, executes
     *       {@code SELECT to_regclass('public.<table>') IS NOT NULL} to check existence.</li>
     * </ol>
     *
     * <p>If all checks pass, logs {@code "Database connection verified. All tables found."}
     * and returns {@code true}.  If any check fails, logs the specific error, prints a
     * hint to run the DDL scripts, and returns {@code false}.
     *
     * @return {@code true} if the connection is healthy and all required tables exist;
     *         {@code false} otherwise
     */
    public static boolean testConnection() {
        Connection        conn = null;
        PreparedStatement ps   = null;
        ResultSet         rs   = null;

        try {
            conn = DatabaseConfig.getConnection();

            // Step 1 — basic connectivity check.
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

            // Step 2 — verify each required table exists.
            for (String table : REQUIRED_TABLES) {
                ps = conn.prepareStatement(
                        "SELECT to_regclass('public." + table + "') IS NOT NULL AS exists");
                rs = ps.executeQuery();
                boolean exists = rs.next() && rs.getBoolean("exists");
                DatabaseConfig.close(null, ps, rs);
                ps = null;
                rs = null;

                if (!exists) {
                    String msg = "Required table '" + table + "' does not exist in the 'public' schema.";
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
            System.out.println("ERROR: Database connection test failed — " + ex.getMessage());
            printSchemaHint();
            return false;
        } finally {
            DatabaseConfig.close(conn, ps, rs);
        }
    }

    /**
     * Prints a console hint directing the user to run the DDL and seed scripts
     * when the database schema is absent or incomplete.
     */
    private static void printSchemaHint() {
        System.out.println("HINT: Run schema.sql and seed.sql to initialise the database.");
        System.out.println("      Example: psql -U <username> -d <database> -f schema.sql");
        System.out.println("               psql -U <username> -d <database> -f seed.sql");
    }
}
