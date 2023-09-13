package org.example.servlet.dto.impl;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.SimpleEntityDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class SimpleEntityDTOImpl implements SimpleEntityDTO {

    private List<SimpleEntity> list;

    public SimpleEntityDTOImpl() {
        this.list = Arrays.asList(
                new SimpleEntity( UUID.randomUUID(), "First Entity" ),
                new SimpleEntity( UUID.randomUUID(), "Second Entity" ),
                new SimpleEntity( UUID.randomUUID(), "Third Entity" ) );
    }

    public SimpleEntityDTOImpl(List<SimpleEntity> list) {
        this.list = list;
    }


    @Override
    public SimpleEntity getSimpleEntityForId(UUID uuid) {
        return list.stream().filter( simpleEntity -> simpleEntity.getUuid().equals( uuid ) ).findAny().orElse( null );
    }

    @Override
    public List<SimpleEntity> getAll() {
        return new ArrayList<>(list);
    }
}
