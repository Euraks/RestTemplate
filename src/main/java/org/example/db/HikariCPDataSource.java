package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HikariCPDataSource {
    private static final Logger LOGGER = Logger.getLogger( HikariCPDataSource.class.getName() );


    private static HikariConfig config;
    private static final HikariDataSource ds;

    static {
        Properties properties = new Properties();
        try (InputStream input = HikariCPDataSource.class.getClassLoader().getResourceAsStream( "db.properties" )){
            if (input == null) {
                throw new FileNotFoundException( "db.properties not found" );
            }
            properties.load( input );
            config = new HikariConfig( properties );
        } catch(IOException e){
            LOGGER.log( Level.WARNING,"Loading file properties failed" );
        }
        ds = new HikariDataSource( config );
    }

    public Connection getConnection() {
        try{
            return ds.getConnection();
        } catch(SQLException e){
            LOGGER.log( Level.WARNING, "Connection Failed" );
            LOGGER.log( Level.WARNING, e.getMessage() );
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println( new HikariCPDataSource().getConnection() );
    }
}