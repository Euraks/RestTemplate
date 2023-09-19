package org.example.service;

import java.util.List;

public interface SimpleEntityService<T,K> {

    T save(T t);

    T findById(K k);

    List<T> findAll();

    void delete(K k);

    void update(T t);
}
