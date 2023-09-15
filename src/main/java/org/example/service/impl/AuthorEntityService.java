package org.example.service.impl;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.impl.AuthorEntityRepositoryImpl;
import org.example.service.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;



public class AuthorEntityService implements Service<AuthorEntity, UUID> {

    private AuthorEntityRepositoryImpl repository = new AuthorEntityRepositoryImpl();

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        return repository.save( authorEntity );
    }

    @Override
    public AuthorEntity findById(UUID uuid) {
        return repository.findById( uuid );
    }


    public Article findArticleById(UUID uuid) {
        return repository.findArticleById( uuid );
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
    public void update(AuthorEntity authorEntity) {
        repository.update( authorEntity );
    }
}
