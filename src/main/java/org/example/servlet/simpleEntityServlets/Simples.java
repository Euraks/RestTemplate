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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Simples", value = "/simples")
public class Simples extends HttpServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger( Simples.class );
    private final ObjectMapper mapper = new ObjectMapper();
    private final Service<SimpleEntity, UUID> service = new SimpleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            List<SimpleEntity> simpleEntityList = service.findAll();
            SimpleEntityAllOutGoingDTO simpleEntityAllOutGoingDTO = SimpleDtoMapper.INSTANCE.mapListToDto( simpleEntityList );
            String jsonString = mapper.writeValueAsString( simpleEntityAllOutGoingDTO );

            response.setContentType( "application/json" );
            response.setCharacterEncoding( "UTF-8" );
            response.getWriter().write( "GetAll SimpleEntity:" + jsonString );
        } catch(SQLException e){
            handleException( response, e, "Failed to fetch all SimpleEntities" );
        } catch(Exception e){
            handleException( response, e, "Failed to process GET request" );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            StringBuilder sb = getStringFromRequest( request );
            String json = sb.toString();

            SimpleEntityIncomingDTO simpleEntityIncomingDTO = mapper.readValue( json, SimpleEntityIncomingDTO.class );

            SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( simpleEntityIncomingDTO );
            service.save( simpleEntity );

            response.setContentType( "text/plain" );
            response.setCharacterEncoding( "UTF-8" );
            response.getWriter().write( "Added SimpleEntity UUID:" + simpleEntity.getUuid() );
        } catch(SQLException e){
            handleException( response, e, "Failed to save the SimpleEntity" );
        } catch(Exception e){
            handleException( response, e, "Failed to process POST request" );
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

    private void handleException(HttpServletResponse response, Exception e, String logMessage) {
        LOGGER.error( logMessage, e );
        response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        try{
            response.getWriter().write( "An internal server error occurred." );
        } catch(IOException ioException){
            LOGGER.error( "Failed to send error response.", ioException );
        }
    }
}
