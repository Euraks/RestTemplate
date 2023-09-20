package org.example.servlet.AuthorServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.AuthorEntity;
import org.example.service.AuthorEntityService;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityIncomingDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.AuthorEntityMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "Authors", value = "/authors")
public class Authors extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final AuthorEntityService service = new AuthorEntityServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<AuthorEntity> authorEntityList = null;
        try{
            authorEntityList = service.findAll();
        } catch(SQLException e){
            e.printStackTrace();
        }
        AuthorEntityAllOutGoingDTO authorEntityAllOutGoingDTO = AuthorEntityMapper.INSTANCE.mapListToDto( authorEntityList );
        String jsonString = mapper.writeValueAsString( authorEntityAllOutGoingDTO );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "GetAll AuthorEntity UUID:" + jsonString );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest( request );

        String json = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        AuthorEntityIncomingDTO authorEntityIncomingDTO = objectMapper.readValue( json, AuthorEntityIncomingDTO.class );

        AuthorEntity authorEntity = AuthorEntityMapper.INSTANCE.map( authorEntityIncomingDTO );
        try{
            service.save( authorEntity );
        } catch(SQLException e){
            e.printStackTrace();
        }
        response.getWriter().write( "Added SimpleEntity UUID:" + authorEntity.getUuid() );
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
