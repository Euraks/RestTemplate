//package org.example.servlet.AuthorServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.service.AuthorEntityService;
import org.example.service.impl.AuthorEntityServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
//
//@WebServlet("/AuthorServlet")
//public class AuthorServlet extends HttpServlet {
//
//    AuthorEntityService<AuthorEntity,UUID> service = new AuthorEntityServiceImpl();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter( "action" );
//
//        if ("update".equals( action )) {
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            AuthorEntity authorEntity = service.findById( authorId );
//            req.setAttribute( "author", authorEntity );
//            req.setAttribute( "action", "update" );
//            req.getRequestDispatcher( "AuthorForm.jsp" ).forward( req, resp ); // addArticle
//        } else if ("updateArticle".equals( action )) {
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            AuthorEntity authorEntity = service.findById( authorId );
//            req.setAttribute( "author", authorEntity );
//            UUID articleId = UUID.fromString( req.getParameter( "articleId" ) );
//            Article article = service.findArticleById( articleId );
//            req.setAttribute( "article", article );
//            req.setAttribute( "action", "updateArticle" );
//            req.getRequestDispatcher( "AuthorArticlesForm.jsp" ).forward( req, resp );
//        } else if ("addArticle".equals( action )) {
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            AuthorEntity authorEntity = service.findById( authorId );
//            req.setAttribute( "author", authorEntity );
//            req.setAttribute( "action", "addArticle" );
//            Article article = service.getNewArticle();
//            req.setAttribute( "article", article );
//            req.getRequestDispatcher( "AuthorArticlesForm.jsp" ).forward( req, resp );
//        } else if ("delete".equals( action )) {
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            service.delete( authorId );
//            resp.sendRedirect( "/AuthorServlet" );
//        } else if ("deleteArticle".equals( action )) {
//            UUID articleId = UUID.fromString( req.getParameter( "articleId" ) );
//            service.deleteArticleById( articleId );
//            resp.sendRedirect( "/AuthorServlet" );
//        } else {
//            List<AuthorEntity> authors = service.findAll();
//            req.setAttribute( "authors", authors );
//            req.getRequestDispatcher( "AuthorsArticles.jsp" ).forward( req, resp );
//        }
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String action = req.getParameter( "action" );
//
//        if ("addArticle".equals( action )) { // Нужно добавить DTO
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            AuthorEntity authorEntity = service.findById( authorId );
//            String articleTexts = req.getParameter( "articleTexts" );
//            List<Article> articles = authorEntity.getArticleList();
//            articles.add( new Article( UUID.randomUUID(), articleTexts ) );
//            authorEntity.setArticleList( articles );
//            service.save( authorEntity );
//            resp.sendRedirect( "/AuthorServlet" );
//        } else if ("updateAuthor".equals( action )) { // Нужно добавить DTO
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            AuthorEntity authorEntity = service.findById( authorId );
//            String authorName = req.getParameter( "authorName" );
//            authorEntity.setAuthorName( authorName );
//            service.save( authorEntity );
//            resp.sendRedirect( "/AuthorServlet" );
//        } else if ("updateArticle".equals( action )) { // Нужно добавить DTO
//            UUID authorId = UUID.fromString( req.getParameter( "authorId" ) );
//            AuthorEntity authorEntity = service.findById( authorId );
//            String authorName = req.getParameter( "authorName" );
//            authorEntity.setAuthorName( authorName );
//
//            UUID articleId = UUID.fromString( req.getParameter( "articleIds" ) );
//            Article article = service.findArticleById( articleId );
//            String articleTexts = req.getParameter( "articleTexts" );
//            article.setText( articleTexts );
//            authorEntity.getArticleList().add( article );
//            service.save( authorEntity );
//            resp.sendRedirect( "/AuthorServlet" );
//        } else { // Нужно добавить DTO
//            UUID authorId = UUID.randomUUID();
//            String authorName = req.getParameter( "authorName" );
//            String articleTexts = req.getParameter( "articleTexts" );
//            List<Article> articles = new ArrayList<>();
//            articles.add( new Article( UUID.randomUUID(), articleTexts ) );
//
//            AuthorEntity authorEntity = new AuthorEntity( authorId, authorName, articles );
//
//            service.save( authorEntity );
//
//            resp.sendRedirect( "/AuthorServlet" );
//
//        }
//    }
//}