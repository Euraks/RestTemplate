package org.example.repository.impl;

import org.example.db.HikariCPDataSource;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;



public class AuthorEntityRepositoryImpl implements AuthorEntityRepository<AuthorEntity, UUID> {

    private final HikariCPDataSource connectionManager = new HikariCPDataSource();

    public AuthorEntity findById(UUID id) {
        String sql = "SELECT id, authorName FROM AuthorEntity WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                AuthorEntity authorEntity = new AuthorEntity();
                authorEntity.setUuid((UUID) resultSet.getObject("id"));
                authorEntity.setAuthorName(resultSet.getString("authorName"));

                List<Article> articles = findArticlesByAuthorId(id, connection);
                authorEntity.setInnerEntityList(articles);

                return authorEntity;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private List<Article> findArticlesByAuthorId(UUID authorId, Connection connection) throws SQLException {
        List<Article> articles = new ArrayList<>();
        String sql = "SELECT id, text FROM Article WHERE author_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setObject(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Article article = new Article();
                article.setUuid((UUID) resultSet.getObject("id"));
                article.setText(resultSet.getString("text"));
                articles.add(article);
            }
        }
        return articles;
    }

    @Override
    public boolean deleteById(UUID id) {
        String sql = "DELETE FROM AuthorEntity WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AuthorEntity> findAll() {
        HashMap<UUID, AuthorEntity> authorsMap = new HashMap<>();

        String authorsSql = "SELECT * FROM AuthorEntity";
        String articlesSql = "SELECT * FROM Article";

        try (Connection connection = connectionManager.getConnection()) {
            // Запрос для авторов
            try (PreparedStatement preparedStatement = connection.prepareStatement(authorsSql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("id"));
                    String authorName = resultSet.getString("authorName");
                    AuthorEntity author = new AuthorEntity();
                    author.setUuid(uuid);
                    author.setAuthorName(authorName);
                    authorsMap.put(uuid, author);
                }
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(articlesSql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("id"));
                    UUID authorId = UUID.fromString(resultSet.getString("author_id"));
                    String text = resultSet.getString("text");

                    Article article = new Article();
                    article.setUuid(uuid);
                    article.setText(text);

                    AuthorEntity author = authorsMap.get(authorId);
                    if (author != null) {
                        if (author.getInnerEntityList() == null) {
                            author.setInnerEntityList(new ArrayList<>());
                        }
                        author.getInnerEntityList().add(article);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return new ArrayList<>(authorsMap.values());
    }

    @Override
    public AuthorEntity save(AuthorEntity authorEntity) {
        String sqlAuthor = "INSERT INTO AuthorEntity (id, authorName) VALUES (?, ?)";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement psAuthor = connection.prepareStatement( sqlAuthor )){

            connection.setAutoCommit( false ); // Начинаем транзакцию

            psAuthor.setObject( 1, authorEntity.getUuid() );
            psAuthor.setString( 2, authorEntity.getAuthorName() );
            psAuthor.executeUpdate();

            String sqlArticle = "INSERT INTO Article (id, author_id, text) VALUES (?, ?, ?)";
            try (PreparedStatement psArticle = connection.prepareStatement( sqlArticle )){

                for (Article article : authorEntity.getInnerEntityList()) {
                    psArticle.setObject( 1, article.getUuid() );
                    psArticle.setObject( 2, authorEntity.getUuid() );
                    psArticle.setString( 3, article.getText() );
                    psArticle.executeUpdate();
                }

                connection.commit();
            } catch(SQLException e){
                connection.rollback();
                throw new RuntimeException( e );
            }

            return authorEntity;

        } catch(SQLException e){
            throw new RuntimeException( e );
        }
    }


    @Override
    public AuthorEntity update(AuthorEntity authorEntity) {
        String updateAuthorSQL = "UPDATE AuthorEntity SET authorName = ? WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateAuthorStmt = connection.prepareStatement(updateAuthorSQL)) {

            updateAuthorStmt.setString(1, authorEntity.getAuthorName());
            updateAuthorStmt.setObject(2, authorEntity.getUuid());
            updateAuthorStmt.executeUpdate();

            String updateArticleSQL = "UPDATE Article SET text = ? WHERE id = ? AND author_id = ?";
            try (PreparedStatement updateArticleStmt = connection.prepareStatement(updateArticleSQL)) {

                for (Article article : authorEntity.getInnerEntityList()) {
                    updateArticleStmt.setString(1, article.getText());
                    updateArticleStmt.setObject(2, article.getUuid());
                    updateArticleStmt.setObject(3, authorEntity.getUuid());
                    updateArticleStmt.addBatch();
                }

                updateArticleStmt.executeBatch();

            } catch (SQLException e) {
                throw new RuntimeException("Error updating articles: ", e);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error updating author: ", e);
        }
        return authorEntity;
    }

}