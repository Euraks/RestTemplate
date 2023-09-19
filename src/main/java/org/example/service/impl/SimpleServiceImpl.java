package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.example.repository.impl.SimpleEntityRepositoryImpl;
import org.example.service.SimpleEntityService;

import java.util.List;
import java.util.UUID;

public class SimpleServiceImpl implements SimpleEntityService<SimpleEntity, UUID> {
    private final Repository<SimpleEntity, UUID> repository = new SimpleEntityRepositoryImpl();
    ;

    @Override
    public SimpleEntity save(SimpleEntity simpleEntity) {
        repository.save( simpleEntity );
        return simpleEntity;
    }

    @Override
    public SimpleEntity findById(UUID uuid) {
        return repository.findById( uuid );
    }

    @Override
    public List<SimpleEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(UUID uuid) {
        repository.deleteById( uuid );
    }
}
