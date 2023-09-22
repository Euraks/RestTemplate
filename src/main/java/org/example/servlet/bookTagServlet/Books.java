package org.example.servlet.bookTagServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.BookEntity;
import org.example.service.Service;
import org.example.service.impl.BookServiceImpl;
import org.example.servlet.dto.BookTagDTO.BookAllOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.BookIncomingDTO;
import org.example.servlet.dto.BookTagDTO.mapper.BookMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Books", value = "/books")
public class Books extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final Service<BookEntity, UUID> service = new BookServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<BookEntity> bookEntities = null;
        try{
            bookEntities = service.findAll();
        } catch(SQLException e){
            e.printStackTrace();
        }
        BookAllOutGoingDTO bookAllIncomingDTO = BookMapper.INSTANCE.mapListToDto( bookEntities );
        String jsonString = mapper.writeValueAsString( bookAllIncomingDTO );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "GetAll BookEntity UUID:" + jsonString );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest( request );

        String json = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        BookIncomingDTO bookIncomingDTO = objectMapper.readValue( json, BookIncomingDTO.class );

        BookEntity bookEntity = BookMapper.INSTANCE.map( bookIncomingDTO );
        try{
            service.save( bookEntity );
        } catch(SQLException e){
            e.printStackTrace();
        }
        response.getWriter().write( "Added BookEntity UUID:" + bookEntity.getUuid() );
        response.setContentType( "text/plain" );
        response.setCharacterEncoding( "UTF-8" );
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
}
