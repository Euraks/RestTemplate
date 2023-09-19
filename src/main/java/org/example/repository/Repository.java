package org.example.repository;

import org.example.model.SimpleEntity;

import java.util.List;

public interface Repository<T,K> {
    T findById(K k);

    boolean deleteById(K k);

    List<T> findAll();

    T save(T t);

    T update(T t);
}
