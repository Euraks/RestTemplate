package org.example.service.impl;

import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagServiceImpl implements Service<TagEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger( TagServiceImpl.class );

    private final Repository<TagEntity, UUID> repository;

    public TagServiceImpl(Repository<TagEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TagEntity> save(TagEntity tagEntity) {
        try{
            return repository.save( tagEntity );
        } catch(SQLException e){
            LOGGER.error( "Failed to save TagEntity", e );
            return Optional.empty();
        }
    }

    @Override
    public Optional<TagEntity> findById(UUID uuid) throws SQLException {
        return repository.findById( uuid );
    }

    @Override
    public List<TagEntity> findAll() {
        try{
            return repository.findAll();
        } catch(SQLException e){
            LOGGER.error( "Failed to find all TagEntities", e );
            return Collections.emptyList();
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        try{
            boolean result = repository.deleteById( uuid );
            if (result) {
                LOGGER.info( "Deleted TagEntity with UUID: {}", uuid );
            } else {
                LOGGER.warn( "TagEntity with UUID: {} not found for deletion", uuid );
            }
            return result;
        } catch(Exception e){
            LOGGER.error( "Failed to delete TagEntity by UUID: {}", uuid, e );
            return false;
        }
    }

    @Override
    public Repository<TagEntity, UUID> getRepository() {
        return this.repository;
    }
}
