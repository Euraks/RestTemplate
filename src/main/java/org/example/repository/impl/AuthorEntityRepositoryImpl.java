package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthorEntityRepositoryImpl implements AuthorEntityRepository<AuthorEntity, UUID> {

    private static final Logger LOGGER = LoggerFactory.getLogger( AuthorEntityRepositoryImpl.class );

    private static final String FIND_AUTHOR_BY_ID_SQL = "SELECT id, authorName FROM AuthorEntity WHERE id = ?";
    private static final String DELETE_AUTHOR_BY_ID_SQL = "DELETE FROM AuthorEntity WHERE id = ?";
    private static final String FIND_ALL_AUTHORS_SQL = "SELECT * FROM AuthorEntity";
    private static final String SAVE_AUTHOR_SQL = "INSERT INTO AuthorEntity (id, authorName) VALUES (?, ?) " +
            "ON CONFLICT (id) DO UPDATE SET authorName = EXCLUDED.authorName";
    private static final String FIND_ARTICLES_BY_AUTHOR_ID_SQL = "SELECT id, author_id, text FROM Article WHERE author_id = ?";
    private static final String SAVE_ARTICLE_SQL = "INSERT INTO Article (id, author_id, text) VALUES (?, ?, ?) " +
            "ON CONFLICT (id) DO UPDATE SET author_id = EXCLUDED.author_id, text = EXCLUDED.text";

    private final ConnectionManager connectionManager;

    public AuthorEntityRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<AuthorEntity> findById(UUID uuid) {
        return Optional.ofNullable( executeQuery( FIND_AUTHOR_BY_ID_SQL, statement -> {
            statement.setObject( 1, uuid );
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return createAuthorFromResultSet( resultSet );
                }
                return null;
            }
        } ) );
    }


    @Override
    public List<AuthorEntity> findAll() {
        return executeQuery( FIND_ALL_AUTHORS_SQL, statement -> {
            try (ResultSet resultSet = statement.executeQuery()){
                List<AuthorEntity> authors = new ArrayList<>();
                while (resultSet.next()) {
                    authors.add( createAuthorFromResultSet( resultSet ) );
                }
                return authors;
            }
        } );
    }

    @Override
    public boolean deleteById(UUID uuid) {
        return executeUpdate( DELETE_AUTHOR_BY_ID_SQL, statement -> {
            statement.setObject( 1, uuid );
            return statement.executeUpdate() > 0;
        } );
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
                        psArticle.setObject( 1, article.getUuid() );
                        psArticle.setObject( 2, article.getAuthor_uuid() );
                        psArticle.setString( 3, article.getText() );
                        psArticle.addBatch();
                    }
                    psArticle.executeBatch();
                }
            }
            connection.commit();
            return Optional.of( authorEntity );
        } catch(SQLException e){
            LOGGER.error( "Error while saving author: {}", authorEntity, e );
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
                UUID uuid = UUID.fromString( resultSet.getString( "id" ) );
                UUID authorId = UUID.fromString( resultSet.getString( "author_id" ) );
                String text = resultSet.getString( "text" );

                Article article = new Article();
                article.setUuid( uuid );
                article.setAuthor_uuid( authorId );
                article.setText( text );
                articles.add( article );
            }
        } catch(SQLException e){
            LOGGER.error( "Error while fetching all articles", e );
        }
        return articles;
    }

    @Override
    public void clearAll() {
        throw new UnsupportedOperationException( "Clear all operation not supported for authors" );
    }

    private AuthorEntity createAuthorFromResultSet(ResultSet resultSet) throws SQLException {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setUuid( (UUID) resultSet.getObject( "id" ) );
        authorEntity.setAuthorName( resultSet.getString( "authorName" ) );
        authorEntity.setArticleList( findArticlesByAuthorId( authorEntity.getUuid() ) );
        return authorEntity;
    }

    private List<Article> findArticlesByAuthorId(UUID authorId) {
        return executeQuery( FIND_ARTICLES_BY_AUTHOR_ID_SQL, statement -> {
            statement.setObject( 1, authorId );
            try (ResultSet resultSet = statement.executeQuery()){
                List<Article> articles = new ArrayList<>();
                while (resultSet.next()) {
                    articles.add( createArticleFromResultSet( resultSet ) );
                }
                return articles;
            }
        } );
    }

    private Article createArticleFromResultSet(ResultSet resultSet) throws SQLException {
        Article article = new Article();
        article.setUuid( (UUID) resultSet.getObject( "id" ) );
        article.setAuthor_uuid( (UUID) resultSet.getObject( "author_id" ) );
        article.setText( resultSet.getString( "text" ) );
        return article;
    }

    private <T> T executeQuery(String sql, PreparedStatementSetterAndMapper<T> setterAndMapper) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement( sql )){

            return setterAndMapper.apply( statement );
        } catch(SQLException e){
            LOGGER.error( "Error executing query: {}", sql, e );
            throw new RuntimeException( e );
        }
    }

    private boolean executeUpdate(String sql, PreparedStatementSetter setter) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement( sql )){

            return setter.apply( statement );
        } catch(SQLException e){
            LOGGER.error( "Error executing update: {}", sql, e );
            throw new RuntimeException( e );
        }
    }

    @FunctionalInterface
    private interface PreparedStatementSetterAndMapper<T> {
        T apply(PreparedStatement statement) throws SQLException;
    }

    @FunctionalInterface
    public interface PreparedStatementSetter {
        boolean apply(PreparedStatement statement) throws SQLException;
    }

}

