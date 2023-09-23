package org.example.service.impl;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.Repository;
import org.example.service.AuthorEntityService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
//
//
//public class AuthorEntityServiceImpl implements AuthorEntityService {
//
//    private final AuthorEntityRepository<AuthorEntity, UUID> repository;
//
//    public AuthorEntityServiceImpl(AuthorEntityRepository<AuthorEntity, UUID> repository) {
//        this.repository = repository;
//    }
//
//    @Override
//    public Optional<AuthorEntity> save(AuthorEntity authorEntity) throws SQLException {
//        return repository.save( authorEntity );
//    }
//
//    @Override
//    public Optional<AuthorEntity> findById(UUID uuid) {
//        return repository.findById( uuid );
//    }
//
//
//    @Override
//    public List<AuthorEntity> findAll() throws SQLException {
//        return repository.findAll();
//    }
//
//    @Override
//    public boolean delete(UUID uuid) {
//        repository.deleteById( uuid );
//        return false;
//    }
//
//    @Override
//    public Repository<AuthorEntity,UUID> getRepository() {
//        return this.repository;
//    }
//
//    @Override
//    public List<Article> findArticlesAll() {
//        return repository.findArticlesAll();
//    }
//}
