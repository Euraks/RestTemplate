package org.example.servlet;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.impl.AuthorEntityService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@WebServlet("/AuthorServlet")
public class AuthorServlet extends HttpServlet {

    AuthorEntityService service = new AuthorEntityService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("update".equals(action)) {
            UUID authorId = UUID.fromString(req.getParameter("authorId"));
            AuthorEntity authorEntity = service.findById(authorId);
            req.setAttribute("authorEntity", authorEntity);
            req.getRequestDispatcher("AuthorArticlesForm.jsp").forward(req, resp);
        } else if ("delete".equals(action)) {
            UUID authorId = UUID.fromString(req.getParameter("authorId"));
            service.delete(authorId);
            resp.sendRedirect("/AuthorServlet");
        } else {
            // По умолчанию просто отображаем всех авторов
            List<AuthorEntity> authors = service.findAll();
            req.setAttribute("authors", authors);
            req.getRequestDispatcher("AuthorsandArticles.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter( "action" );

        if ("create".equals( action )) {
            UUID authorId = UUID.randomUUID();
            String authorName = request.getParameter( "authorName" );
            String[] articleTexts = request.getParameterValues( "articleTexts" );
            List<Article> articles = new ArrayList<>();
            for (int i = 0; i < articleTexts.length; i++) {
                articles.add( new Article( UUID.randomUUID(), articleTexts[i] ) );
            }

            AuthorEntity authorEntity = new AuthorEntity( authorId, authorName, articles );

            service.save( authorEntity );

            response.sendRedirect( "/" );
        }
    }
}