package org.example.servlet.simpleentityservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.SimpleEntity;
import org.example.service.Service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimplesIdTest {

    @InjectMocks
    private SimplesId servlet;

    @Mock
    private Service<SimpleEntity, UUID> mockedService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private BufferedReader reader;

    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        servlet.setService(mockedService);
        servlet.setMapper(new ObjectMapper());
    }

    @Test
    void testDoGet() throws Exception {
        UUID testUUID = UUID.randomUUID();
        SimpleEntity mockEntity = new SimpleEntity();
        mockEntity.setUuid( testUUID );

        when( mockedService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );
        when( request.getPathInfo() ).thenReturn( "/" + testUUID );

        servlet.doGet( request, response );

        writer.flush();
        assertTrue( stringWriter.toString().contains( "Get SimpleEntity UUID:" ) );
        assertTrue( stringWriter.toString().contains( testUUID.toString() ) );
    }

    @Test
    void testDoPut() throws Exception {
        UUID testUUID = UUID.randomUUID();
        SimpleEntity mockEntity = new SimpleEntity();
        mockEntity.setUuid( testUUID );

        when( mockedService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );
        when( request.getPathInfo() ).thenReturn( "/" + testUUID );

        String inputJson = "{ \"description\": \"Test Description\" }";
        when( request.getReader() ).thenReturn( new BufferedReader( new StringReader( inputJson ) ) );

        servlet.doPut( request, response );

        writer.flush();
        assertTrue( stringWriter.toString().contains( "Updated SimpleEntity UUID:" ) );
    }

    @Test
    void testDoDelete() throws Exception {
        UUID testUUID = UUID.randomUUID();
        SimpleEntity mockEntity = new SimpleEntity();
        mockEntity.setUuid( testUUID );

        when( mockedService.findById( testUUID ) ).thenReturn( Optional.of( mockEntity ) );
        when( request.getPathInfo() ).thenReturn( "/" + testUUID );

        servlet.doDelete( request, response );

        writer.flush();
        assertTrue( stringWriter.toString().contains( "Deleted SimpleEntity UUID:" ) );
        assertTrue( stringWriter.toString().contains( testUUID.toString() ) );
    }

    @Test
    void testHandleExceptionViaDoGet()  {
        when( request.getPathInfo() ).thenReturn( "/invalidUUID" );

        servlet.setService( mockedService );

        servlet.doGet( request, response );

        verify( response ).setContentType( "application/json" );
        verify( response ).setCharacterEncoding( "UTF-8" );
        verify( response ).setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
    }
}
