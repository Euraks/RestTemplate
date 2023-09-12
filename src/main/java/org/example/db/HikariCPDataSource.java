package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource {


    private static HikariConfig config = new HikariConfig(
            "src/main/resources/db.properties" );
    private static HikariDataSource ds;

    static {
        ds = new HikariDataSource( config );

    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    private HikariCPDataSource() {
    }
}
