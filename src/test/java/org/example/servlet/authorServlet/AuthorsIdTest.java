package org.example.servlet.authorServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.AuthorEntity;
import org.example.model.SimpleEntity;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.simpleentityservlets.SimplesId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


class AuthorsIdTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "testcontainers" )
            .withUsername( "testcontainers" )
            .withPassword( "testcontainers" )
            .withInitScript( "db.sql" );

    private AuthorsId servlet;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };

        servlet = new AuthorsId( testConnectionManager );

        postgreSQLContainer.start();
    }

    @AfterEach
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    void testDefaultConstructor() throws Exception {
        postgreSQLContainer.start();

        AuthorsId servlet = new AuthorsId();

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        when( request.getPathInfo() ).thenReturn( "/authors/" + UUID.randomUUID() );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );

        postgreSQLContainer.stop();
    }

    @Test
    void testDoGet() throws Exception {
        AuthorEntityServiceImpl mockService = mock( AuthorEntityServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        AuthorEntity mockEntity = new AuthorEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = AuthorsId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/authors/" + testUUID );

        servlet.doGet( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( testUUID.toString() ) );
    }

    @Test
    void testDoPut() throws Exception {
        AuthorEntityServiceImpl mockService = mock( AuthorEntityServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        AuthorEntity mockEntity = new AuthorEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = AuthorsId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/authors/" + testUUID.toString() );

        testPutMethod( testUUID, mockRequest, mockResponse, stringWriter );
        testUpdateMethod( testUUID, mockRequest, mockResponse, stringWriter );
    }

    private void testUpdateMethod(UUID testUUID, HttpServletRequest mockRequest, HttpServletResponse mockResponse, StringWriter stringWriter) throws IOException {
        String inputJson = "{\n" +
                "    \"authorName\": \"Update authorName\",\n" +
                "    \"articleList\": [\n" +
                "        {\n" +
                "            \"uuid\": \"866e2781-a445-487d-813c-edbcd4f299f7\",\n" +
                "            \"author_uuid\": \"" + testUUID + "\",\n" +
                "            \"text\": \"Update articleText\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        when( mockRequest.getReader() ).thenReturn( new BufferedReader( new StringReader( inputJson ) ) );

        servlet.doPut( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Author updated successfully" ) );
    }

    private void testPutMethod(UUID testUUID, HttpServletRequest mockRequest, HttpServletResponse mockResponse, StringWriter stringWriter) throws IOException {
        String inputJson = "{\n" +
                "    \"authorName\": \"Create authorName\",\n" +
                "    \"articleList\": [\n" +
                "        {\n" +
                "            \"uuid\": \"866e2781-a445-487d-813c-edbcd4f299f7\",\n" +
                "            \"author_uuid\": \"" + testUUID + "\",\n" +
                "            \"text\": \"Create articleText\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        when( mockRequest.getReader() ).thenReturn( new BufferedReader( new StringReader( inputJson ) ) );

        servlet.doPut( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Author updated successfully" ) );
    }

    @Test
    void testDoDelete() throws Exception {
        AuthorEntityServiceImpl mockService = mock( AuthorEntityServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        AuthorEntity mockEntity = new AuthorEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = AuthorsId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/authors/" + testUUID.toString() );

        servlet.doDelete( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( "Deleted AuthorEntity UUID:" ) );
    }

    @Test
    void testHandleExceptionViaDoGet() throws Exception {

        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PrintWriter mockWriter = mock(PrintWriter.class);

        when(mockRequest.getPathInfo()).thenReturn("/authors/invalidUUID");
        when(mockResponse.getWriter()).thenReturn(mockWriter);

        SimplesId servlet = new SimplesId();

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setContentType("application/json");
        verify(mockResponse).setCharacterEncoding("UTF-8");
        verify(mockResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        verify(mockWriter).write("An internal server error occurred.");
    }

}