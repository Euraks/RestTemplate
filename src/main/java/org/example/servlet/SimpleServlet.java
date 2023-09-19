package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.SimpleEntity;
import org.example.service.impl.SimpleServiceImpl;
import org.example.servlet.mapper.SimpleDtoMapper;


import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


//@WebServlet(name = "SimpleServlet", value = "/simple")
//public class SimpleServlet extends HttpServlet {
//
//    ObjectMapper mapper = new ObjectMapper();
//    private final SimpleServiceImpl service = new SimpleServiceImpl();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String pathInfo = req.getPathInfo();
//
//        switch (pathInfo == null ? "/simples" : pathInfo) {
//            case "delete":
//                UUID uuid = getUuid( req );
//                service.delete(uuid);
//                resp.sendRedirect( "/simple" );
//                break;
//            case "create":
//                SimpleEntity newEntity = new SimpleEntity();
//                OutGoingSimplyDto outGoingSimplyDto = SimpleDtoMapper.INSTANCE.map( newEntity );
//                req.setAttribute( "action",pathInfo );
//                req.setAttribute("simpleEntity", outGoingSimplyDto );
//                req.getRequestDispatcher("simpleEntityForm.jsp").forward(req, resp);
//                break;
//            case "update":
//                SimpleEntity simpleEntity = service.findById(getUuid( req )  );
//                OutGoingSimplyDto updateOutGoingSimplyDto = SimpleDtoMapper.INSTANCE.map( simpleEntity );
//                req.setAttribute( "action",pathInfo );
//                req.setAttribute("simpleEntity", updateOutGoingSimplyDto );
//                req.getRequestDispatcher("simpleEntityForm.jsp").forward(req, resp);
//                break;
//            case "/simples":
//            default:
////                List<SimpleEntity> simpleEntityList = service.findAll();
////                OutGoingSimplyDto simpleEntityDTO = SimpleDtoMapper.INSTANCE.mapListToDto( simpleEntityList );
////                String jsonString = mapper.writeValueAsString(simpleEntityDTO);
////                resp.setContentType( "application/json" );
////                resp.setCharacterEncoding("UTF-8");
////                resp.getWriter().write(jsonString);
////                break;
//        }
//    }
//
//    private UUID getUuid(HttpServletRequest req) {
//        String stringUUID = Objects.requireNonNull( req.getParameter("uuid"));
//        return UUID.fromString( stringUUID );
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
////        String action = req.getParameter("action");
////
////        String description = req.getParameter( "description" );
////        IncomingSimplyDto incomingSimplyDto = new IncomingSimplyDto();
////        incomingSimplyDto.setUuid( getUuid( req ) );
////        incomingSimplyDto.setDescription( description );
////        SimpleEntity simpleEntity = SimpleDtoMapper.INSTANCE.map( incomingSimplyDto );
////
////        if (action.equals( "create" )){
////            service.save( simpleEntity );
////        } else {
////            service.update(simpleEntity);
////        }
////        resp.sendRedirect( "/simple" );
//    }
//}
