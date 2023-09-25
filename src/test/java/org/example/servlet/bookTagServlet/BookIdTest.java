package org.example.servlet.bookTagServlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.AuthorEntity;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.service.impl.BookServiceImpl;
import org.example.servlet.authorServlet.AuthorsId;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Testcontainers
class BookIdTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript( "db.sql" );

    private BookId servlet;
    private Repository<TagEntity, UUID> tagRepository;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };

        tagRepository = new TagRepositoryImpl( testConnectionManager );
        servlet = new BookId( testConnectionManager, tagRepository );

        postgreSQLContainer.start();
    }

    @AfterEach
    void tearDown() {
        postgreSQLContainer.stop();
    }

    @Test
    void testDefaultConstructor() throws Exception {
        postgreSQLContainer.start();

        BookId servlet = new BookId();

        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );
        when( request.getPathInfo() ).thenReturn( "/books/" + UUID.randomUUID() );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );

        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );

        postgreSQLContainer.stop();
    }

    @Test
    void testDoGet() throws Exception {
        BookServiceImpl mockService = mock( BookServiceImpl.class );

        UUID testUUID = UUID.randomUUID();
        BookEntity mockEntity = new BookEntity();
        mockEntity.setUuid( testUUID );

        when( mockService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );

        Field serviceField = BookId.class.getDeclaredField( "service" );
        serviceField.setAccessible( true );
        serviceField.set( servlet, mockService );

        HttpServletRequest mockRequest = mock( HttpServletRequest.class );
        HttpServletResponse mockResponse = mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter( stringWriter );
        when( mockResponse.getWriter() ).thenReturn( writer );
        when( mockRequest.getPathInfo() ).thenReturn( "/books/" + testUUID );

        servlet.doGet( mockRequest, mockResponse );

        Assertions.assertTrue( stringWriter.toString().contains( testUUID.toString() ) );
    }

    @Test
    void testDoGetWithValidUUID() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        // Replace 'validUUID' with a valid UUID string
        String validUUID = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Mockito.when( request.getPathInfo() ).thenReturn( "/" + validUUID );

        servlet.doGet( request, response );

        Assertions.assertFalse( stringWriter.toString().isEmpty(), "Response body should not be empty" );
        Mockito.verify( response ).setStatus( HttpServletResponse.SC_OK );
    }

    @Test
    void testDoPut() throws Exception {
        HttpServletRequest request = Mockito.mock( HttpServletRequest.class );
        HttpServletResponse response = Mockito.mock( HttpServletResponse.class );
        StringWriter stringWriter = new StringWriter();
        Mockito.when( response.getWriter() ).thenReturn( new PrintWriter( stringWriter ) );

        String validUUID = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        Mockito.when( request.getPathInfo() ).thenReturn( "/books/" + validUUID );

        // Replace 'validJson' with a valid JSON request body
        String validJson = "{\"bookText\": \"Updated Book Text\", \"tagEntities\": []}";
        BufferedReader bufferedReader = new BufferedReader( new StringReader( validJson ) );
        Mockito.when( request.getReader() ).thenReturn( bufferedReader );

        servlet.doPut( request, response );

        Assertions.assertTrue( stringWriter.toString().contains( "BookEntity updated successfully" ), "Response should confirm the entity was updated" );
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


}
