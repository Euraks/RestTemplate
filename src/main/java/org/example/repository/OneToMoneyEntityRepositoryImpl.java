package org.example.repository;

import org.example.model.OneToManyEntity;
import org.example.model.SimpleEntity;

import java.util.List;
import java.util.UUID;

public class OneToMoneyEntityRepositoryImpl<T, K> implements OneToMoneyEntityRepository<OneToManyEntity, UUID> {
    @Override
    public OneToManyEntity findById(UUID id) {
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public List<OneToManyEntity> findAll() {
        return null;
    }

    @Override
    public OneToManyEntity save(OneToManyEntity oneToManyEntity) {
        return null;
    }

    @Override
    public OneToManyEntity update(SimpleEntity simpleEntity) {
        return null;
    }
}
