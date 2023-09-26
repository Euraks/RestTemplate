package org.example.servlet.authorservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Authors", value = "/authors")
public class Authors extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Authors.class);
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final AuthorEntityRepository<AuthorEntity, UUID> repository;
    private transient Service<AuthorEntity, UUID> service;

    public Authors() {
        this.connectionManager = new HikariCPDataSource();
        this.repository = new AuthorEntityRepositoryImpl(this.connectionManager);
        this.service = new AuthorEntityServiceImpl(repository);
    }

    public Authors(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new AuthorEntityRepositoryImpl(this.connectionManager);
        this.service = new AuthorEntityServiceImpl(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<AuthorEntity> authorEntityList = service.findAll();
            String jsonString = mapper.writeValueAsString(authorEntityList);
            sendSuccessResponse(response, "GetAll AuthorEntity:" + jsonString, HttpServletResponse.SC_OK);
        } catch (SQLException e) {
            handleException(response, e, "Failed to fetch all AuthorEntities");
        } catch (Exception e) {
            handleException(response, e, "Failed to process GET request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String json = readRequestBody(request).toString();
            AuthorEntity authorEntity = mapper.readValue(json, AuthorEntity.class);
            service.save(authorEntity);
            sendSuccessResponse(response, "Added AuthorEntity UUID:" +
                    authorEntity.getUuid(), HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            handleException(response, e, "Failed to save the AuthorEntity");
        } catch (Exception e) {
            handleException(response, e, "Failed to process POST request");
        }
    }

    private StringBuilder readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb;
    }

    private void sendSuccessResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        writeResponse(response, message, statusCode, "application/json");
    }

    void handleException(HttpServletResponse response, Exception e, String logMessage) {
        LOGGER.error(logMessage, e);
        try {
            writeResponse(response, "An internal server error occurred.",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "text/plain");
        } catch (IOException ioException) {
            LOGGER.error("Failed to send error response.", ioException);
        }
    }

    private void writeResponse(HttpServletResponse response, String message, int statusCode,
                               String contentType) throws IOException {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    protected void setService(Service<AuthorEntity, UUID> service) {
        this.service = service;
    }
}
