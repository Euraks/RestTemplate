package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.impl.SimpleEntityRepositoryImpl;
import org.example.service.SimpleService;


import java.util.List;
import java.util.UUID;

public class SimpleServiceImpl implements SimpleService {
    private final SimpleEntityRepositoryImpl repository = new SimpleEntityRepositoryImpl();;
    @Override
    public SimpleEntity save(SimpleEntity simpleEntity) {
        repository.save( simpleEntity );
        return simpleEntity;
    }

    @Override
    public SimpleEntity findById(UUID uuid) {
        return null;
    }

    @Override
    public List<SimpleEntity> findAll() {
        return repository.findAll();
    }
}
