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
import org.example.servlet.dto.BookTagDTO.BookOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.BookUpdateDTO;
import org.example.servlet.dto.BookTagDTO.mapper.BookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;


@WebServlet(name = "BookId", value = "/books/*")
public class BookId extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( BookId.class );

    ObjectMapper mapper = new ObjectMapper();
    private final Service<BookEntity, UUID> service = new BookServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                String id = pathParts[1];
                BookEntity bookEntity = service.findById( UUID.fromString( id ) );
                BookOutGoingDTO bookOutGoingDTO = BookMapper.INSTANCE.map( bookEntity );
                String jsonString = mapper.writeValueAsString( bookOutGoingDTO );
                response.setContentType( "application/json" );
                response.setCharacterEncoding( "UTF-8" );
                response.getWriter().write( "Get BookEntity UUID:" + jsonString );
                return;
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                StringBuilder sb = getStringFromRequest( request );
                UUID authorId = UUID.fromString( pathParts[1] );
                String json = sb.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                BookUpdateDTO bookUpdateDTO = objectMapper.readValue( json, BookUpdateDTO.class );

                BookEntity updateBookEntity = BookMapper.INSTANCE.map( bookUpdateDTO );
                BookEntity newBookEntity = service.findById( authorId );
                newBookEntity.setBookText( updateBookEntity.getBookText() );
                newBookEntity.setTagEntities( updateBookEntity.getTagEntities() );
                try{
                    service.save( newBookEntity );
                } catch(SQLException e){
                    e.printStackTrace();
                }
                response.setContentType( "application/json" );
                response.setCharacterEncoding( "UTF-8" );
                response.getWriter().write( "Updated BookEntity UUID:" + json );
                return;
            }
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        setResponseDefaults(response);

        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                String id = pathParts[1];
                if (service.delete(UUID.fromString(id))) {
                    response.getWriter().write("Delete BookEntity UUID:" + id);

                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("BookEntity with UUID:" + id + " not found");

                }
                return;
            }
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("ID is required for deletion");

    }

    private void setResponseDefaults(HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

}
