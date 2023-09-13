package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.impl.SimpleEntityRepositoryImpl;
import org.example.service.SimpleService;


import java.util.UUID;

public class SimpleServiceImpl implements SimpleService {
    private SimpleEntityRepositoryImpl repository;
    @Override
    public SimpleEntity save(SimpleEntity simpleEntity) {
        repository.save( simpleEntity );
        return simpleEntity;
    }

    @Override
    public SimpleEntity findById(UUID uuid) {
        return null;
    }
}
