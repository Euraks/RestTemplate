package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.model.SimpleEntity;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.IncomingSimplyDto;
import org.example.servlet.dto.OutGoingSimplyDto;
import org.example.servlet.mapper.SimpleDtoMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "SimplesId", value = "/simples/*")
public class SimplesId extends HttpServlet {
    ObjectMapper mapper = new ObjectMapper();
    private final SimpleServiceImpl service = new SimpleServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length > 1) {
                String id = pathParts[1];
                SimpleEntity simpleEntity = service.findById( UUID.fromString( id) );
                OutGoingSimplyDto simpleEntityDTO = SimpleDtoMapper.INSTANCE.map( simpleEntity );
                String jsonString = mapper.writeValueAsString(simpleEntityDTO);
                response.setContentType( "application/json" );
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonString);
                return;
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest( request );

        String json = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        IncomingSimplyDto incomingSimplyDto = objectMapper.readValue(json, IncomingSimplyDto.class);

        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map(incomingSimplyDto  );
        service.save( simpleEntity );

        response.getWriter().write("Received object with UUID: " + simpleEntity.getUuid());
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");


    }

    private StringBuilder getStringFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb;
    }
}
