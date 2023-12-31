package org.example.servlet.booktagservlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.TagServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
@WebServlet(name = "TagsId", value = "/tags/*")
public class TagsId extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( TagsId.class );
    private final ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<TagEntity, UUID> tagRepository;
    private Service<TagEntity, UUID> service;

    public TagsId() {
        this.connectionManager = new HikariCPDataSource();
        this.tagRepository = new TagRepositoryImpl( connectionManager );
        this.service = new TagServiceImpl( tagRepository );
    }

    public TagsId(ConnectionManager connectionManager, Repository<TagEntity, UUID> tagRepository) {
        this.connectionManager = connectionManager;
        this.tagRepository = tagRepository;
        this.service = new TagServiceImpl( tagRepository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String uuid = extractIdFromRequest( request );
            if (uuid == null) {
                sendBadRequest( response, "Invalid ID format" );
                return;
            }
            processGetRequest( uuid, response );
        } catch (Exception e) {
            handleException( response, e, "Failed to process GET request" );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String id = extractIdFromRequest( request );
            if (id == null) {
                sendBadRequest( response, "Invalid ID format" );
                return;
            }
            processPutRequest( id, request, response );
        } catch (Exception e) {
            handleException( response, e, "Failed to process PUT request" );
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID entityUUID = UUID.fromString( pathParts[pathParts.length - 1] );
                    service.delete( entityUUID );
                    response.setContentType( "application/json" );
                    response.setCharacterEncoding( "UTF-8" );
                    response.getWriter().write( "Deleted TagEntity UUID:" + entityUUID );
                    response.setStatus( HttpServletResponse.SC_OK );
                }
            }
        } catch (Exception e) {
            handleException( response, e, "Failed to process DELETE request" );
        }
    }

    private String extractIdFromRequest(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty()) {
            return null;
        }
        String[] pathParts = pathInfo.split( "/" );
        return pathParts.length > 1 ? String.valueOf( UUID.fromString( pathParts[pathParts.length - 1] ) ) : null;
    }

    private void processGetRequest(String id, HttpServletResponse response) throws IOException, SQLException {
        setResponseDefaults( response );

        UUID uuid;
        try {
            uuid = UUID.fromString( id );
        } catch (IllegalArgumentException e) {
            sendBadRequest( response, "Invalid UUID format" );
            return;
        }

        Optional<TagEntity> tagEntityOpt = service.findById( uuid );
        if (tagEntityOpt.isEmpty()) {
            sendNotFound( response );
            return;
        }
        TagEntity tagEntity = tagEntityOpt.get();
        String jsonString = mapper.writeValueAsString( tagEntity );
        response.getWriter().write( jsonString );
    }

    private void processPutRequest(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        setResponseDefaults( response );

        UUID uuid;
        try {
            uuid = UUID.fromString( id );
        } catch (IllegalArgumentException e) {
            sendBadRequest( response, "Invalid UUID format" );
            return;
        }

        StringBuilder sb = getStringFromRequest( request );
        TagEntity tagEntity;
        try {
            tagEntity = mapper.readValue( sb.toString(), TagEntity.class );
        } catch (JsonProcessingException e) {
            sendBadRequest( response, "Invalid JSON format" );
            return;
        }

        tagEntity.setUuid( uuid );

        try {
            service.save( tagEntity );
            response.getWriter().write( "TagEntity updated successfully" );
        } catch (SQLException e) {
            LOGGER.error( "Failed to save TagEntity", e );
            response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        }
    }

    private void sendBadRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
        response.getWriter().write( message );
    }

    private void sendNotFound(HttpServletResponse response) throws IOException {
        response.setStatus( HttpServletResponse.SC_NOT_FOUND );
        response.getWriter().write( "TagEntity not found" );
    }

    private void setResponseDefaults(HttpServletResponse response) {
        response.setStatus( HttpServletResponse.SC_OK );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
    }

    private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
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
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "An internal server error occurred." );
    }

    protected void setService(Service<TagEntity, UUID> service) {
        this.service = service;
    }
}
