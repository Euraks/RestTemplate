package org.example.service;

import java.sql.SQLException;
import java.util.List;

public interface Service<T,K> {

    T save(T t) throws SQLException;

    T findById(K k);

    List<T> findAll() throws SQLException;

    boolean delete(K k);
}
