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
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityOutGoingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityUpdateDTO;
import org.example.servlet.dto.SimpleEntityDTO.mapper.SimpleDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "SimplesId", value = "/simples/*")
public class SimplesId extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( SimplesId.class );
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<SimpleEntity, UUID> repository;
    private Service<SimpleEntity, UUID> service;

    public SimplesId() {
        this.connectionManager = instantiateConnectionManager();
        this.repository = instantiateRepository( this.connectionManager );
        this.service = instantiateService( repository );
    }

    public SimplesId(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = instantiateRepository( this.connectionManager );
        this.service = instantiateService( repository );
    }

    private ConnectionManager instantiateConnectionManager() {
        return new HikariCPDataSource();
    }

    private Repository<SimpleEntity, UUID> instantiateRepository(ConnectionManager connectionManager) {
        return new SimpleEntityRepositoryImpl( connectionManager );
    }

    private Service<SimpleEntity, UUID> instantiateService(Repository<SimpleEntity, UUID> repository) {
        return new SimpleServiceImpl( repository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID entityUUID = UUID.fromString(pathParts[pathParts.length - 1]);
                    Optional<SimpleEntity> optionalSimpleEntity = service.findById( entityUUID );

                    if (optionalSimpleEntity.isPresent()) {
                        SimpleEntity simpleEntity = optionalSimpleEntity.get();
                        SimpleEntityOutGoingDTO simpleEntityOutGoingDTO = SimpleDtoMapper.INSTANCE.map( simpleEntity );
                        String jsonString = mapper.writeValueAsString( simpleEntityOutGoingDTO );
                        response.setContentType( "application/json" );
                        response.setCharacterEncoding( "UTF-8" );
                        response.getWriter().write( "Get SimpleEntity UUID:" + jsonString );
                    } else {
                        response.setStatus( HttpServletResponse.SC_NOT_FOUND );
                        response.getWriter().write( "The requested entity was not found." );
                    }
                }
            }
        } catch(Exception e){
            handleException( response, e, "Failed to process GET request" );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID entityUUID = UUID.fromString(pathParts[pathParts.length - 1]);
                    StringBuilder sb = getStringFromRequest( request );
                    String json = sb.toString();

                    SimpleEntityUpdateDTO simpleEntityUpdateDTO = mapper.readValue( json, SimpleEntityUpdateDTO.class );
                    SimpleEntity updateSimpleEntity = SimpleDtoMapper.INSTANCE.map( simpleEntityUpdateDTO );

                    Optional<SimpleEntity> optionalNewSimpleEntity = service.findById( entityUUID );

                    if (optionalNewSimpleEntity.isPresent()) {
                        SimpleEntity newSimpleEntity = optionalNewSimpleEntity.get();
                        newSimpleEntity.setDescription( updateSimpleEntity.getDescription() );
                        service.save( newSimpleEntity );
                        response.setContentType( "application/json" );
                        response.setCharacterEncoding( "UTF-8" );
                        response.getWriter().write( "Updated SimpleEntity UUID:" + json );
                    } else {
                        response.setStatus( HttpServletResponse.SC_NOT_FOUND );
                        response.getWriter().write( "The requested entity was not found." );
                    }
                }
            }
        } catch(SQLException e){
            handleException( response, e, "Failed to save the updated entity" );
        } catch(Exception e){
            handleException( response, e, "Failed to process PUT request" );
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID entityUUID = UUID.fromString(pathParts[pathParts.length - 1]);
                    service.delete( entityUUID );
                    response.setContentType( "application/json" );
                    response.setCharacterEncoding( "UTF-8" );
                    response.getWriter().write( "Deleted SimpleEntity UUID:" + entityUUID );
                }
            }
        } catch(Exception e){
            handleException( response, e, "Failed to process DELETE request" );
        }
    }

    private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()){
            while ((line = reader.readLine()) != null) {
                sb.append( line );
            }
        }
        return sb;
    }

    private void handleException(HttpServletResponse response, Exception e, String logMessage) {
        LOGGER.error( logMessage, e );
        try{
            writeResponse( response, "An internal server error occurred.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "application/json" );
        } catch(IOException ioException){
            LOGGER.error( "Failed to send error response.", ioException );
        }
    }

    private void writeResponse(HttpServletResponse response, String message, int statusCode, String contentType) throws IOException {
        response.setContentType( contentType );
        response.setCharacterEncoding( "UTF-8" );
        response.setStatus( statusCode );
        response.getWriter().write( message );
    }
}
