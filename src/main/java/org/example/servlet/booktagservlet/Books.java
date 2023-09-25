package org.example.servlet.booktagservlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.service.Service;
import org.example.service.impl.BookServiceImpl;
import org.example.servlet.dto.BookTagDTO.BookAllOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.BookIncomingDTO;
import org.example.servlet.dto.BookTagDTO.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Books", value = "/books")
public class Books extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( Books.class );
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final Repository<BookEntity, UUID> bookRepository;
    private final Repository<TagEntity, UUID> tagRepository;
    private Service<BookEntity, UUID> service;

    public Books() {
        this.connectionManager = new HikariCPDataSource();
        this.tagRepository = new TagRepositoryImpl( connectionManager );
        this.bookRepository = new BookRepositoryImpl( connectionManager, tagRepository );
        this.service = new BookServiceImpl( bookRepository );
    }

    public Books(ConnectionManager connectionManager, Repository<TagEntity, UUID> tagRepository) {
        this.connectionManager = connectionManager;
        this.bookRepository = new BookRepositoryImpl( connectionManager, tagRepository );
        this.tagRepository = tagRepository;
        this.service = new BookServiceImpl( bookRepository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try{
            List<BookEntity> bookEntities = service.findAll();
            BookAllOutGoingDTO bookAllOutGoingDTO = BookMapper.INSTANCE.mapListToDto( bookEntities );
            String jsonString = mapper.writeValueAsString( bookAllOutGoingDTO );
            sendSuccessResponse( response, "GetAll BookEntity: " + jsonString, HttpServletResponse.SC_OK );
        } catch(SQLException e){
            handleException( response, e, "Error retrieving all BookEntities" );
        } catch(Exception e){
            handleException( response, e, "Error processing GET request" );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        try{
            String json = readRequestBody( request ).toString();
            BookIncomingDTO bookIncomingDTO = mapper.readValue( json, BookIncomingDTO.class );
            BookEntity bookEntity = BookMapper.INSTANCE.map( bookIncomingDTO );
            service.save( bookEntity );
            sendSuccessResponse( response, "Added BookEntity UUID: " + bookEntity.getUuid(), HttpServletResponse.SC_CREATED );
        } catch(SQLException e){
            handleException( response, e, "Error saving BookEntity" );
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

    protected void setService(Service<BookEntity, UUID> service) {
        this.service = service;
    }
}
