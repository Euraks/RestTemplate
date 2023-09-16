package org.example.repository;

import java.util.List;
import java.util.UUID;

public interface AuthorEntityRepository<T,K> {
    T findById(K id);

    boolean deleteById(K id);

    List<T> findAll();

    T save(T t);

    T update(T t);

    boolean deleteArticleById(UUID articleId);
}
