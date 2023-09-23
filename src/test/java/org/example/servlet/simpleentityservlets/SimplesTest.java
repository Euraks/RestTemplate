package org.example.servlet.simpleentityservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.service.Service;
import org.junit.jupiter.api.Assertions;
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
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.startsWith;

@Testcontainers
public class SimplesTest {

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
    void testDefaultConstructor() throws Exception {
        postgreSQLContainer.start();

        Simples servlet = new Simples();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        Assertions.assertFalse(stringWriter.toString().isEmpty(), "Response body should not be empty");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);

        postgreSQLContainer.stop();
    }


    @Test
    void testDoGet() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );
    }

    @Test
    void testDoPost() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"description\": \"description\"}";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Assertions.assertTrue( stringWriter.toString().contains( "Added SimpleEntity UUID:" ), "Response should confirm the entity was added" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_CREATED );
    }

    @Test
    void testDoPostSavesSimpleEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service<SimpleEntity, UUID> mockedService = Mockito.mock( Service.class );

        servlet.setMapper( realMapper );
        servlet.setService( mockedService );

        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"description\": \"description\"}";

        SimpleEntity expectedEntity = realMapper.readValue( validJson, SimpleEntity.class );

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Mockito.verify( mockedService ).save( expectedEntity );
        String expectedMessage = "Added SimpleEntity UUID:" + expectedEntity.getUuid();
        Assertions.assertTrue( stringWriter.toString().contains( expectedMessage ) );
    }

    @Test
    void testDoPostUpdateSimpleEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service mockedService = Mockito.mock( Service.class );

        servlet.setMapper( realMapper );
        servlet.setService( mockedService );

        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"description\": \"Update description\"}";

        SimpleEntity expectedEntity = realMapper.readValue( validJson, SimpleEntity.class );

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Mockito.verify( mockedService ).save( expectedEntity );
        String expectedMessage = "Added SimpleEntity UUID:" + expectedEntity.getUuid();
        Assertions.assertTrue( stringWriter.toString().contains( expectedMessage ) );
    }

    @Test
    void testSendSuccessResponseViaDoGet() throws Exception {

        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        PrintWriter mockWriter = Mockito.mock(PrintWriter.class);

        Service<SimpleEntity, UUID> mockService = Mockito.mock(Service.class);
        Mockito.when(mockService.findAll()).thenReturn( Collections.emptyList());

        Mockito.when(mockResponse.getWriter()).thenReturn(mockWriter);

        Simples servlet = new Simples();
        servlet.setService(mockService);

        servlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setContentType("application/json");
        Mockito.verify(mockResponse).setCharacterEncoding("UTF-8");
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(mockWriter).write(startsWith("GetAll SimpleEntity:"));
    }
}
