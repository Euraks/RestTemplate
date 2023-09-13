package org.example.servlet;

import org.example.model.SimpleEntity;
import org.example.service.SimpleService;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.IncomingDto;
import org.example.servlet.dto.OutGoingDto;
import org.example.servlet.dto.impl.SimpleEntityDTOImpl;
import org.example.servlet.mapper.SimpleDtomapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


@WebServlet(urlPatterns = "/simple")
public class SimpleServlet extends HttpServlet {
    private SimpleEntityDTOImpl simpleEntityDTO = new SimpleEntityDTOImpl();
    private SimpleServiceImpl service;
    private SimpleDtomapper dtomapper;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<SimpleEntity> simpleEntityes = simpleEntityDTO.getAll();
        req.setAttribute( "simpleEntityes",simpleEntityes );
        req.getRequestDispatcher( "simpleEntityes.jsp" ).forward( req,resp );
//        UUID uuid = UUID.randomUUID();// Our Id from request
//        SimpleEntity byId = service.findById(uuid);
//        OutGoingDto outGoingDto = dtomapper.map(byId);
        // return our DTO
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SimpleEntity simpleEntity = dtomapper.map(new IncomingDto());
        SimpleEntity saved = service.save(simpleEntity);
        OutGoingDto map = dtomapper.map(saved);
        // return our DTO, not necessary
    }

}
