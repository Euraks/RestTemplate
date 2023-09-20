package org.example.service.impl;

import org.example.model.BookEntity;
import org.example.repository.Repository;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.service.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class BookServiceImpl implements Service<BookEntity, UUID> {

    private final Repository<BookEntity, UUID> repository = new BookRepositoryImpl();

    @Override
    public BookEntity save(BookEntity bookEntity) throws SQLException {
        return repository.save( bookEntity );
    }

    @Override
    public BookEntity findById(UUID uuid) {
        return repository.findById( uuid );
    }

    @Override
    public List<BookEntity> findAll() throws SQLException {
        return repository.findAll();
    }

    @Override
    public boolean delete(UUID uuid) {
        repository.deleteById( uuid );
        return false;
    }
}
