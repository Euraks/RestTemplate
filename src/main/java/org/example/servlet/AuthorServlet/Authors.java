package org.example.servlet.AuthorServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.AuthorEntity;
import org.example.service.impl.AuthorEntityService;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.AuthorEntityMapper;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Authors", value = "/authors")
public class Authors extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final AuthorEntityService service = new AuthorEntityService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<AuthorEntity> authorEntityList = service.findAll();
        AuthorEntityAllOutGoingDTO authorEntityAllOutGoingDTO = AuthorEntityMapper.INSTANCE.mapListToDto( authorEntityList );
        String jsonString = mapper.writeValueAsString( authorEntityAllOutGoingDTO );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "GetAll SimpleEntity UUID:" + jsonString );
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
