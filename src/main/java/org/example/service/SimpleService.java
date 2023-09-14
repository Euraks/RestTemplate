package org.example.service;

import org.example.model.SimpleEntity;


import java.util.List;
import java.util.UUID;

public interface SimpleService {

    SimpleEntity save(SimpleEntity simpleEntity);

    SimpleEntity findById(UUID uuid);

    List<SimpleEntity> findAll();

    void delete(UUID uuid);

    void update(SimpleEntity simpleEntity);
}
