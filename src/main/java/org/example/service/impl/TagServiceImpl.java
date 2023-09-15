package org.example.service.impl;

import org.example.model.TagEntity;
import org.example.service.Service;

import java.util.List;
import java.util.UUID;

public class TagServiceImpl implements Service<TagEntity, UUID> {
    @Override
    public TagEntity save(TagEntity tagEntity) {
        return null;
    }

    @Override
    public TagEntity findById(UUID uuid) {
        return null;
    }

    @Override
    public List<TagEntity> findAll() {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public void update(TagEntity tagEntity) {

    }
}
