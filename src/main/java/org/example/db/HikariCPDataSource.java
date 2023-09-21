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
    private static final Logger LOGGER = LoggerFactory.getLogger(  HikariCPDataSource.class );


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
            LOGGER.info( "Message  {}",11 );
        }
        ds = new HikariDataSource( config );
    }

    @Override
    public Connection getConnection() {
        try{
            return ds.getConnection();
        } catch(SQLException e){
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println( new HikariCPDataSource().getConnection() );
    }
}