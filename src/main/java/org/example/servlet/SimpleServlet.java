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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;


@WebServlet(urlPatterns = "/simple")
public class SimpleServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger( Main.class.getName() );

    private SimpleServiceImpl service = new SimpleServiceImpl();



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                UUID uuid = getUuid( req );
                service.delete(uuid);
                resp.sendRedirect( "/simple" );
                break;
            case "create":
                SimpleEntity newEntity = new SimpleEntity();
                req.setAttribute( "action",action );
                req.setAttribute("simpleEntity", newEntity);
                req.getRequestDispatcher("simpleEntityForm.jsp").forward(req, resp);
                break;
            case "update":
                SimpleEntity simpleEntity = service.findById(getUuid( req )  );
                req.setAttribute( "action",action );
                req.setAttribute("simpleEntity", simpleEntity);
                req.getRequestDispatcher("simpleEntityForm.jsp").forward(req, resp);
                break;
            case "all":
            default:
                List<SimpleEntity> simpleEntityList = service.findAll();
                req.setAttribute( "simpleEntityes",simpleEntityList );
                req.getRequestDispatcher( "simpleEntityes.jsp" ).forward( req,resp );
                break;
        }

//        UUID uuid = UUID.randomUUID();// Our Id from request
//        SimpleEntity byId = service.findById(uuid);
//        OutGoingDto outGoingDto = dtomapper.map(byId);
        // return our DTO
    }

    private UUID getUuid(HttpServletRequest req) {
        String stringUUID = Objects.requireNonNull( req.getParameter("uuid"));
        return UUID.fromString( stringUUID );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        String description = req.getParameter( "description" );
        IncomingDto incomingDto = new IncomingDto();
        incomingDto.setUuid( getUuid( req ) );
        incomingDto.setDescription( description );
        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( incomingDto);

        if (action.equals( "create" )){
            service.save( simpleEntity );
        } else {
            service.update(simpleEntity);
        }
        resp.sendRedirect( "/simple" );
    }
}
