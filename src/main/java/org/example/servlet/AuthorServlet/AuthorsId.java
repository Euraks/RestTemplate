package org.example.servlet.AuthorServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.model.AuthorEntity;
import org.example.model.SimpleEntity;
import org.example.service.AuthorEntityService;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityUpdateDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.AuthorEntityMapper;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityOutGoingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityUpdateDTO;
import org.example.servlet.dto.SimpleEntityDTO.mapper.SimpleDtoMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "AuthorsId", value = "/authors/*")
public class AuthorsId extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final AuthorEntityService service = new AuthorEntityServiceImpl();



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                String id = pathParts[1];
                AuthorEntity authorEntity = service.findById( UUID.fromString( id ) );
                 AuthorEntityOutGoingDTO authorEntityOutGoingDTO = AuthorEntityMapper.INSTANCE.map( authorEntity);
                String jsonString = mapper.writeValueAsString( authorEntityOutGoingDTO );
                response.setContentType( "application/json" );
                response.setCharacterEncoding( "UTF-8" );
                response.getWriter().write("Get SimpleEntity UUID:"+ jsonString );
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
                UUID authorId = UUID.fromString( pathParts[1]);
                String json = sb.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                AuthorEntityUpdateDTO authorEntityUpdateDTO = objectMapper.readValue( json, AuthorEntityUpdateDTO.class );

                AuthorEntity updateAuthorEntity = AuthorEntityMapper.INSTANCE.map( authorEntityUpdateDTO );
                AuthorEntity newAuthorEntity = service.findById( authorId );
                newAuthorEntity.setAuthorName( updateAuthorEntity.getAuthorName() );
                newAuthorEntity.setArticleList( updateAuthorEntity.getArticleList() );
                try{
                    service.save( newAuthorEntity);
                } catch(SQLException e){
                    e.printStackTrace();
                }
                response.setContentType( "application/json" );
                response.setCharacterEncoding( "UTF-8" );
                response.getWriter().write("Updated SimpleEntity UUID:"+ json );
                return;
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                String id = pathParts[1];
                service.delete( UUID.fromString( id ) );
                response.setContentType( "application/json" );
                response.setCharacterEncoding( "UTF-8" );
                response.getWriter().write("Delete SimpleEntity UUID:"+ id );
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
}
