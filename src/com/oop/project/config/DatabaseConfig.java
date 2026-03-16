package com.oop.project.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseConfig — plain Java singleton that manages the HikariCP connection pool.
 *
 * <p>On first call to {@link #initialize()}, this class:
 * <ol>
 *   <li>Loads {@code application.properties} from the classpath (falls back to defaults).</li>
 *   <li>Prompts the user via the console for any missing or blank credentials.</li>
 *   <li>Builds a HikariCP {@link DataSource}.</li>
 *   <li>Tests the connection; if the test fails, offers the user a retry or a graceful exit.</li>
 * </ol>
 *
 * <p>After initialisation, callers obtain connections via {@link #getConnection()} and
 * must close all JDBC resources through {@link #close(Connection, PreparedStatement, ResultSet)}.
 *
 * <p>This class does NOT use Spring — it is a plain static utility class.
 */
public final class DatabaseConfig {

    private static final Logger LOG = Logger.getLogger(DatabaseConfig.class.getName());

    /** Singleton HikariCP data source; null until {@link #initialize()} is called. */
    private static volatile HikariDataSource dataSource;

    /** Properties file name, resolved from the classpath root. */
    private static final String PROPS_FILE = "application.properties";

    // Private constructor — this class must not be instantiated.
    private DatabaseConfig() {}

    // =========================================================================
    // Public API
    // =========================================================================

    /**
     * Initialises the connection pool.  Must be called once at application startup,
     * before any repository accesses the database.
     *
     * <p>If the credentials stored in {@code application.properties} are incomplete,
     * the user is prompted interactively on {@code System.in}/{@code System.out}.
     * On connection failure the user may choose to retry or exit the application.
     */
    public static synchronized void initialize() {
        if (dataSource != null) {
            LOG.fine("DatabaseConfig already initialised — skipping.");
            return;
        }

        Properties defaults = loadProperties();

        boolean connected = false;
        while (!connected) {
            DbCredentials creds = promptCredentials(defaults);
            try {
                dataSource = buildDataSource(creds);
                // Verify the pool can actually reach the server.
                try (Connection c = dataSource.getConnection()) {
                    LOG.info("Database connection pool initialised successfully. URL: "
                            + dataSource.getJdbcUrl());
                }
                connected = true;
            } catch (Exception ex) {
                // Close any partially-created pool before retrying.
                if (dataSource != null) {
                    dataSource.close();
                    dataSource = null;
                }
                System.out.println();
                System.out.println("ERROR: Could not connect to PostgreSQL — " + ex.getMessage());
                LOG.log(Level.WARNING, "Database connection failed", ex);

                if (!askRetry()) {
                    System.out.println("Exiting application.");
                    System.exit(1);
                }
                System.out.println();
            }
        }
    }

    /**
     * Returns the initialised {@link DataSource}.
     *
     * @return the HikariCP data source
     * @throws IllegalStateException if {@link #initialize()} has not been called yet
     */
    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException(
                    "DatabaseConfig has not been initialised. Call DatabaseConfig.initialize() first.");
        }
        return dataSource;
    }

    /**
     * Obtains a {@link Connection} from the connection pool.
     *
     * @return a live JDBC connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * Closes a set of JDBC resources safely, suppressing any exceptions that occur
     * during the close operations.  Any of the parameters may be {@code null}.
     *
     * @param connection       the {@link Connection} to close, or {@code null}
     * @param preparedStatement the {@link PreparedStatement} to close, or {@code null}
     * @param resultSet        the {@link ResultSet} to close, or {@code null}
     */
    public static void close(Connection connection,
                             PreparedStatement preparedStatement,
                             ResultSet resultSet) {
        if (resultSet != null) {
            try { resultSet.close(); } catch (SQLException ex) {
                LOG.log(Level.WARNING, "Failed to close ResultSet", ex);
            }
        }
        if (preparedStatement != null) {
            try { preparedStatement.close(); } catch (SQLException ex) {
                LOG.log(Level.WARNING, "Failed to close PreparedStatement", ex);
            }
        }
        if (connection != null) {
            try { connection.close(); } catch (SQLException ex) {
                LOG.log(Level.WARNING, "Failed to close Connection", ex);
            }
        }
    }

    // =========================================================================
    // Internal helpers
    // =========================================================================

    /**
     * Loads {@code application.properties} from the classpath.
     * Returns an empty {@link Properties} object (with no defaults) if the file
     * cannot be found so that the interactive prompt covers all fields.
     */
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(PROPS_FILE)) {
            if (in != null) {
                props.load(in);
                LOG.fine("Loaded " + PROPS_FILE + " from classpath.");
            } else {
                LOG.warning(PROPS_FILE + " not found on classpath — using interactive prompts only.");
            }
        } catch (IOException ex) {
            LOG.log(Level.WARNING, "Could not read " + PROPS_FILE, ex);
        }
        return props;
    }

    /**
     * Interactively prompts the user for any credential fields that are blank in
     * the supplied {@link Properties}.  Fields that already have values are shown
     * as defaults which the user can accept by pressing Enter.
     *
     * <p>Password input is masked when the JVM provides a {@link Console} (i.e. when
     * the application is run from a real terminal, not piped).
     *
     * @param defaults properties loaded from {@code application.properties}
     * @return a fully-populated {@link DbCredentials} record
     */
    private static DbCredentials promptCredentials(Properties defaults) {
        System.out.println("==========================================================");
        System.out.println("  Tax Calculation System — Database Connection Setup");
        System.out.println("==========================================================");
        System.out.println("Press ENTER to accept the [default] shown in brackets.");
        System.out.println();

        Scanner scanner = new Scanner(System.in);
        Console  console = System.console(); // may be null in IDEs

        String host     = prompt(scanner, "  Host",     defaults.getProperty("db.host", "localhost"),  false, console);
        String portStr  = prompt(scanner, "  Port",     defaults.getProperty("db.port", "5432"),       false, console);
        String dbName   = prompt(scanner, "  Database", defaults.getProperty("db.name", ""),           false, console);
        String username = prompt(scanner, "  Username", defaults.getProperty("db.username", ""),       false, console);
        String password = prompt(scanner, "  Password", defaults.getProperty("db.password", ""),       true,  console);

        // Apply built-in defaults for host/port if the user left them blank.
        if (host.isBlank())    host    = "localhost";
        if (portStr.isBlank()) portStr = "5432";

        int port;
        try {
            port = Integer.parseInt(portStr.trim());
        } catch (NumberFormatException ex) {
            System.out.println("  Invalid port '" + portStr + "' — using 5432.");
            port = 5432;
        }

        // Pool settings — read from properties (no interactive prompt needed).
        int    poolSize         = intProp(defaults, "db.pool.size",               10);
        long   connTimeout      = longProp(defaults, "db.pool.connection.timeout", 30_000L);
        long   idleTimeout      = longProp(defaults, "db.pool.idle.timeout",      600_000L);
        long   maxLifetime      = longProp(defaults, "db.pool.max.lifetime",      1_800_000L);

        System.out.println();
        return new DbCredentials(host, port, dbName, username, password,
                                 poolSize, connTimeout, idleTimeout, maxLifetime);
    }

    /**
     * Displays a single prompt line and reads the user's input.
     * If the user presses Enter without typing anything, the supplied default is used.
     * When {@code maskInput} is {@code true} and a {@link Console} is available, the
     * input characters are not echoed.
     *
     * @param scanner   the {@link Scanner} wrapping {@code System.in}
     * @param label     the field name shown to the user
     * @param defaultVal the default value (empty string means "no default")
     * @param maskInput  whether to suppress echo (for passwords)
     * @param console   the JVM {@link Console}, or {@code null}
     * @return the value entered by the user, or the default if the user pressed Enter
     */
    private static String prompt(Scanner scanner, String label, String defaultVal,
                                 boolean maskInput, Console console) {
        String hint = (defaultVal != null && !defaultVal.isBlank())
                      ? " [" + (maskInput ? "****" : defaultVal) + "]"
                      : "";
        System.out.print(label + hint + ": ");
        System.out.flush();

        String input;
        if (maskInput && console != null) {
            char[] chars = console.readPassword();
            input = (chars == null) ? "" : new String(chars);
        } else {
            input = scanner.hasNextLine() ? scanner.nextLine() : "";
        }

        if (input == null || input.isBlank()) {
            return (defaultVal != null) ? defaultVal : "";
        }
        return input.trim();
    }

    /**
     * Asks the user whether they want to retry the database connection after a failure.
     *
     * @return {@code true} if the user enters "y" or "Y", {@code false} otherwise
     */
    private static boolean askRetry() {
        System.out.print("Retry connection? (y/n): ");
        System.out.flush();
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.hasNextLine() ? scanner.nextLine().trim() : "n";
        return "y".equalsIgnoreCase(answer);
    }

    /**
     * Constructs and configures a {@link HikariDataSource} from the given credentials.
     *
     * @param creds the database credentials and pool settings
     * @return a configured, started {@link HikariDataSource}
     */
    private static HikariDataSource buildDataSource(DbCredentials creds) {
        String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s",
                creds.host(), creds.port(), creds.dbName());

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(creds.username());
        config.setPassword(creds.password());
        config.setDriverClassName("org.postgresql.Driver");

        config.setMaximumPoolSize(creds.poolSize());
        config.setConnectionTimeout(creds.connTimeout());
        config.setIdleTimeout(creds.idleTimeout());
        config.setMaxLifetime(creds.maxLifetime());

        config.setPoolName("TaxCalSysPool");
        config.addDataSourceProperty("reWriteBatchedInserts", "true");

        return new HikariDataSource(config);
    }

    /** Reads an integer property, returning {@code fallback} on parse failure. */
    private static int intProp(Properties p, String key, int fallback) {
        try { return Integer.parseInt(p.getProperty(key, String.valueOf(fallback)).trim()); }
        catch (NumberFormatException ex) { return fallback; }
    }

    /** Reads a long property, returning {@code fallback} on parse failure. */
    private static long longProp(Properties p, String key, long fallback) {
        try { return Long.parseLong(p.getProperty(key, String.valueOf(fallback)).trim()); }
        catch (NumberFormatException ex) { return fallback; }
    }

    // =========================================================================
    // Internal value holder
    // =========================================================================

    /**
     * Immutable value record that carries all parameters needed to build the
     * HikariCP data source.
     */
    private record DbCredentials(
            String host,
            int    port,
            String dbName,
            String username,
            String password,
            int    poolSize,
            long   connTimeout,
            long   idleTimeout,
            long   maxLifetime
    ) {}
}
