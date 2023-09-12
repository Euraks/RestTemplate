package org.example;

import org.example.db.HikariCPDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws SQLException {
        Connection connection  =HikariCPDataSource.getConnection();
        System.out.println(connection);
    }
}