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
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Testcontainers
public class SimplesIdTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "testcontainers" )
            .withUsername( "testcontainers" )
            .withPassword( "testcontainers" )
            .withInitScript( "db.sql" );

    private SimplesId servlet;
    private ConnectionManager connectionManager;

    @BeforeEach
    public void setUp() throws SQLException {
        connectionManager = mock( ConnectionManager.class );
        when( connectionManager.getConnection() ).thenReturn( postgreSQLContainer.createConnection( "" ) );
        servlet = new SimplesId( connectionManager );
    }

    @Test
    void testDefaultConstructor() {
        SimplesId defaultServlet = new SimplesId();

        ConnectionManager connectionManager = getFieldValue( defaultServlet, "connectionManager", ConnectionManager.class );
        assertNotNull( connectionManager, "connectionManager should be initialized" );

        Repository<SimpleEntity, UUID> repository = getFieldValue( defaultServlet, "repository", Repository.class );
        assertNotNull( repository, "repository should be initialized" );

        Service<SimpleEntity, UUID> service = getFieldValue( defaultServlet, "service", Service.class );
        assertNotNull( service, "service should be initialized" );
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(Object object, String fieldName, Class<T> clazz) {
        try{
            Field field = object.getClass().getDeclaredField( fieldName );
            field.setAccessible( true );
            return (T) field.get( object );
        } catch(Exception e){
            throw new RuntimeException( "Failed to get the value of the field: " + fieldName, e );
        }
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



    @AfterEach
    public void tearDown() {
        reset( connectionManager );
    }
}
