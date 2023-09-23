package org.example.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {
    Optional<T> findById(K k);

    boolean deleteById(K k);

    List<T> findAll() throws SQLException;

    Optional<T> save(T t) throws SQLException;

    void clearAll();
}
