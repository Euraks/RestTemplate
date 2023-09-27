package org.example.servlet.authorservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.Repository;
import org.example.service.AuthorEntityService;
import org.example.service.Service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

class AuthorsTest {

    private Authors servlet;
    private Service<AuthorEntity, UUID> mockedService;
    private Repository<AuthorEntity, UUID> mockedRepository;
    private ConnectionManager mockedConnectionManager;
    private Connection mockedConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockedService = Mockito.mock( AuthorEntityService.class );
        mockedRepository = Mockito.mock( AuthorEntityRepository.class );
        mockedConnectionManager = Mockito.mock( ConnectionManager.class );
        mockedConnection = Mockito.mock( Connection.class );

        when( mockedService.getRepository() ).thenReturn( mockedRepository );
        when( mockedRepository.save( any() ) ).thenReturn( Optional.empty() );
        when( mockedRepository.findAll() ).thenReturn( Collections.emptyList() );
        when( mockedConnectionManager.getConnection() ).thenReturn( mockedConnection );

        servlet = new Authors( mockedConnectionManager );
        servlet.setService( mockedService );
    }

    @Test
    void testDefaultConstructor() throws Exception {
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

        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"authorName\": \"authorName\"}";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Assertions.assertTrue( stringWriter.toString().contains( "Added AuthorEntity UUID:" ), "Response should confirm the entity was added" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_CREATED );
    }

    @Test
    void testDoPostSavesAuthorEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service<AuthorEntity, UUID> mockedService = Mockito.mock( Service.class );

        servlet.setMapper( realMapper );
        servlet.setService( mockedService );

        String jsonString = "{\"authorName\":\"New Author Name\",\"articleList\":[{\"text\":\"Article text\"}]}";


        AuthorEntity expectedEntity = realMapper.readValue( jsonString, AuthorEntity.class );

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        BufferedReader bufferedReader = new BufferedReader( new StringReader( jsonString ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Mockito.verify( mockedService ).save( expectedEntity );
        String expectedMessage = "Added AuthorEntity UUID:";
        Assertions.assertTrue( stringWriter.toString().contains( expectedMessage ) );
    }

    @Test
    void testDoPostUpdateAuthorEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service<AuthorEntity,UUID> mockedService = Mockito.mock( Service.class );

        servlet.setMapper( realMapper );
        servlet.setService( mockedService );

        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\",\"authorName\":\"New Author Name\",\"articleList\":[{\"text\":\"Article text\"}]}";

        AuthorEntity expectedEntity = realMapper.readValue( validJson, AuthorEntity.class );

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Mockito.verify( mockedService ).save( expectedEntity );
        String expectedMessage = "Added AuthorEntity UUID:" + expectedEntity.getUuid();
        Assertions.assertTrue( stringWriter.toString().contains( expectedMessage ) );
    }

    @Test
    void testSendSuccessResponseViaDoGet() throws Exception {

        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = Mockito.mock(HttpServletResponse.class);
        PrintWriter mockWriter = Mockito.mock(PrintWriter.class);

        Service<AuthorEntity, UUID> mockService = Mockito.mock(Service.class);
        Mockito.when(mockService.findAll()).thenReturn( Collections.emptyList());

        Mockito.when(mockResponse.getWriter()).thenReturn(mockWriter);

        servlet.setService(mockService);

        servlet.doGet(mockRequest, mockResponse);

        Mockito.verify(mockResponse).setContentType("application/json");
        Mockito.verify(mockResponse).setCharacterEncoding("UTF-8");
        Mockito.verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
        Mockito.verify(mockWriter).write(startsWith("GetAll AuthorEntity:"));
    }
}