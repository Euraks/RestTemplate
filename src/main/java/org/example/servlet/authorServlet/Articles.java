package org.example.servlet.authorServlet;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.AuthorEntityService;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.servlet.dto.AuthorEntityDTO.ArticleAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.ArticleMapper;
import org.example.servlet.simpleentityservlets.Simples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Articles", value = "/articles")
public class Articles extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger( Simples.class );
    private ObjectMapper mapper = new ObjectMapper();

    private final ConnectionManager connectionManager;
    private final AuthorEntityRepository<AuthorEntity, UUID> repository;
    private AuthorEntityService service;

    public Articles() {
        this.connectionManager = new HikariCPDataSource();
        this.repository = new AuthorEntityRepositoryImpl( this.connectionManager );
        this.service = new AuthorEntityServiceImpl( repository );
    }


    public Articles(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        this.repository = new AuthorEntityRepositoryImpl( this.connectionManager );
        this.service = new AuthorEntityServiceImpl( repository );
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Article> articleList = service.findArticlesAll();
        ArticleAllOutGoingDTO articleAllOutGoingDTO = ArticleMapper.INSTANCE.mapListToDto( articleList );
        String jsonString = mapper.writeValueAsString( articleAllOutGoingDTO );
        response.setContentType( "application/json" );
        response.setCharacterEncoding( "UTF-8" );
        response.getWriter().write( "GetAll SimpleEntity UUID:" + jsonString );
    }
}
