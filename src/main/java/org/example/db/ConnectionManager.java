package org.example.db;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager extends Serializable {

     Connection getConnection() throws SQLException;
}
