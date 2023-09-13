package org.example.servlet;

import org.example.Main;
import org.example.model.SimpleEntity;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.IncomingDto;
import org.example.servlet.dto.OutGoingDto;
import org.example.servlet.mapper.SimpleDtoMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;


@WebServlet(urlPatterns = "/simple")
public class SimpleServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );

    private SimpleServiceImpl service;



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //req.setAttribute( "simpleEntityes",simpleEntityes );
        //req.getRequestDispatcher( "simpleEntityes.jsp" ).forward( req,resp );
//        UUID uuid = UUID.randomUUID();// Our Id from request
//        SimpleEntity byId = service.findById(uuid);
//        OutGoingDto outGoingDto = dtomapper.map(byId);
        // return our DTO
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String description = req.getParameter( "description" );
        IncomingDto incomingDto = new IncomingDto(description);
        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( incomingDto);
        service.save( simpleEntity );
        req.getRequestDispatcher( "/simple" ).forward( req,resp );
    }

}
