package org.example.service.impl;

import org.example.model.BookEntity;
import org.example.repository.Repository;
import org.example.service.Service;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BookServiceImpl implements Service<BookEntity, UUID> {

    private final Repository<BookEntity, UUID> repository;

    public BookServiceImpl(Repository<BookEntity, UUID> repository) {
        this.repository = repository;
    }

    @Override
    public Optional<BookEntity> save(BookEntity bookEntity) {
        try{
            return repository.save( bookEntity );
        } catch(SQLException e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<BookEntity> findById(UUID uuid) {
        return repository.findById( uuid );
    }

    @Override
    public List<BookEntity> findAll() {
        try{
            return repository.findAll();
        } catch(SQLException e){
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public boolean delete(UUID uuid) {
        return repository.deleteById( uuid );
    }

    @Override
    public Repository<BookEntity, UUID> getRepository() {
        return this.repository;
    }
}
