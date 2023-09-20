package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.SimpleEntity;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityOutGoingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityUpdateDTO;
import org.example.servlet.dto.SimpleEntityDTO.mapper.SimpleDtoMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "SimplesId", value = "/simples/*")
public class SimplesId extends HttpServlet {
    ObjectMapper mapper = new ObjectMapper();
    private final SimpleServiceImpl service = new SimpleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                String id = pathParts[1];
                SimpleEntity simpleEntity = service.findById( UUID.fromString( id ) );
                SimpleEntityOutGoingDTO simpleEntityOutGoingDTO = SimpleDtoMapper.INSTANCE.map( simpleEntity );
                String jsonString = mapper.writeValueAsString( simpleEntityOutGoingDTO );
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
                String id = pathParts[1];
                StringBuilder sb = getStringFromRequest( request );

                String json = sb.toString();

                ObjectMapper objectMapper = new ObjectMapper();
                SimpleEntityUpdateDTO simpleEntityUpdateDTO = objectMapper.readValue( json, SimpleEntityUpdateDTO.class );

                SimpleEntity updateSimpleEntity = SimpleDtoMapper.INSTANCE.map( simpleEntityUpdateDTO );
                SimpleEntity newSimpleEntity = service.findById( UUID.fromString( id ) );
                newSimpleEntity.setDescription( updateSimpleEntity.getDescription() );
                try{
                    service.save( newSimpleEntity );
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
                response.getWriter().write( "Deleted SimpleEntity UUID:" + id );
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
