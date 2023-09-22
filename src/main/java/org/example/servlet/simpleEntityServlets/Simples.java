package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.repository.impl.SimpleEntityRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.SimpleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Simples", value = "/simples")
public class Simples extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( Simples.class );
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<SimpleEntity, UUID> repository;
    private Service<SimpleEntity, UUID> service;

    public Simples(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new SimpleEntityRepositoryImpl( this.connectionManager );
        this.service = new SimpleServiceImpl( repository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<SimpleEntity> simpleEntityList = service.findAll();
            String jsonString = mapper.writeValueAsString( simpleEntityList );
            sendSuccessResponse( response, "GetAll SimpleEntity:" + jsonString, HttpServletResponse.SC_OK );
        } catch(SQLException e){
            handleException( response, e, "Failed to fetch all SimpleEntities" );
        } catch(Exception e){
            handleException( response, e, "Failed to process GET request" );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
            String json = readRequestBody( request ).toString();
            SimpleEntity simpleEntity = mapper.readValue( json, SimpleEntity.class );
            service.save( simpleEntity );
            sendSuccessResponse( response, "Added SimpleEntity UUID:" + simpleEntity.getUuid(), HttpServletResponse.SC_CREATED );
        } catch(SQLException e){
            handleException( response, e, "Failed to save the SimpleEntity" );
        } catch(Exception e){
            handleException( response, e, "Failed to process POST request" );
        }
    }

    private StringBuilder readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()){
            while ((line = reader.readLine()) != null) {
                sb.append( line );
            }
        }
        return sb;
    }

    private void sendSuccessResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        writeResponse( response, message, statusCode, "application/json" );
    }

    private void handleException(HttpServletResponse response, Exception e, String logMessage) {
        LOGGER.error( logMessage, e );
        try{
            writeResponse( response, "An internal server error occurred.", HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "text/plain" );
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

    protected void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    protected void setService(Service<SimpleEntity, UUID> service) {
        this.service = service;
    }
}
