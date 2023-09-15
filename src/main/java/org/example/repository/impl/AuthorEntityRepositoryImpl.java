package org.example.repository.impl;

import org.example.db.HikariCPDataSource;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AuthorEntityRepositoryImpl<T, K> implements AuthorEntityRepository<AuthorEntity, UUID> {

    private final HikariCPDataSource connectionManager = new HikariCPDataSource();

    @Override
    public AuthorEntity findById(UUID id) {
        return null;
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
    }

    @Override
    public List<AuthorEntity> findAll() {
        return null;
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        // Сохраняем сначала AuthorEntity
        String sqlAuthor = "INSERT INTO AuthorEntity (id, authorName) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement psAuthor = connection.prepareStatement(sqlAuthor)) {

            connection.setAutoCommit(false); // Начинаем транзакцию

            psAuthor.setObject(1, authorEntity.getUuid());
            psAuthor.setString(2, authorEntity.getAuthorName());
            psAuthor.executeUpdate();

            // Теперь сохраняем связанные Article
            String sqlArticle = "INSERT INTO Article (id, author_id, text) VALUES (?, ?, ?)";
            try (PreparedStatement psArticle = connection.prepareStatement(sqlArticle)) {

                for (Article article : authorEntity.getInnerEntityList()) {
                    psArticle.setObject(1, article.getUuid());
                    psArticle.setObject(2, authorEntity.getUuid());
                    psArticle.setString(3, article.getText());
                    psArticle.executeUpdate();
                }

                connection.commit(); // Подтверждаем транзакцию
            } catch (SQLException e) {
                connection.rollback(); // Откатываем транзакцию в случае ошибки
                throw new RuntimeException(e);
            }

            return authorEntity;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public AuthorEntity update(AuthorEntity simpleEntity) {
        return null;
    }
}
