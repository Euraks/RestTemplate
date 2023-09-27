package org.example.db;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class HikariCPDataSourceTest {

    @Mock
    private HikariDataSource mockDataSource;

    @InjectMocks
    private HikariCPDataSource hikariCPDataSource;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks( this );
    }


    @Test
    void testInitializeConfig_Success() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream( "db.properties" )){
            assertNotNull( is, "Ensure db.properties is present for this test" );
        }
    }

    @Test
    void testGetConnection_Success() throws SQLException {
        Connection mockConnection = Mockito.mock(Connection.class);
        Mockito.when(mockDataSource.getConnection()).thenReturn(mockConnection);

        Connection result = hikariCPDataSource.getConnection();
        assertEquals(mockConnection, result);
    }

    @Test
    void testGetConnection_Failure() throws SQLException {
        Mockito.when(mockDataSource.getConnection()).thenThrow(new SQLException("Error getting connection"));

        Connection result = hikariCPDataSource.getConnection();
        assertNull(result);
    }

}
