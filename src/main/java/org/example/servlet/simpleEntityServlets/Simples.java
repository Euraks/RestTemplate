package org.example.servlet.simpleEntityServlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.model.SimpleEntity;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.IncomingSimplyDto;
import org.example.servlet.dto.OutGoingSimplyDto;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityAllOutGoingDTO;
import org.example.servlet.mapper.SimpleDtoMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Simples", value = "/simples")
public class Simples extends HttpServlet {
    ObjectMapper mapper = new ObjectMapper();
    private final SimpleServiceImpl service = new SimpleServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SimpleEntity> simpleEntityList = service.findAll();
        SimpleEntityAllOutGoingDTO simpleEntityAllOutGoingDTO = SimpleDtoMapper.INSTANCE.mapListToDto( simpleEntityList );
        String jsonString = mapper.writeValueAsString(simpleEntityAllOutGoingDTO);
        response.setContentType( "application/json" );
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonString);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest( request );

        String json = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        IncomingSimplyDto incomingSimplyDto = objectMapper.readValue(json, IncomingSimplyDto.class);

        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map(incomingSimplyDto  );
        simpleEntity.setUuid( UUID.randomUUID() );
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
