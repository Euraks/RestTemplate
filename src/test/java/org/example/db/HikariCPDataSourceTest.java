package org.example.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class HikariCPDataSourceTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("test")
            .withUsername("user")
            .withPassword("password");

    @BeforeAll
    public static void setUp() {
        System.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        System.setProperty("dataSource.user", "user");
        System.setProperty("dataSource.password", "password");
        System.setProperty("dataSource.databaseName", "test");
        System.setProperty("dataSource.portNumber", postgreSQLContainer.getMappedPort(5432).toString());
        System.setProperty("dataSource.serverName", postgreSQLContainer.getContainerIpAddress());
    }

    @AfterAll
    public static void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    public void testConnection() throws SQLException {
        HikariCPDataSource hikariCPDataSource = new HikariCPDataSource();
        try (Connection connection = hikariCPDataSource.getConnection()) {
            assertNotNull(connection);
        }
    }
}
