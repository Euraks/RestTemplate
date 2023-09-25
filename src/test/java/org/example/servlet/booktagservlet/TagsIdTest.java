package org.example.servlet.booktagservlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.TagServiceImpl;
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
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TagsIdTest {

    private TagsId servlet;
    private Service<TagEntity, UUID> mockedService;
    private Repository<TagEntity, UUID> mockedRepository;
    private ConnectionManager mockedConnectionManager;
    private Connection mockedConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockedService = Mockito.mock( TagServiceImpl.class );
        mockedRepository = Mockito.mock( TagRepositoryImpl.class );
        mockedConnectionManager = Mockito.mock( ConnectionManager.class );
        mockedConnection = Mockito.mock( Connection.class );

        when( mockedService.getRepository() ).thenReturn( mockedRepository );
        when( mockedRepository.save( any() ) ).thenReturn( Optional.empty() );
        when( mockedConnectionManager.getConnection() ).thenReturn( mockedConnection );

        servlet = new TagsId( mockedConnectionManager, mockedRepository );
        servlet.setService( mockedService );
    }

    @Test
    void testDefaultConstructor() throws Exception {
        TagsId servlet = new TagsId();

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        when( request.getPathInfo() ).thenReturn( "/tags/" + UUID.randomUUID() );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );

        Mockito.verify( response ).setStatus( HttpServletResponse.SC_NOT_FOUND );
    }

    @Test
    void testDoGet() throws Exception {
        TagServiceImpl mockService = mock( TagServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        TagEntity mockEntity = new TagEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = TagsId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/tags/" + testUUID );

        servlet.doGet( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( testUUID.toString() ) );
    }

    @Test
    void testDoGetNotFound() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String invalidUUID = "6f213292-1c7f-4086-9f12-a3057178651a";
        Mockito.when( request.getPathInfo() ).thenReturn( "/tags/" + invalidUUID );

        when( mockedService.findById( any() ) ).thenReturn( Optional.empty() );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_NOT_FOUND );
    }

    @Test
    void testDoPut() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String validUUID = "d20835bc-6715-48ea-80b4-b2365075cf5e";
        Mockito.when( request.getPathInfo() ).thenReturn( "/books/" + validUUID );

        String validJson = "{\n" +
                "    \"tagName\": \"Science Fiction update\",\n" +
                "    \"bookEntities\": []\n" +
                "}";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPut( request, response );

        Assertions.assertTrue( stringWriter.toString().contains( "TagEntity updated successfully" ), "Response should confirm the entity was updated" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );
    }

    @Test
    void testDoPutWithInvalidUUID() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String invalidUUID = "invalid-uuid";
        Mockito.when( request.getPathInfo() ).thenReturn( "/books/" + invalidUUID );

        String validJson = "{\"bookText\": \"Updated Book Text\", \"tagEntities\": []}";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPut( request, response );

        Assertions.assertTrue( stringWriter.toString().contains( "An internal server error occurred." ), "Response should indicate invalid UUID format" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }

    @Test
    void testDoDelete() throws Exception {
        TagServiceImpl mockService = mock( TagServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        TagEntity mockEntity = new TagEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = TagsId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/tags/" + testUUID.toString() );

        servlet.doDelete( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Deleted TagEntity UUID:" ) );
    }

    @Test
    void testDoDeleteNotFound() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String invalidUUID = "6f213292-1c7f-4086-9f12-a3057178651a";
        Mockito.when( request.getPathInfo() ).thenReturn( "/tags/" + invalidUUID );

        UUID uuid = UUID.fromString( invalidUUID );

        when( mockedService.delete( uuid ) ).thenReturn( false );

        servlet.doDelete( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );
    }
}
