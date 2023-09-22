package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.service.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@Testcontainers
public class SimplesServletTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript( "db.sql" );

    private Simples servlet;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };

        servlet = new Simples( testConnectionManager );
    }

    @Test
    void testDoGet() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        servlet.doGet( request, response );
    }

    @Test
    void testDoPost() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String inputJson = "{ ... }";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( inputJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );
    }

    @Test
    void testDoPostSavesSimpleEntity() throws Exception {
        // Mocks
        ObjectMapper mockedMapper = Mockito.mock(ObjectMapper.class);
        Service<SimpleEntity, UUID> mockedService = Mockito.mock(Service.class);

        servlet.setMapper(mockedMapper);
        servlet.setService(mockedService);

        String inputJson = "{ ... }";
        SimpleEntity expectedEntity = new SimpleEntity();
        Mockito.when(mockedMapper.readValue(inputJson, SimpleEntity.class)).thenReturn(expectedEntity);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(inputJson));
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        servlet.doPost(request, response);

        Mockito.verify(mockedService).save(expectedEntity);
        String expectedMessage = "Added SimpleEntity UUID:" + expectedEntity.getUuid();
        assertTrue(stringWriter.toString().contains(expectedMessage));
    }
}
