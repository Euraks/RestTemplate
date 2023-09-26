package org.example.servlet.authorservlet;


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
import org.example.service.AuthorEntityService;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Articles", value = "/articles")
public class Articles extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( Articles.class );
    private final ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final AuthorEntityRepository<AuthorEntity, UUID> repository;
    private final transient AuthorEntityService service;

    public Articles() {
        this.connectionManager = new HikariCPDataSource();
        this.repository = new AuthorEntityRepositoryImpl( this.connectionManager );
        this.service = new AuthorEntityServiceImpl( repository );
    }


    public Articles(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new AuthorEntityRepositoryImpl( this.connectionManager );
        this.service = new AuthorEntityServiceImpl( repository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<AuthorEntity> authorEntityList = service.findAll();
            String jsonString = mapper.writeValueAsString(authorEntityList);
            sendSuccessResponse(response, "GetAll AuthorEntity:" + jsonString );
        } catch (SQLException e) {
            handleException(response, e, "Failed to fetch all AuthorEntities");
        } catch (Exception e) {
            handleException(response, e, "Failed to process GET request");
        }
    }

    private void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
        writeResponse(response, message, HttpServletResponse.SC_OK, "application/json");
    }

    void handleException(HttpServletResponse response, Exception e, String logMessage) {
        LOGGER.error(logMessage, e);
        try {
            writeResponse(response, "An internal server error occurred.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "text/plain");
        } catch (IOException ioException) {
            LOGGER.error("Failed to send error response.", ioException);
        }
    }

    private void writeResponse(HttpServletResponse response, String message, int statusCode, String contentType) throws IOException {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(message);
    }
}
