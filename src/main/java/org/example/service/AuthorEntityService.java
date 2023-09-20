package org.example.service;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.servlet.AuthorServlet.Articles;

import java.util.List;
import java.util.UUID;

public interface AuthorEntityService<T,K> {
    T save(T t);

    T findById(K k);

    List<T> findAll();

    void delete(K k);

    void update(T t);

    Article findArticleById(UUID uuid);

    void deleteArticleById(UUID uuid);

    Article getNewArticle();

    List<Article> findArticlesAll();
}
