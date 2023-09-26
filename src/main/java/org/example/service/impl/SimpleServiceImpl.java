package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.service.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SimpleServiceImpl implements Service<SimpleEntity, UUID> {

    private final Repository<SimpleEntity, UUID> repository;

    public SimpleServiceImpl(Repository<SimpleEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<SimpleEntity> save(SimpleEntity simpleEntity) throws SQLException {
        repository.save(simpleEntity);
        return Optional.of(simpleEntity);
    }

    @Override
    public Optional<SimpleEntity> findById(UUID uuid) throws SQLException {
        return repository.findById(uuid);
    }

    @Override
    public List<SimpleEntity> findAll() throws SQLException {
        return repository.findAll();
    }

    @Override
    public boolean delete(UUID uuid) throws SQLException {
        return repository.deleteById(uuid);
    }

    @Override
    public Repository<SimpleEntity, UUID> getRepository() {
        return this.repository;
    }
}
