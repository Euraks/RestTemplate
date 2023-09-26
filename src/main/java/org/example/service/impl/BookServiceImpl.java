package org.example.service.impl;

import org.example.model.BookEntity;
import org.example.repository.Repository;
import org.example.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookServiceImpl implements Service<BookEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger( BookServiceImpl.class );

    private final Repository<BookEntity, UUID> repository;

    public BookServiceImpl(Repository<BookEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<BookEntity> save(BookEntity bookEntity) {
        try{
            repository.save( bookEntity );
            LOGGER.info( "Saved BookEntity with UUID: {}", bookEntity.getUuid() );
            return Optional.of( bookEntity );
        } catch(SQLException e){
            LOGGER.error( "Failed to save BookEntity", e );
            return Optional.empty();
        }
    }

    @Override
    public Optional<BookEntity> findById(UUID uuid) throws SQLException {
        return repository.findById( uuid );
    }

    @Override
    public List<BookEntity> findAll() {
        try{
            return repository.findAll();
        } catch(SQLException e){
            LOGGER.error( "Failed to find all BookEntities", e );
            return Collections.emptyList();
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        try{
            boolean result = repository.deleteById( uuid );
            if (result) {
                LOGGER.info( "Deleted BookEntity with UUID: {}", uuid );
            } else {
                LOGGER.warn( "BookEntity with UUID: {} not found for deletion", uuid );
            }
            return result;
        } catch(Exception e){
            LOGGER.error( "Failed to delete BookEntity by UUID: {}", uuid, e );
            return false;
        }
    }

    @Override
    public Repository<BookEntity, UUID> getRepository() {
        return this.repository;
    }
}
