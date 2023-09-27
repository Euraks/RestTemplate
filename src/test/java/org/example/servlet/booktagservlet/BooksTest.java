package org.example.servlet.booktagservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.when;

class BooksTest {

    private Books servlet;
    private Service<BookEntity, UUID> mockedService;
    private Repository<BookEntity, UUID> mockedRepository;
    private Repository<TagEntity, UUID> mockedTagRepository;
    private ConnectionManager mockedConnectionManager;
    private Connection mockedConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockedService = Mockito.mock( BookServiceImpl.class );
        mockedRepository = Mockito.mock( BookRepositoryImpl.class );
        mockedTagRepository = Mockito.mock( TagRepositoryImpl.class );
        mockedConnectionManager = Mockito.mock( ConnectionManager.class );
        mockedConnection = Mockito.mock( Connection.class );

        when( mockedService.getRepository() ).thenReturn( mockedRepository );
        when( mockedRepository.save( any() ) ).thenReturn( Optional.empty() );
        when( mockedRepository.findAll() ).thenReturn( Collections.emptyList() );
        when( mockedConnectionManager.getConnection() ).thenReturn( mockedConnection );

        servlet = new Books( mockedConnectionManager, mockedTagRepository );
        servlet.setService( mockedService );
    }

    @Test
    void testDefaultConstructor() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );

        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );
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

        String validJson = "{\n" +
                "            \"bookText\": \"Add Book.\",\n" +
                "            \"tagEntities\": [\n" +
                "                {\n" +
                "                    \"uuid\": \"6f213292-1c7f-4086-9f12-a3057178651a\",\n" + //
                "                    \"tagName\": \"Science Fiction\",\n" +
                "                    \"bookEntities\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"uuid\": \"2e372ddd-7717-4417-a34c-e298006559d3\",\n" +
                "                    \"tagName\": \"Novel\",\n" +
                "                    \"bookEntities\": null\n" +
                "                }\n" +
                "            ]\n" +
                "        }";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Assertions.assertTrue( stringWriter.toString().contains( "Added BookEntity UUID:" ), "Response should confirm the entity was added" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_CREATED );
    }

    @Test
    void testDoPostSavesBookEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service<BookEntity, UUID> mockedService = Mockito.mock( Service.class );

        servlet.setMapper( realMapper );
        servlet.setService( mockedService );

        String jsonString = "{\n" +
                "            \"bookText\": \"Add Book.\",\n" +
                "            \"tagEntities\": [\n" +
                "                {\n" +
                "                    \"uuid\": \"6f213292-1c7f-4086-9f12-a3057178651a\",\n" +
                "                    \"tagName\": \"Science Fiction\",\n" +
                "                    \"bookEntities\": null\n" +
                "                },\n" +
                "                {\n" +
                "                    \"uuid\": \"2e372ddd-7717-4417-a34c-e298006559d3\",\n" +
                "                    \"tagName\": \"Novel\",\n" +
                "                    \"bookEntities\": null\n" +
                "                }\n" +
                "            ]\n" +
                "        }";

        BookEntity expectedEntity = realMapper.readValue( jsonString, BookEntity.class );

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        BufferedReader bufferedReader = new BufferedReader( new StringReader( jsonString ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Mockito.verify( mockedService ).save( expectedEntity );
        String expectedMessage = "Added BookEntity UUID:";
        Assertions.assertTrue( stringWriter.toString().contains( expectedMessage ) );
    }

    @Test
    void testDoPostUpdateBookEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service<BookEntity, UUID> mockedService = Mockito.mock( Service.class );

        servlet.setMapper( realMapper );
        servlet.setService( mockedService );

        String validJson = "{\n" +
                "    \"bookText\": \"Update book\",\n" +
                "    \"tagEntities\": [\n" +
                "        {\n" +
                "            \"uuid\": \"d20835bc-6715-48ea-80b4-b2365075cf5e\",\n" +
                "            \"tagName\": \"Update from book\",\n" +
                "            \"bookEntities\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"uuid\": \"90684a02-76cc-4f59-be40-643193ad5c9c\",\n" +
                "            \"tagName\": \"Update from book\",\n" +
                "            \"bookEntities\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"uuid\": \"6f213292-1c7f-4086-9f12-a3057178651a\",\n" +
                "            \"tagName\": \"Update from book\",\n" +
                "            \"bookEntities\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"uuid\": \"2e372ddd-7717-4417-a34c-e298006559d3\",\n" +
                "            \"tagName\": \"Update from book\",\n" +
                "            \"bookEntities\": []\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        BookEntity expectedEntity = realMapper.readValue( validJson, BookEntity.class );

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPost( request, response );

        Mockito.verify( mockedService ).save( expectedEntity );
        String expectedMessage = "Added BookEntity UUID:" ;
        Assertions.assertTrue( stringWriter.toString().contains( expectedMessage ) );
    }

    @Test
    void testSendSuccessResponseViaDoGet() throws Exception {
        HttpServletRequest mockRequest = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = Mockito.mock( HttpServletResponse.class );
        PrintWriter mockWriter = Mockito.mock( PrintWriter.class );

        Service<BookEntity, UUID> mockService = Mockito.mock( Service.class );
        Mockito.when( mockService.findAll() ).thenReturn( Collections.emptyList() );

        Mockito.when( mockResponse.getWriter() ).thenReturn( mockWriter );

        servlet.setService( mockService );

        servlet.doGet( mockRequest, mockResponse );

        Mockito.verify( mockResponse ).setContentType( "application/json" );
        Mockito.verify( mockResponse ).setCharacterEncoding( "UTF-8" );
        Mockito.verify( mockResponse ).setStatus( HttpServletResponse.SC_OK );
        Mockito.verify( mockWriter ).write( startsWith( "GetAll BookEntity:" ) );
    }
}
