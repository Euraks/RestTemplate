package org.example.servlet;

import org.example.model.SimpleEntity;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.dto.IncomingSimplyDto;
import org.example.servlet.dto.OutGoingSimplyDto;
import org.example.servlet.mapper.SimpleDtoMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@WebServlet(urlPatterns = "/simple")
public class SimpleServlet extends HttpServlet {

    private final SimpleServiceImpl service = new SimpleServiceImpl();

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
                OutGoingSimplyDto outGoingSimplyDto = SimpleDtoMapper.INSTANCE.map( newEntity );
                req.setAttribute( "action",action );
                req.setAttribute("simpleEntity", outGoingSimplyDto );
                req.getRequestDispatcher("simpleEntityForm.jsp").forward(req, resp);
                break;
            case "update":
                SimpleEntity simpleEntity = service.findById(getUuid( req )  );
                OutGoingSimplyDto updateOutGoingSimplyDto = SimpleDtoMapper.INSTANCE.map( simpleEntity );
                req.setAttribute( "action",action );
                req.setAttribute("simpleEntity", updateOutGoingSimplyDto );
                req.getRequestDispatcher("simpleEntityForm.jsp").forward(req, resp);
                break;
            case "all":
            default:
                List<SimpleEntity> simpleEntityList = service.findAll();
                OutGoingSimplyDto simpleEntityDTO = SimpleDtoMapper.INSTANCE.mapListToDto( simpleEntityList );
                req.setAttribute( "simpleEntityes",simpleEntityDTO );
                req.getRequestDispatcher( "simpleEntityes.jsp" ).forward( req,resp );
                break;
        }
    }

    private UUID getUuid(HttpServletRequest req) {
        String stringUUID = Objects.requireNonNull( req.getParameter("uuid"));
        return UUID.fromString( stringUUID );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        String description = req.getParameter( "description" );
        IncomingSimplyDto incomingSimplyDto = new IncomingSimplyDto();
        incomingSimplyDto.setUuid( getUuid( req ) );
        incomingSimplyDto.setDescription( description );
        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( incomingSimplyDto );

        if (action.equals( "create" )){
            service.save( simpleEntity );
        } else {
            service.update(simpleEntity);
        }
        resp.sendRedirect( "/simple" );
    }
}
