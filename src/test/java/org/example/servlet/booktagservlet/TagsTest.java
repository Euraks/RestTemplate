package org.example.servlet.booktagservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TagsTest {

    private Tags servlet;
    private Service<TagEntity, UUID> mockedService;
    private Repository<TagEntity, UUID> mockedRepository;
    private ConnectionManager mockedConnectionManager;
    private Connection mockedConnection;

    @BeforeEach
    void setUp() throws SQLException {
        mockedService = Mockito.mock( TagServiceImpl.class);
        mockedRepository = Mockito.mock( TagRepositoryImpl.class);
        mockedConnectionManager = Mockito.mock(ConnectionManager.class);
        mockedConnection = Mockito.mock(Connection.class);

        when(mockedService.getRepository()).thenReturn(mockedRepository);
        when(mockedRepository.save(any())).thenReturn( Optional.empty());
        when(mockedRepository.findAll()).thenReturn(Collections.emptyList());
        when(mockedConnectionManager.getConnection()).thenReturn(mockedConnection);

        servlet = new Tags(mockedConnectionManager);
        servlet.setService(mockedService);
    }

    @Test
    void testDefaultConstructor() throws Exception {
        Tags servlet = new Tags();

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
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        servlet.doGet(request, response);

        Assertions.assertFalse(stringWriter.toString().isEmpty(), "Response body should not be empty");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void testDoPost() throws Exception {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));

        String validJson = "{    \n" +
                "    \"tagName\": \"Science Fiction 2222\",\n" +
                "    \"bookEntities\": [\n" +
                "        {\n" +
                "            \"uuid\": \"123e4567-e89b-12d3-a456-426655440000\",\n" +
                "            \"bookText\": \"Add from tag\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"uuid\": \"123e4567-e89b-12d3-a456-426655440001\",\n" +
                "            \"bookText\": \"Add from tag\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        BufferedReader bufferedReader = new BufferedReader(new StringReader(validJson));
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        servlet.doPost(request, response);

        Assertions.assertTrue(stringWriter.toString().contains("Added TagEntity UUID:"), "Response should confirm the entity was added");
        Mockito.verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPostSavesTagEntity() throws Exception {
        ObjectMapper realMapper = new ObjectMapper();
        Service<TagEntity, UUID> mockedService = Mockito.mock(Service.class);

        servlet.setMapper(realMapper);
        servlet.setService(mockedService);

        String jsonString = "{    \n" +
                "    \"tagName\": \"Science Fiction 2222\",\n" +
                "    \"bookEntities\": [\n" +
                "        {\n" +
                "            \"uuid\": \"123e4567-e89b-12d3-a456-426655440000\",\n" +
                "            \"bookText\": \"Add from tag\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"uuid\": \"123e4567-e89b-12d3-a456-426655440001\",\n" +
                "            \"bookText\": \"Add from tag\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        TagEntity expectedEntity = realMapper.readValue(jsonString, TagEntity.class);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        StringWriter stringWriter = new StringWriter();
        Mockito.when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
        BufferedReader bufferedReader = new BufferedReader(new StringReader(jsonString));
        Mockito.when(request.getReader()).thenReturn(bufferedReader);

        servlet.doPost(request, response);

        Mockito.verify(mockedService).save(expectedEntity);
        String expectedMessage = "Added TagEntity UUID:";
        Assertions.assertTrue(stringWriter.toString().contains(expectedMessage));
    }
}
