package org.example.servlet.simpleentityservlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.service.impl.SimpleServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SimplesIdTest {

    private SimplesId servlet;
    private Service<SimpleEntity, UUID> mockedService;
    private Repository<SimpleEntity, UUID> mockedRepository;
    private ConnectionManager mockedConnectionManager;
    private Connection mockedConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockedService = Mockito.mock( Service.class );
        mockedRepository = Mockito.mock( Repository.class );
        mockedConnectionManager = Mockito.mock( ConnectionManager.class );
        mockedConnection = Mockito.mock( Connection.class );

        when( mockedService.getRepository() ).thenReturn( mockedRepository );
        when( mockedRepository.save( any() ) ).thenReturn( Optional.empty() );
        when( mockedRepository.findAll() ).thenReturn( Collections.emptyList() );
        when( mockedConnectionManager.getConnection() ).thenReturn( mockedConnection );

        servlet = new SimplesId( mockedConnectionManager );
        servlet.setService( mockedService );
    }

    @Test
    void testDefaultConstructor() throws Exception {

        Simples servlet = new Simples();

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        Assertions.assertFalse(stringWriter.toString().isEmpty(), "Response body should not be empty");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);

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
    void testHandleExceptionViaDoGet() throws Exception {

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
