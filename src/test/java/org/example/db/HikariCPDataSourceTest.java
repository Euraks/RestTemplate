package org.example.db;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HikariCPDataSourceTest {

    @Test
    void testGetConnection() throws SQLException {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        Connection mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);

        HikariCPDataSource dataSource = new HikariCPDataSource(mockDataSource);
        Connection connection = dataSource.getConnection();

        Assertions.assertNotNull(connection);
        Mockito.verify(mockDataSource).getConnection();
    }

    @Test
    void testGetConnectionException() throws SQLException {
        HikariDataSource mockDataSource = mock(HikariDataSource.class);
        when(mockDataSource.getConnection()).thenThrow(new SQLException("Test exception"));

        HikariCPDataSource dataSource = new HikariCPDataSource(mockDataSource);
        Connection connection = dataSource.getConnection();

        Assertions.assertNull(connection);
        Mockito.verify(mockDataSource).getConnection();
    }
}
