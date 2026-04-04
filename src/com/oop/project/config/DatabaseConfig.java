package com.oop.project.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DatabaseConfig {

    private static final Logger LOG = Logger.getLogger(DatabaseConfig.class.getName());
    private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static volatile HikariDataSource dataSource;

    private DatabaseConfig() {}

    public static synchronized void initialize() {
        if (dataSource != null) {
            return;
        }

        HikariDataSource candidate = buildDataSource();
        try (Connection connection = candidate.getConnection()) {
            dataSource = candidate;
            LOG.info("Database connection pool initialized successfully. URL: " + candidate.getJdbcUrl());
        } catch (SQLException ex) {
            candidate.close();
            throw new IllegalStateException("Could not initialize database connection pool", ex);
        }
    }

    public static DataSource getDataSource() {
        if (dataSource == null) {
            throw new IllegalStateException(
                    "DatabaseConfig has not been initialized. Call DatabaseConfig.initialize() first.");
        }
        return dataSource;
    }

    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    public static void close(Connection connection,
                             PreparedStatement preparedStatement,
                             ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException ex) {
                LOG.log(Level.WARNING, "Failed to close ResultSet", ex);
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException ex) {
                LOG.log(Level.WARNING, "Failed to close PreparedStatement", ex);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                LOG.log(Level.WARNING, "Failed to close Connection", ex);
            }
        }
    }

    private static HikariDataSource buildDataSource() {
        String jdbcUrl = ConfigLoader.getDbUrl();
        String username = ConfigLoader.getDbUser();
        String password = ConfigLoader.getDbPassword();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        String driverClassName = inferDriverClassName(jdbcUrl);
        if (driverClassName != null) {
            config.setDriverClassName(driverClassName);
        }

        config.setMaximumPoolSize(ConfigLoader.getIntProperty("db.pool.size", 10));
        config.setConnectionTimeout(ConfigLoader.getIntProperty("db.pool.connection.timeout", 30000));
        config.setIdleTimeout(ConfigLoader.getIntProperty("db.pool.idle.timeout", 600000));
        config.setMaxLifetime(ConfigLoader.getIntProperty("db.pool.max.lifetime", 1800000));
        config.setPoolName("TaxCalSysPool");

        return new HikariDataSource(config);
    }

    private static String inferDriverClassName(String jdbcUrl) {
        if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:mysql:")) {
            return MYSQL_DRIVER_CLASS_NAME;
        }
        throw new IllegalStateException("Only MySQL JDBC URLs are supported. Current db.url: " + jdbcUrl);
    }
}
