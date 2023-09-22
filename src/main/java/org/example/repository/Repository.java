package org.example.repository;

import org.example.model.Article;

import java.sql.SQLException;
import java.util.List;

public interface Repository<T, K> {
    T findById(K k);

    boolean deleteById(K k);

    List<T> findAll() throws SQLException;

    T save(T t) throws SQLException;

}
