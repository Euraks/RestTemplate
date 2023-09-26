package org.example.service.impl;

import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.service.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TagServiceImpl implements Service<TagEntity, UUID> {

    private final Repository<TagEntity, UUID> repository;

    public TagServiceImpl(Repository<TagEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TagEntity> save(TagEntity tagEntity) throws SQLException {
        return repository.save( tagEntity );
    }

    @Override
    public Optional<TagEntity> findById(UUID uuid) throws SQLException {
        return repository.findById( uuid );
    }

    @Override
    public List<TagEntity> findAll() throws SQLException {
        return repository.findAll();
    }

    @Override
    public boolean delete(UUID uuid) throws SQLException {
        return repository.deleteById( uuid );
    }

    @Override
    public Repository<TagEntity, UUID> getRepository() {
        return this.repository;
    }
}
