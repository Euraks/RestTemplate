package org.example.service.impl;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.AuthorEntityService;
import org.example.servlet.AuthorServlet.Articles;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


public class AuthorEntityServiceImpl implements AuthorEntityService {

    private AuthorEntityRepository<AuthorEntity, UUID> repository = new AuthorEntityRepositoryImpl();

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) throws SQLException {
        return repository.save( authorEntity );
    }

    @Override
    public AuthorEntity findById(UUID uuid) {
        return repository.findById( uuid );
    }


    @Override
    public List<AuthorEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById( uuid );
    }

    @Override
    public List<Article> findArticlesAll() {
        return repository.findArticlesAll();
    }
}
