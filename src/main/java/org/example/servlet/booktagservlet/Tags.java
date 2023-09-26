package org.example.servlet.booktagservlet;

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
import org.example.servlet.dto.booktagDTO.TagAllOutGoingDTO;
import org.example.servlet.dto.booktagDTO.TagIncomingDTO;
import org.example.servlet.dto.booktagDTO.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@WebServlet(name = "Tags", value = "/tags")
public class Tags extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( Tags.class );
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<TagEntity, UUID> tagRepository;
    private transient Service<TagEntity, UUID> service;

    public Tags() {
        this.connectionManager = new HikariCPDataSource();
        this.tagRepository = new TagRepositoryImpl(connectionManager);
        this.service = new TagServiceImpl( tagRepository );
    }

    public Tags(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.tagRepository = new TagRepositoryImpl(connectionManager);
        this.service = new TagServiceImpl( tagRepository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<TagEntity> tagEntities = service.findAll();
            TagAllOutGoingDTO tagAllOutGoingDTO = TagMapper.INSTANCE.mapListToDto( tagEntities );
            String jsonString = mapper.writeValueAsString( tagAllOutGoingDTO );
            sendSuccessResponse( response, "GetAll TagEntity: " + jsonString, HttpServletResponse.SC_OK );
        } catch(SQLException e){
            handleException( response, e, "Error retrieving all TagEntities" );
        } catch(Exception e){
            handleException( response, e, "Error processing GET request" );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
            String json = readRequestBody( request ).toString();
            TagIncomingDTO tagIncomingDTO = mapper.readValue( json, TagIncomingDTO.class );
            TagEntity tagEntity = TagMapper.INSTANCE.map( tagIncomingDTO );
            service.save( tagEntity );
            sendSuccessResponse( response, "Added TagEntity UUID: " + tagEntity.getUuid(), HttpServletResponse.SC_CREATED );
        } catch(SQLException e){
            handleException( response, e, "Error saving TagEntity" );
        } catch(Exception e){
            handleException( response, e, "Error processing POST request" );
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

    void handleException(HttpServletResponse response, Exception e, String logMessage) {
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

    protected void setService(Service<TagEntity, UUID> service) {
        this.service = service;
    }
}
