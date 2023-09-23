package org.example.servlet.authorServlet;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityUpdateDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.AuthorEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;


@WebServlet(name = "AuthorsId", value = "/authors/*")
public class AuthorsId extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Authors.class);
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final AuthorEntityRepository<AuthorEntity, UUID> repository;
    private Service<AuthorEntity, UUID> service;

    public AuthorsId() {
        this(new HikariCPDataSource());
    }

    public AuthorsId(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new AuthorEntityRepositoryImpl(this.connectionManager);
        this.service = new AuthorEntityServiceImpl(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = extractIdFromRequest(request);
        if (id == null) {
            sendBadRequest(response, "Invalid ID format");
            return;
        }
        processGetRequest(id, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = extractIdFromRequest(request);
        if (id == null) {
            sendBadRequest(response, "Invalid ID format");
            return;
        }
        processPutRequest(id, request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = extractIdFromRequest(request);
        if (id == null) {
            sendBadRequest(response, "Invalid ID format");
            return;
        }
        processDeleteRequest(id, response);
    }

    private String extractIdFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            return null;
        }
        String[] pathParts = pathInfo.split("/");
        return pathParts.length > 1 ? pathParts[1] : null;
    }

    private void processGetRequest(String id, HttpServletResponse response) throws IOException {
        setResponseDefaults(response);

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            sendBadRequest(response, "Invalid UUID format");
            return;
        }

        Optional<AuthorEntity> authorEntityOpt = service.findById(uuid);
        if (authorEntityOpt.isEmpty()) {
            sendNotFound(response);
            return;
        }
        AuthorEntityOutGoingDTO authorEntityOutGoingDTO = AuthorEntityMapper.INSTANCE.map(authorEntityOpt.get());
        String jsonString = mapper.writeValueAsString(authorEntityOutGoingDTO);
        response.getWriter().write(jsonString);
    }

    private void processPutRequest(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        setResponseDefaults(response);

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            sendBadRequest(response, "Invalid UUID format");
            return;
        }

        StringBuilder sb = getStringFromRequest(request);
        AuthorEntityUpdateDTO authorEntityUpdateDTO;
        try {
            authorEntityUpdateDTO = mapper.readValue(sb.toString(), AuthorEntityUpdateDTO.class);
        } catch (JsonProcessingException e) {
            sendBadRequest(response, "Invalid JSON format");
            return;
        }

        AuthorEntity updateAuthorEntity = AuthorEntityMapper.INSTANCE.map(authorEntityUpdateDTO);

        Optional<AuthorEntity> newAuthorEntityOpt = service.findById(uuid);
        if (newAuthorEntityOpt.isEmpty()) {
            sendNotFound(response);
            return;
        }
        AuthorEntity newAuthorEntity = newAuthorEntityOpt.get();
        newAuthorEntity.setAuthorName(updateAuthorEntity.getAuthorName());
        newAuthorEntity.setArticleList(updateAuthorEntity.getArticleList());

        try {
            service.save(newAuthorEntity);
        } catch (SQLException e) {
            LOGGER.error("Failed to save AuthorEntity", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.getWriter().write("Author updated successfully");
    }

    private void processDeleteRequest(String id, HttpServletResponse response) throws IOException {
        setResponseDefaults(response);

        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            sendBadRequest(response, "Invalid UUID format");
            return;
        }

        try {
            if (service.delete(uuid)) {
                response.getWriter().write("Author deleted successfully");
                LOGGER.info("Successfully deleted Author with UUID: {}", id);
            } else {
                sendNotFound(response);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to delete AuthorEntity with UUID: {}", id, e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(message);
    }

    private void sendNotFound(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("Author not found");
    }

    private void setResponseDefaults(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb;
    }
}
