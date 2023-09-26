package org.example.service;

import org.example.model.Article;
import org.example.model.AuthorEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface AuthorEntityService extends Service<AuthorEntity, UUID> {

    List<Article> findArticlesAll() throws SQLException;
}
