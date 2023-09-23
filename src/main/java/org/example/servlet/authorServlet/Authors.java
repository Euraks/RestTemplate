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
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityIncomingDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.AuthorEntityMapper;
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
    private Service<AuthorEntity, UUID> service;

    public Authors() {
        this(new HikariCPDataSource());
    }

    public Authors(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new AuthorEntityRepositoryImpl(this.connectionManager);
        this.service = new AuthorEntityServiceImpl(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        setResponseDefaults(response);

        List<AuthorEntity> authorEntityList;
        try {
            authorEntityList = service.findAll();
        } catch (SQLException e) {
            LOGGER.error("Failed to fetch all AuthorEntities", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        AuthorEntityAllOutGoingDTO authorEntityAllOutGoingDTO = AuthorEntityMapper.INSTANCE.mapListToDto(authorEntityList);
        String jsonString = mapper.writeValueAsString(authorEntityAllOutGoingDTO);
        response.getWriter().write(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setResponseDefaults(response);

        StringBuilder sb = getStringFromRequest(request);
        AuthorEntityIncomingDTO authorEntityIncomingDTO;
        try {
            authorEntityIncomingDTO = mapper.readValue(sb.toString(), AuthorEntityIncomingDTO.class);
        } catch (JsonProcessingException e) {
            sendBadRequest(response, "Invalid JSON format");
            return;
        }

        AuthorEntity authorEntity = AuthorEntityMapper.INSTANCE.map(authorEntityIncomingDTO);
        try {
            service.save(authorEntity);
        } catch (SQLException e) {
            LOGGER.error("Failed to save AuthorEntity", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.getWriter().write("Added SimpleEntity UUID:" + authorEntity.getUuid());
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

    private void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write(message);
    }

    private void setResponseDefaults(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}
