package org.example.servlet.simpleentityservlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.service.impl.SimpleServiceImpl;
import org.junit.jupiter.api.AfterEach;
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
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Testcontainers
public class SimplesIdTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript( "db.sql" );

    private SimplesId servlet;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };

        servlet = new SimplesId( testConnectionManager );
    }

    @AfterEach
    public void tearDown() {
        postgreSQLContainer.stop();
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
        SimpleServiceImpl mockService = mock( SimpleServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        SimpleEntity mockEntity = new SimpleEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = SimplesId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/simples/" + testUUID.toString() );

        servlet.doGet( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Get SimpleEntity UUID:" ) );
    }

    @Test
    void testDoPut() throws Exception {
        SimpleServiceImpl mockService = mock( SimpleServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        SimpleEntity mockEntity = new SimpleEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = SimplesId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/simples/" + testUUID.toString() );

        String inputJson = "{ \"description\": \"Test Description\" }";
        when( mockRequest.getReader() ).thenReturn( new BufferedReader( new StringReader( inputJson ) ) );

        servlet.doPut( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Updated SimpleEntity UUID:" ) );
    }

    @Test
    void testDoDelete() throws Exception {
        SimpleServiceImpl mockService = mock( SimpleServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        SimpleEntity mockEntity = new SimpleEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = SimplesId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/simples/" + testUUID.toString() );

        servlet.doDelete( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Deleted SimpleEntity UUID:" ) );
    }

    @Test
    public void testHandleExceptionViaDoGet() throws Exception {

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PrintWriter mockWriter = mock(PrintWriter.class);

        when(mockRequest.getPathInfo()).thenReturn("/simples/invalidUUID");
        when(mockResponse.getWriter()).thenReturn(mockWriter);

        SimplesId servlet = new SimplesId();

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        verify(mockResponse).setCharacterEncoding("UTF-8");
        verify(mockResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(mockWriter).write("An internal server error occurred.");
    }
}
