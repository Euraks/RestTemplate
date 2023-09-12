package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.service.SimpleService;
import org.example.servlet.dto.SimpleEntityDTO;

import java.util.List;
import java.util.UUID;

public class SimpleServiceImpl implements SimpleService {
    private final SimpleEntityDTO simpleEntityDto;

    public SimpleServiceImpl(SimpleEntityDTO simpleEntityDto) {
        this.simpleEntityDto = simpleEntityDto;
    }

    @Override
    public SimpleEntity save(SimpleEntity simpleEntity) {
        return null;
    }

    @Override
    public SimpleEntity findById(UUID uuid) {
        simpleEntityDto.getSimpleEntityForId( uuid );
        return null;
    }

    @Override
    public List<SimpleEntity> getAll() {
        return simpleEntityDto.getAll();
    }


}
