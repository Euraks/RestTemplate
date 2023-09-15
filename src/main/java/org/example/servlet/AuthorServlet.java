package org.example.servlet;

import org.example.repository.AuthorEntityRepository;
import org.example.service.impl.AuthorEntityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/authors")
public class AuthorServlet extends HttpServlet {
    private AuthorEntityService service = new AuthorEntityService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet( req, resp );
    }
}
