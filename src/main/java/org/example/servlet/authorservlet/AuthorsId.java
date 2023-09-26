package org.example.servlet.authorservlet;


import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "AuthorsId", value = "/authors/*")
public class AuthorsId extends HttpServlet {

    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";
    private static final Logger LOGGER = LoggerFactory.getLogger( AuthorsId.class );

    private ObjectMapper mapper = new ObjectMapper();

    private final transient ConnectionManager connectionManager;
    private final transient AuthorEntityRepository<AuthorEntity, UUID> repository;
    private  transient Service<AuthorEntity, UUID> service;

    public AuthorsId() {
        this(new HikariCPDataSource());
    }

    public AuthorsId(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new AuthorEntityRepositoryImpl(this.connectionManager);
        this.service = new AuthorEntityServiceImpl(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String uuid = extractIdFromRequest( request );
            if (uuid == null) {
                sendBadRequest( response, "Invalid ID format" );
                return;
            }
            processGetRequest( uuid, response );
        } catch(Exception e){
            handleException( response, e, "Failed to process GET request" );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String uuid = extractIdFromRequest( request );
            if (uuid == null) {
                sendBadRequest( response, "Invalid ID format" );
                return;
            }
            processPutRequest( uuid, request, response );
        } catch(Exception e){
            handleException( response, e, "Failed to process PUT request" );
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID entityUUID = UUID.fromString(pathParts[pathParts.length - 1]);
                    service.delete( entityUUID );
                    response.setContentType( APPLICATION_JSON );
                    response.setCharacterEncoding( UTF_8 );
                    response.getWriter().write( "Deleted AuthorEntity UUID:" + entityUUID );
                }
            }
        } catch(Exception e){
            handleException( response, e, "Failed to process DELETE request" );
        }
    }

    private String extractIdFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            return null;
        }
        String[] pathParts = pathInfo.split("/");
        return pathParts.length > 1 ? String.valueOf( UUID.fromString(pathParts[pathParts.length - 1]) ) : null;
    }

    private void processGetRequest(String id, HttpServletResponse response) throws IOException, SQLException {
        setResponseDefaults( response );

        UUID uuid;
        try{
            uuid = UUID.fromString( id );
        } catch(IllegalArgumentException e){
            sendBadRequest( response, "Invalid UUID format" );
            return;
        }

        Optional<AuthorEntity> authorEntityOpt = service.findById( uuid );
        if (authorEntityOpt.isEmpty()) {
            sendNotFound( response );
            return;
        }
        AuthorEntity authorEntity = authorEntityOpt.get();
        String jsonString = mapper.writeValueAsString( authorEntity );
        response.getWriter().write( jsonString );
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

        StringBuilder sb = getStringFromRequest( request );
        AuthorEntity authorEntity;
        try {
            authorEntity = mapper.readValue( sb.toString(), AuthorEntity.class );
        } catch (JsonProcessingException e) {
            sendBadRequest(response, "Invalid JSON format");
            return;
        }

        authorEntity.setUuid( uuid );

        try{
            service.save( authorEntity );
            response.getWriter().write( "Author updated successfully" );
        } catch (SQLException e) {
            LOGGER.error("Failed to save AuthorEntity", e);
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
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(APPLICATION_JSON);
        response.setCharacterEncoding(UTF_8);
    }

    private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()){
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append( line );
            }
        }
        return sb;
    }

    private void handleException(HttpServletResponse response, Exception e, String logMessage) throws IOException {
        LOGGER.error( logMessage, e );
        response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        response.setContentType( "text/plain" );
        response.setCharacterEncoding( UTF_8 );
        response.getWriter().write( "An internal server error occurred." );
    }

    protected void setService(Service<AuthorEntity, UUID> service) {
        this.service = service;
    }

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

}
