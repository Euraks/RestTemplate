package org.example.service;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.SimpleEntityDTO;

import java.util.List;
import java.util.UUID;

public interface SimpleService {

    SimpleEntity save(SimpleEntity simpleEntity);

    SimpleEntity findById(UUID uuid);

    List<SimpleEntity> getAll();
}
