package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.example.repository.mapper.ArticleResultSetMapper;
import org.example.repository.mapper.AuthorEntityResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AuthorEntityRepositoryImpl implements AuthorEntityRepository<AuthorEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger( AuthorEntityRepositoryImpl.class );
    private static final String ERROR_EXECUTING_QUERY = "Error executing query: {}";

    private static final String FIND_AUTHOR_BY_ID_SQL = "SELECT uuid, authorName FROM AuthorEntity WHERE uuid = ?";
    private static final String DELETE_AUTHOR_BY_ID_SQL = "DELETE FROM AuthorEntity WHERE  uuid = ?";
    private static final String FIND_ALL_AUTHORS_SQL = "SELECT * FROM AuthorEntity";
    private static final String SAVE_AUTHOR_SQL = "INSERT INTO AuthorEntity (uuid, authorName) VALUES (?, ?) " +
            "ON CONFLICT (uuid) DO UPDATE SET authorName = EXCLUDED.authorName";
    private static final String FIND_ARTICLES_BY_AUTHOR_ID_SQL = "SELECT uuid, author_id, text FROM Article WHERE author_id = ?";
    private static final String SAVE_ARTICLE_SQL = "INSERT INTO Article (uuid, author_id, text) VALUES (?, ?, ?) " +
            "ON CONFLICT (uuid) DO UPDATE SET author_id = EXCLUDED.author_id, text = EXCLUDED.text";
    private static final String DELETE_ARTICLE = "DELETE FROM Article";
    private static final String DELETE_AUTHORS = "DELETE FROM AuthorEntity";

    private final ConnectionManager connectionManager;

    public AuthorEntityRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<AuthorEntity> findById(UUID uuid) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement( FIND_AUTHOR_BY_ID_SQL )){

            statement.setObject( 1, uuid );
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return Optional.of( createAuthorFromResultSet( resultSet ) );
                }
                return Optional.empty();
            }
        } catch(SQLException e){
            LOGGER.error( ERROR_EXECUTING_QUERY, FIND_AUTHOR_BY_ID_SQL, e );
            return Optional.empty();
        }
    }


    @Override
    public List<AuthorEntity> findAll() {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement( FIND_ALL_AUTHORS_SQL )){

            try (ResultSet resultSet = statement.executeQuery()){
                List<AuthorEntity> authors = new ArrayList<>();
                while (resultSet.next()) {
                    authors.add( createAuthorFromResultSet( resultSet ) );
                }
                return authors;
            }
        } catch(SQLException e){
            LOGGER.error( ERROR_EXECUTING_QUERY, FIND_ALL_AUTHORS_SQL, e );
            return Collections.emptyList();
        }
    }

    @Override
    public boolean deleteById(UUID uuid) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement( DELETE_AUTHOR_BY_ID_SQL )){
            statement.setObject( 1, uuid );
            return statement.executeUpdate() > 0;
        } catch(SQLException e){
            LOGGER.error( ERROR_EXECUTING_QUERY, DELETE_AUTHOR_BY_ID_SQL, e );
            return false;
        }
    }


    @Override
    public Optional<AuthorEntity> save(AuthorEntity authorEntity) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement psAuthor = connection.prepareStatement( SAVE_AUTHOR_SQL )){
            connection.setAutoCommit( false );
            psAuthor.setObject( 1, authorEntity.getUuid() );
            psAuthor.setString( 2, authorEntity.getAuthorName() );
            psAuthor.executeUpdate();

            if (authorEntity.getArticleList() != null && !authorEntity.getArticleList().isEmpty()) {
                try (PreparedStatement psArticle = connection.prepareStatement( SAVE_ARTICLE_SQL )){
                    for (Article article : authorEntity.getArticleList()) {
                        psArticle.setObject( 1, article.getUuid()==null?UUID.randomUUID():article.getUuid() );
                        psArticle.setObject( 2, authorEntity.getUuid() );
                        psArticle.setString( 3, article.getText() );
                        psArticle.addBatch();
                    }
                    psArticle.executeBatch();
                }
            }
            connection.commit();
            return Optional.of( authorEntity );
        } catch(SQLException e){
            LOGGER.error( ERROR_EXECUTING_QUERY, SAVE_AUTHOR_SQL, e );
            return Optional.empty();
        }
    }

    @Override
    public List<Article> findArticlesAll() {
        List<Article> articles = new ArrayList<>();
        String articlesSql = "SELECT * FROM Article";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement( articlesSql );
             ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()) {
                Article article = ArticleResultSetMapper.INSTANCE.map( resultSet );
                articles.add( article );
            }
        } catch(SQLException e){
            LOGGER.error( ERROR_EXECUTING_QUERY,"SELECT * FROM Article", e );
        }
        return articles;
    }

    @Override
    public void clearAll() {
        try (Connection connection = connectionManager.getConnection()){
            connection.setAutoCommit( false );

            try (PreparedStatement deleteArticles = connection.prepareStatement( DELETE_ARTICLE )){
                deleteArticles.executeUpdate();
            }

            try (PreparedStatement deleteAuthors = connection.prepareStatement( DELETE_AUTHORS )){
                deleteAuthors.executeUpdate();
            }

            connection.commit();
        } catch(SQLException e){
            LOGGER.error( "Error while clearing AuthorEntity and related entities", e );
        }
    }

    private AuthorEntity createAuthorFromResultSet(ResultSet resultSet) throws SQLException {
        AuthorEntity authorEntity = AuthorEntityResultSetMapper.INSTANCE.map( resultSet );
        authorEntity.setArticleList( findArticlesByAuthorId( authorEntity.getUuid() ) );
        return authorEntity;
    }

    private List<Article> findArticlesByAuthorId(UUID authorId) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement( FIND_ARTICLES_BY_AUTHOR_ID_SQL )){

            statement.setObject( 1, authorId );
            try (ResultSet resultSet = statement.executeQuery()){
                List<Article> articles = new ArrayList<>();
                while (resultSet.next()) {
                    articles.add( createArticleFromResultSet( resultSet ) );
                }
                return articles;
            }
        } catch(SQLException e){
            LOGGER.error( ERROR_EXECUTING_QUERY, FIND_ARTICLES_BY_AUTHOR_ID_SQL, e );
            return Collections.emptyList();
        }
    }

    private Article createArticleFromResultSet(ResultSet resultSet) throws SQLException {
        return ArticleResultSetMapper.INSTANCE.map( resultSet );
    }
}

