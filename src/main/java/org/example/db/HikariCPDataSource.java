package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.Main;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HikariCPDataSource implements ConnectionManager {
    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );


    private static HikariConfig config = new HikariConfig(
            "src/main/resources/db.properties" );
    private static HikariDataSource ds;

    static {
        ds = new HikariDataSource( config );

    }

    public Connection getConnection()  {
        try{
            return ds.getConnection();
        } catch(SQLException e){
            LOGGER.log( Level.WARNING,"Connection Failed" );
            LOGGER.log( Level.WARNING,e.getMessage() );
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new HikariCPDataSource().getConnection());
    }
}