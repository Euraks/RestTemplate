package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.SimpleEntity;
import org.example.service.Service;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityAllOutGoingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityIncomingDTO;
import org.example.servlet.dto.SimpleEntityDTO.mapper.SimpleDtoMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Simples", value = "/simples")
public class Simples extends HttpServlet {
    ObjectMapper mapper = new ObjectMapper();
    private final Service<SimpleEntity, UUID> service = new SimpleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SimpleEntity> simpleEntityList = null;
        try{
            simpleEntityList = service.findAll();
        } catch(SQLException e){
            e.printStackTrace();
        }
        SimpleEntityAllOutGoingDTO simpleEntityAllOutGoingDTO = SimpleDtoMapper.INSTANCE.mapListToDto( simpleEntityList );
        String jsonString = mapper.writeValueAsString( simpleEntityAllOutGoingDTO );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "GetAll SimpleEntity UUID:" + jsonString );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest( request );

        String json = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleEntityIncomingDTO simpleEntityIncomingDTO = objectMapper.readValue( json, SimpleEntityIncomingDTO.class );

        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( simpleEntityIncomingDTO );
        try{
            service.save( simpleEntity );
        } catch(SQLException e){
            e.printStackTrace();
        }
        response.getWriter().write( "Added SimpleEntity UUID:" + simpleEntity.getUuid() );
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
