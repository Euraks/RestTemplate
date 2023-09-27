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
import org.example.servlet.dto.simpleentityDTO.SimpleEntityOutGoingDTO;
import org.example.servlet.dto.simpleentityDTO.SimpleEntityUpdateDTO;
import org.example.servlet.dto.simpleentityDTO.mapper.SimpleDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@WebServlet(name = "SimplesId", value = "/simples/*")
public class SimplesId extends HttpServlet {

    private static final String APPLICATION_JSON = "application/json";
    private static final String UTF_8 = "UTF-8";

    private static final Logger LOGGER = LoggerFactory.getLogger( SimplesId.class );
    private ObjectMapper mapper = new ObjectMapper();

    private final transient ConnectionManager connectionManager;
    private final transient Repository<SimpleEntity, UUID> repository;
    private transient Service<SimpleEntity, UUID> service;

    public SimplesId() {
        this.connectionManager = new HikariCPDataSource();
        this.repository = new SimpleEntityRepositoryImpl( connectionManager );
        this.service = new SimpleServiceImpl( repository );
    }

    public SimplesId(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new SimpleEntityRepositoryImpl( connectionManager );
        this.service = new SimpleServiceImpl( repository );
    }
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
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
                        response.setContentType( APPLICATION_JSON );
                        response.setCharacterEncoding( UTF_8 );
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
                        response.setContentType( APPLICATION_JSON );
                        response.setCharacterEncoding( UTF_8 );
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
                    UUID entityUUID = UUID.fromString( pathParts[pathParts.length - 1] );
                    service.delete( entityUUID );
                    response.setContentType( APPLICATION_JSON );
                    response.setCharacterEncoding( UTF_8 );
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
            writeResponse( response );
        } catch(IOException ioException){
            LOGGER.error( "Failed to send error response.", ioException );
        }
    }

    private void writeResponse(HttpServletResponse response) throws IOException {
        response.setContentType( SimplesId.APPLICATION_JSON );
        response.setCharacterEncoding( UTF_8 );
        response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        response.getWriter().write( "An internal server error occurred." );
    }

    protected void setService(Service<SimpleEntity, UUID> service) {
        this.service = service;
    }

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }
}
