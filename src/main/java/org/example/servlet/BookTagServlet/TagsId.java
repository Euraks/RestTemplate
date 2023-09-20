package org.example.servlet.BookTagServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.TagEntity;
import org.example.service.Service;
import org.example.service.impl.TagServiceImpl;
import org.example.servlet.dto.BookTagDTO.TagOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.TagUpdateDTO;
import org.example.servlet.dto.BookTagDTO.mapper.TagMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "TagsId", value = "/tags/*")
public class TagsId extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final Service<TagEntity, UUID> service = new TagServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                String id = pathParts[1];
                try{
                    TagEntity tagEntity = service.findById( UUID.fromString( id ) );
                    if (tagEntity != null) {
                        TagOutGoingDTO tagOutGoingDTO = TagMapper.INSTANCE.map( tagEntity );
                        String jsonString = mapper.writeValueAsString( tagOutGoingDTO );

                        response.setContentType( "application/json" );
                        response.setCharacterEncoding( "UTF-8" );
                        response.getWriter().write( "Get TagEntity UUID:" + jsonString );
                    } else {
                        response.setStatus( HttpServletResponse.SC_NOT_FOUND );
                        response.getWriter().write( "TagEntity not found" );
                    }
                } catch(IllegalArgumentException e){
                    response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
                    response.getWriter().write( "Invalid UUID format" );
                }
            } else {
                response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
                response.getWriter().write( "ID is required" );
            }
        } else {
            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
            response.getWriter().write( "ID is required" );
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                StringBuilder sb = getStringFromRequest( request );
                UUID tagId = UUID.fromString( pathParts[1] );
                String json = sb.toString();


                TagUpdateDTO tagUpdateDTO = mapper.readValue( json, TagUpdateDTO.class );

                TagEntity updateTagEntity = TagMapper.INSTANCE.map( tagUpdateDTO );
                TagEntity newTagEntity = service.findById( tagId );
                if (newTagEntity != null) {
                    newTagEntity.setTagName( updateTagEntity.getTagName() );
                    newTagEntity.setBookEntities( updateTagEntity.getBookEntities() );
                    try{
                        service.save( newTagEntity );
                    } catch(SQLException e){
                        e.printStackTrace();
                    }
                    response.setContentType( "application/json" );
                    response.setCharacterEncoding( "UTF-8" );
                    response.getWriter().write( "Updated TagEntity UUID:" + json );
                } else {
                    response.setStatus( HttpServletResponse.SC_NOT_FOUND );
                    response.getWriter().write( "TagEntity not found" );
                }
            } else {
                response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
                response.getWriter().write( "ID is required" );
            }
        } else {
            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
            response.getWriter().write( "ID is required" );
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                String id = pathParts[1];
                service.delete(UUID.fromString(id));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Delete TagEntity UUID:" + id);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("ID is required for deletion");
    }



}
