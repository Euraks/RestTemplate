package org.example.repository;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T, K> extends Serializable {
    Optional<T> findById(K k);

    boolean deleteById(K k);

    List<T> findAll() throws SQLException;

    Optional<T> save(T t) throws SQLException;

    void clearAll();
}
