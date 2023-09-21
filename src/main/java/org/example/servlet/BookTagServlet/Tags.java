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
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "Tags", value = "/tags")
public class Tags extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(Tags.class.getName());

    private final ObjectMapper mapper = new ObjectMapper();
    private final Service<TagEntity, UUID> service = new TagServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TagEntity> tagEntities;
        try {
            tagEntities = service.findAll();
            TagAllOutGoingDTO tagAllIncomingDTO = TagMapper.INSTANCE.mapListToDto(tagEntities);
            String jsonString = mapper.writeValueAsString(tagAllIncomingDTO);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("GetAll TagEntity UUID:" + jsonString);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all TagEntities", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = getStringFromRequest(request);
        String json = sb.toString();

        try {
            TagIncomingDTO tagIncomingDTO = mapper.readValue(json, TagIncomingDTO.class);
            TagEntity tagEntity = TagMapper.INSTANCE.map(tagIncomingDTO);
            service.save(tagEntity);
            response.getWriter().write("Added BookEntity UUID:" + tagEntity.getUuid());
            setResponseDefaults(response, "text/plain");
        } catch (SQLException e) {
            LOGGER.log( Level.SEVERE, "Error saving TagEntity", e);
            throw new ServletException(e);
        }
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

    private void setResponseDefaults(HttpServletResponse response, String contentType) {
        response.setContentType(contentType);
        response.setCharacterEncoding("UTF-8");
    }
}
