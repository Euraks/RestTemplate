package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
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
import java.util.UUID;

@WebServlet(name = "Simples", value = "/simples")
public class SimplesId extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimplesId.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<SimpleEntity, UUID> repository;
    private final Service<SimpleEntity, UUID> service;

    public SimplesId(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new SimpleEntityRepositoryImpl(this.connectionManager);
        this.service = new SimpleServiceImpl(repository);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    String id = pathParts[1];
                    SimpleEntity simpleEntity = service.findById( UUID.fromString( id ) );
                    SimpleEntityOutGoingDTO simpleEntityOutGoingDTO = SimpleDtoMapper.INSTANCE.map( simpleEntity );
                    String jsonString = mapper.writeValueAsString( simpleEntityOutGoingDTO );

                    response.setContentType( "application/json" );
                    response.setCharacterEncoding( "UTF-8" );
                    response.getWriter().write( "Get SimpleEntity UUID:" + jsonString );
                }
            }
        } catch(Exception e){
            handleException( response, e, "Failed to process GET request" );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    String id = pathParts[1];
                    StringBuilder sb = getStringFromRequest( request );

                    String json = sb.toString();

                    SimpleEntityUpdateDTO simpleEntityUpdateDTO = mapper.readValue( json, SimpleEntityUpdateDTO.class );

                    SimpleEntity updateSimpleEntity = SimpleDtoMapper.INSTANCE.map( simpleEntityUpdateDTO );
                    SimpleEntity newSimpleEntity = service.findById( UUID.fromString( id ) );
                    newSimpleEntity.setDescription( updateSimpleEntity.getDescription() );
                    service.save( newSimpleEntity );

                    response.setContentType( "application/json" );
                    response.setCharacterEncoding( "UTF-8" );
                    response.getWriter().write( "Updated SimpleEntity UUID:" + json );
                }
            }
        } catch(SQLException e){
            handleException( response, e, "Failed to save the updated entity" );
        } catch(Exception e){
            handleException( response, e, "Failed to process PUT request" );
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            String pathInfo = request.getPathInfo();
            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    String id = pathParts[1];
                    service.delete( UUID.fromString( id ) );
                    response.setContentType( "application/json" );
                    response.setCharacterEncoding( "UTF-8" );
                    response.getWriter().write( "Deleted SimpleEntity UUID:" + id );
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
        response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        try{
            response.getWriter().write( "An internal server error occurred." );
        } catch(IOException ioException){
            LOGGER.error( "Failed to send error response.", ioException );
        }
    }
}
