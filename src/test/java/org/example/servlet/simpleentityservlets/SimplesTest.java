package org.example.servlet.simpleentityservlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.servlet.simpleentityservlets.Simples;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.example.db.ConnectionManager;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

class SimplesTest {

    private Simples servlet;
    private Service<SimpleEntity, UUID> mockedService;
    private Repository<SimpleEntity, UUID> mockedRepository;
    private ConnectionManager mockedConnectionManager;
    private Connection mockedConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockedService = Mockito.mock(Service.class);
        mockedRepository = Mockito.mock(Repository.class);
        mockedConnectionManager = Mockito.mock(ConnectionManager.class);
        mockedConnection = Mockito.mock(Connection.class);

        when(mockedService.getRepository()).thenReturn(mockedRepository);
        when(mockedRepository.save(any())).thenReturn(Optional.empty());
        when(mockedRepository.findAll()).thenReturn(Collections.emptyList());
        when(mockedConnectionManager.getConnection()).thenReturn(mockedConnection);

        servlet = new Simples(mockedConnectionManager);
        servlet.setService(mockedService);
    }

    @Test
    void testDefaultConstructor() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);

        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"description\": \"description\"}";
        BufferedReader bufferedReader = new BufferedReader(new StringReader(validJson));
        when(request.getReader()).thenReturn(bufferedReader);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPostSavesSimpleEntity() throws Exception {
        String validJson = "{\"description\": \"New Simple Postman23232\"}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        BufferedReader bufferedReader = new BufferedReader(new StringReader(validJson));
        when(request.getReader()).thenReturn(bufferedReader);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPostUpdateSimpleEntity() throws Exception {
        String validJson = "{\"uuid\": \"f47ac10b-58cc-4372-a567-0e02b2c3d479\", \"description\": \"Update description\"}";
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
        BufferedReader bufferedReader = new BufferedReader(new StringReader(validJson));
        when(request.getReader()).thenReturn(bufferedReader);

        servlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testSendSuccessResponseViaDoGet() throws Exception {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        PrintWriter mockWriter = mock(PrintWriter.class);

        when(mockedService.findAll()).thenReturn(Collections.emptyList());
        when(mockResponse.getWriter()).thenReturn(mockWriter);

        servlet.doGet(mockRequest, mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_OK);
    }
}
