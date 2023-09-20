package org.example.service.impl;

import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.repository.impl.TagRepositoryImpl;
import org.example.service.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class TagServiceImpl implements Service<TagEntity, UUID> {
    private final Repository<TagEntity, UUID> repository = new TagRepositoryImpl();
    private final Repository<BookEntity, UUID> bookRepository = new BookRepositoryImpl();

    @Override
    public TagEntity save(TagEntity tagEntity) throws SQLException {
        return repository.save( tagEntity );
    }

    @Override
    public TagEntity findById(UUID uuid) {
        return repository.findById( uuid );
    }

    @Override
    public List<TagEntity> findAll() throws SQLException {
        return repository.findAll();
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById( uuid );
        return false;
    }
}
