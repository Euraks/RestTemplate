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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SimplesTest {

    @InjectMocks
    private Simples servlet;

    @Mock
    private Service<SimpleEntity, UUID> service;

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

        servlet.setService(service);
        servlet.setMapper(new ObjectMapper());
    }

    @Test
    void testDoGet() throws  SQLException {
        SimpleEntity entity = new SimpleEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setDescription("TestDescription");

        when(service.findAll()).thenReturn(Collections.singletonList(entity));

        servlet.doGet(request, response);

        writer.flush();
        assertTrue(stringWriter.toString().contains("TestDescription"));
        assertTrue(stringWriter.toString().contains("GetAll SimpleEntity Jenkins Hello:"));
    }

    @Test
    void testDoPost() throws IOException {
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn("{\"description\":\"TestDescription\"}", null);

        servlet.doPost(request, response);

        writer.flush();
        assertTrue(stringWriter.toString().contains("Added SimpleEntity UUID"));
    }

    @Test
    void testDoPostSQLException() throws IOException, SQLException {
        when(request.getReader()).thenReturn(reader);
        when(reader.readLine()).thenReturn("{\"uuid\":\"550e8400-e29b-41d4-a716-446655440000\",\"description\":\"TestDescription\"}", null);
        doThrow(new SQLException("Simulated SQL Exception")).when(service).save(any());

        servlet.doPost(request, response);

        writer.flush();
        assertTrue(stringWriter.toString().contains("An internal server error occurred."));
    }
}
