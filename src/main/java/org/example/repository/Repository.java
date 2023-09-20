package org.example.repository;

import org.example.model.SimpleEntity;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T,K> {
    T findById(K k);

    boolean deleteById(K k);

    List<T> findAll();

    T save(T t) throws SQLException;
}
