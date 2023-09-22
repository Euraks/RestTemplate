package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SimpleServiceImpl implements Service<SimpleEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServiceImpl.class);

    private final Repository<SimpleEntity, UUID> repository;

    public SimpleServiceImpl(Repository<SimpleEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public SimpleEntity save(SimpleEntity simpleEntity) throws SQLException {
        try {
            repository.save(simpleEntity);
            LOGGER.info("Saved SimpleEntity with UUID: {}", simpleEntity.getUuid());
            return simpleEntity;
        } catch (SQLException e) {
            LOGGER.error("Failed to save SimpleEntity", e);
            throw e;
        }
    }

    @Override
    public SimpleEntity findById(UUID uuid) {
        try {
            return repository.findById(uuid);
        } catch (Exception e) {
            LOGGER.error("Failed to find SimpleEntity by UUID: {}", uuid, e);
            throw e;
        }
    }

    @Override
    public List<SimpleEntity> findAll() throws SQLException {
        try {
            return repository.findAll();
        } catch (SQLException e) {
            LOGGER.error("Failed to find all SimpleEntities", e);
            throw e;
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        try {
            boolean result = repository.deleteById(uuid);
            if (result) {
                LOGGER.info("Deleted SimpleEntity with UUID: {}", uuid);
            } else {
                LOGGER.warn("SimpleEntity with UUID: {} not found for deletion", uuid);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("Failed to delete SimpleEntity by UUID: {}", uuid, e);
            throw e;
        }
    }

    @Override
    public Repository<SimpleEntity, UUID> getRepository() {
        return this.repository;
    }
}
