package org.example.servlet.bookTagServlet;

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
import org.example.servlet.dto.BookTagDTO.TagOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.TagUpdateDTO;
import org.example.servlet.dto.BookTagDTO.mapper.TagMapper;
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
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<TagEntity, UUID> repository;
    private Service<TagEntity, UUID> service;

    public TagsId() {
        this.connectionManager = new HikariCPDataSource();
        this.repository = new TagRepositoryImpl( this.connectionManager );
        this.service = new TagServiceImpl( repository );
    }

    public TagsId(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new TagRepositoryImpl( this.connectionManager );
        this.service = new TagServiceImpl( repository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String pathInfo = request.getPathInfo();

            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID tagUUID = UUID.fromString( pathParts[pathParts.length - 1] );
                    Optional<TagEntity> optionalTag = service.findById( tagUUID );
                    if (optionalTag.isPresent()) {
                        TagOutGoingDTO tagOutGoingDTO = TagMapper.INSTANCE.map( optionalTag.get() );
                        String jsonString = mapper.writeValueAsString( tagOutGoingDTO );

                        sendSuccessResponse( response, "Get TagEntity UUID: " + jsonString, HttpServletResponse.SC_OK );
                    } else {
                        sendErrorResponse( response, "TagEntity not found", HttpServletResponse.SC_NOT_FOUND );
                    }
                } else {
                    sendErrorResponse( response, "ID is required", HttpServletResponse.SC_BAD_REQUEST );
                }
            } else {
                sendErrorResponse( response, "ID is required", HttpServletResponse.SC_BAD_REQUEST );
            }
        } catch(IllegalArgumentException e){
            sendErrorResponse( response, "Invalid UUID format", HttpServletResponse.SC_BAD_REQUEST );
        } catch(IOException e){
            handleException( response, e, "Error processing GET request" );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String pathInfo = request.getPathInfo();
            setResponseDefaults( response );

            if (pathInfo == null || pathInfo.isEmpty()) {
                sendErrorResponse( response, "ID is required", HttpServletResponse.SC_BAD_REQUEST );
                return;
            }

            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length <= 1) {
                sendErrorResponse( response, "ID is required", HttpServletResponse.SC_BAD_REQUEST );
                return;
            }

            StringBuilder sb = readRequestBody( request );
            UUID tagId = UUID.fromString( pathParts[1] );
            String json = sb.toString();

            TagUpdateDTO tagUpdateDTO = mapper.readValue( json, TagUpdateDTO.class );
            TagEntity updateTagEntity = TagMapper.INSTANCE.map( tagUpdateDTO );
            Optional<TagEntity> optNewTagEntity = service.findById( tagId );

            if (!optNewTagEntity.isPresent()) {
                sendErrorResponse( response, "TagEntity not found", HttpServletResponse.SC_NOT_FOUND );
                return;
            }

            TagEntity newTagEntity = optNewTagEntity.get();
            newTagEntity.setTagName( updateTagEntity.getTagName() );
            newTagEntity.setBookEntities( updateTagEntity.getBookEntities() );

            service.save( newTagEntity );
            sendSuccessResponse( response, "Updated TagEntity UUID: " + json, HttpServletResponse.SC_OK );
        } catch(IllegalArgumentException e){
            sendErrorResponse( response, "Invalid UUID format", HttpServletResponse.SC_BAD_REQUEST );
        } catch(IOException e){
            handleException( response, e, "Error processing PUT request" );
        } catch(SQLException e){
            handleException( response, e, "Error while updating TagEntity" );
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String pathInfo = request.getPathInfo();
            setResponseDefaults( response );

            if (pathInfo != null && !pathInfo.isEmpty()) {
                String[] pathParts = pathInfo.split( "/" );
                if (pathParts.length > 1) {
                    UUID tagUUID = UUID.fromString( pathParts[pathParts.length - 1] );
                    if (service.delete( tagUUID )) {
                        sendSuccessResponse( response, "Delete TagEntity UUID: " + tagUUID, HttpServletResponse.SC_OK );
                        LOGGER.info( "Successfully deleted TagEntity with UUID: {}", tagUUID );
                    } else {
                        sendErrorResponse( response, "TagEntity with UUID: " + tagUUID + " not found", HttpServletResponse.SC_NOT_FOUND );
                        LOGGER.warn( "TagEntity with UUID: {} not found", tagUUID );
                    }
                    return;
                }
            }

            sendErrorResponse( response, "ID is required for deletion", HttpServletResponse.SC_BAD_REQUEST );
            LOGGER.warn( "ID is required for deletion" );
        } catch(IllegalArgumentException e){
            sendErrorResponse( response, "Invalid UUID format", HttpServletResponse.SC_BAD_REQUEST );
        } catch(IOException e){
            handleException( response, e, "Error while deleting TagEntity" );
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

    private void sendErrorResponse(HttpServletResponse response, String message, int statusCode) throws IOException {
        writeResponse( response, message, statusCode, "text/plain" );
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

    private void setResponseDefaults(HttpServletResponse response) {
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
    }
}
