package org.example.servlet.AuthorServlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.example.db.ConnectionManager;
import org.example.db.HikariCPDataSource;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.Repository;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.AuthorEntityService;
import org.example.service.impl.AuthorEntityServiceImpl;
import org.example.servlet.dto.AuthorEntityDTO.ArticleAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.mapper.ArticleMapper;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "Articles", value = "/articles")
public class Articles extends HttpServlet {

    ObjectMapper mapper = new ObjectMapper();
    private final AuthorEntityRepository<AuthorEntity, UUID> repository = new AuthorEntityRepositoryImpl();
    private final AuthorEntityService service = new AuthorEntityServiceImpl( repository );

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
