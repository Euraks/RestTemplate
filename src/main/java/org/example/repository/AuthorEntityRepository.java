package org.example.repository;

import org.example.model.Article;
import org.example.model.AuthorEntity;

import java.util.List;
import java.util.UUID;

public interface AuthorEntityRepository<T, K> extends Repository<AuthorEntity, UUID> {

    List<Article> findArticlesAll();
}
