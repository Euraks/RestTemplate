package org.example.servlet.BookTagServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.BookEntity;
import org.example.service.Service;
import org.example.service.impl.BookServiceImpl;
import org.example.servlet.dto.BookTagDTO.BookOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.mapper.BookMapper;

import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "BookId", value = "/books/*")
public class BookId extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final Service<BookEntity, UUID> service = new BookServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.isEmpty()) {
            String[] pathParts = pathInfo.split( "/" );
            if (pathParts.length > 1) {
                String id = pathParts[1];
                BookEntity bookEntity = service.findById( UUID.fromString( id ) );
                BookOutGoingDTO bookOutGoingDTO = BookMapper.INSTANCE.map( bookEntity);
                String jsonString = mapper.writeValueAsString( bookOutGoingDTO );
                response.setContentType( "application/json" );
                response.setCharacterEncoding( "UTF-8" );
                response.getWriter().write("Get BookEntity UUID:"+ jsonString );
                return;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
