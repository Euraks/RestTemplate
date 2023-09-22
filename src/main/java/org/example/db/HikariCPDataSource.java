package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class HikariCPDataSource implements ConnectionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(HikariCPDataSource.class);

    private final HikariDataSource ds;

    public HikariCPDataSource() {
        HikariConfig config = initializeConfig();
        this.ds = new HikariDataSource(config);
    }

    protected HikariCPDataSource(HikariDataSource ds) {
        this.ds = ds;
    }

    private HikariConfig initializeConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new FileNotFoundException("db.properties not found");
            }
            properties.load(input);
            return new HikariConfig(properties);
        } catch (IOException e) {
            LOGGER.error("Failed to initialize HikariConfig", e);
            return null;
        }
    }

    @Override
    public Connection getConnection() {
        try {
            return ds.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Failed to get connection", e);
            return null;
        }
    }
}

