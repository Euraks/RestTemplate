package org.example.service.impl;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.Repository;
import org.example.service.AuthorEntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class AuthorEntityServiceImpl implements AuthorEntityService {

    private static final Logger LOGGER = LoggerFactory.getLogger( AuthorEntityServiceImpl.class );

    private final AuthorEntityRepository<AuthorEntity, UUID> repository;

    public AuthorEntityServiceImpl(AuthorEntityRepository<AuthorEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<AuthorEntity> save(AuthorEntity authorEntity) {
        try{
            repository.save( authorEntity );
            LOGGER.info( "Saved AuthorEntity with UUID: {}", authorEntity.getUuid() );
            return Optional.of( authorEntity );
        } catch(SQLException e){
            LOGGER.error( "Failed to save AuthorEntity", e );
            return Optional.empty();
        }
    }

    @Override
    public Optional<AuthorEntity> findById(UUID uuid) {
        return repository.findById( uuid );
    }

    @Override
    public List<AuthorEntity> findAll() {
        try{
            return repository.findAll();
        } catch(SQLException e){
            LOGGER.error( "Failed to find all AuthorEntities", e );
            return Collections.emptyList();
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        try{
            boolean result = repository.deleteById( uuid );
            if (result) {
                LOGGER.info( "Deleted AuthorEntity with UUID: {}", uuid );
            } else {
                LOGGER.warn( "AuthorEntity with UUID: {} not found for deletion", uuid );
            }
            return result;
        } catch(Exception e){
            LOGGER.error( "Failed to delete AuthorEntity by UUID: {}", uuid, e );
            return false;
        }
    }

    @Override
    public Repository<AuthorEntity, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public List<Article> findArticlesAll() {
        return repository.findArticlesAll();
    }
}
