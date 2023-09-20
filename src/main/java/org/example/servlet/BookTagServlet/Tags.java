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
import org.example.servlet.dto.BookTagDTO.TagAllOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.TagIncomingDTO;
import org.example.servlet.dto.BookTagDTO.mapper.TagMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Tags", value = "/tags")
public class Tags extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final Service<TagEntity, UUID> service = new TagServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TagEntity> tagEntities = null;
        try{
            tagEntities = service.findAll();
        } catch(SQLException e){
            e.printStackTrace();
        }
        TagAllOutGoingDTO tagAllIncomingDTO = TagMapper.INSTANCE.mapListToDto( tagEntities );
        String jsonString = mapper.writeValueAsString( tagAllIncomingDTO );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "GetAll TagEntity UUID:" + jsonString );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest( request );

        String json = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        TagIncomingDTO tagIncomingDTO = objectMapper.readValue( json, TagIncomingDTO.class );

        TagEntity tagEntity = TagMapper.INSTANCE.map( tagIncomingDTO );
        try{
            service.save( tagEntity );
        } catch(SQLException e){
            e.printStackTrace();
        }
        response.getWriter().write( "Added BookEntity UUID:" + tagEntity.getUuid() );
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
