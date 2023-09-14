package org.example.repository;

import org.example.model.SimpleEntity;

import java.util.List;

public interface SimpleRepository<T, K> {
    T findById(K id);

    boolean deleteById(K id);

    List<T> findAll();

    T save(T t);
}
