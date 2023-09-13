package org.example.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class HikariCPDataSourceTest {

    @Test
    void getConnection() throws SQLException {
        Connection connection = new HikariCPDataSource().getConnection();
        assertNotNull( connection );
    }
}