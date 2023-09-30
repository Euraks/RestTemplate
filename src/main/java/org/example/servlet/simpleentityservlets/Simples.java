package org.example.servlet.simpleentityservlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.repository.impl.SimpleEntityRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.simpleentityDTO.SimpleEntityAllOutGoingDTO;
import org.example.servlet.dto.simpleentityDTO.SimpleEntityIncomingDTO;
import org.example.servlet.dto.simpleentityDTO.mapper.SimpleDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Simples", value = "/simples")
public class Simples extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simples.class);
    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<SimpleEntity, UUID> repository;
    private transient Service<SimpleEntity, UUID> service;

    public Simples() {
        this.connectionManager = new HikariCPDataSource();
        this.repository = new SimpleEntityRepositoryImpl(this.connectionManager);
        this.service = new SimpleServiceImpl(repository);
    }

    public Simples(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new SimpleEntityRepositoryImpl(this.connectionManager);
        this.service = new SimpleServiceImpl(repository);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            List<SimpleEntity> simpleEntityList = service.findAll();
            SimpleEntityAllOutGoingDTO simpleEntityAllOutGoingDTO = SimpleDtoMapper.INSTANCE.mapListToDto(simpleEntityList);
            String jsonString = mapper.writeValueAsString(simpleEntityAllOutGoingDTO);
            response.setContentType(APPLICATION_JSON);
            response.setCharacterEncoding(UTF_8);
            response.getWriter().write("GetAll SimpleEntity:" + jsonString);
        } catch (SQLException e) {
            handleException(response, e, "Failed to fetch all SimpleEntities");
        } catch (Exception e) {
            handleException(response, e, "Failed to process GET request");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            String json = readRequestBody(request).toString();
            SimpleEntityIncomingDTO simpleEntityIncomingDTO = mapper.readValue(json, SimpleEntityIncomingDTO.class);
            SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map(simpleEntityIncomingDTO);
            service.save(simpleEntity);
            response.setContentType(APPLICATION_JSON);
            response.setCharacterEncoding(UTF_8);
            response.getWriter().write("Added SimpleEntity UUID:" + simpleEntity.getUuid());
        } catch (SQLException e) {
            handleException(response, e, "Failed to save the SimpleEntity");
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

    private void handleException(HttpServletResponse response, Exception e, String logMessage) {
        LOGGER.error(logMessage, e);
        try {
            writeResponse(response);
        } catch (IOException ioException) {
            LOGGER.error("Failed to send error response.", ioException);
        }
    }

    private void writeResponse(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("An internal server error occurred.");
    }

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    protected void setService(Service<SimpleEntity, UUID> service) {
        this.service = service;
    }
}
