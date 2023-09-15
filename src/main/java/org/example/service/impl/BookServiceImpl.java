package org.example.service.impl;

import org.example.model.BookEntity;
import org.example.service.Service;

import java.util.List;
import java.util.UUID;

public class BookServiceImpl implements Service<BookEntity, UUID> {
    @Override
    public BookEntity save(BookEntity bookEntity) {
        return null;
    }

    @Override
    public BookEntity findById(UUID uuid) {
        return null;
    }

    @Override
    public List<BookEntity> findAll() {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public void update(BookEntity bookEntity) {

    }
}
