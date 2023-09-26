package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;


@Testcontainers
class AuthorEntityRepositoryImplTest  {
    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript( "db.sql" );

    private AuthorEntityRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };
        repository = new AuthorEntityRepositoryImpl( testConnectionManager );
    }

    @AfterEach
    void tearDown() throws SQLException {
        repository.clearAll();
    }

    @Test
    void testFindById() throws SQLException {
        AuthorEntity entity = new AuthorEntity(  );
        entity.setAuthorName( "Test AuthorName" );
        Article article = new Article();
        article.setText( "Test Text" );
        entity.getArticleList().add( article );
        repository.save( entity );

        AuthorEntity foundEntity = repository.findById( entity.getUuid() ).orElse( null );
        Assertions.assertNotNull( foundEntity );
        Assertions.assertEquals( entity.getUuid(), foundEntity.getUuid() );
        Assertions.assertEquals( entity.getAuthorName(), foundEntity.getAuthorName() );
        Assertions.assertEquals( 1, foundEntity.getArticleList().size() );
    }

    @Test
    void testFindAll() throws SQLException {
        repository.save( new AuthorEntity(  ) );
        repository.save( new AuthorEntity(  ) );


        List<AuthorEntity> entities = repository.findAll();
        Assertions.assertEquals( 2, entities.size() );
    }

    @Test
    void testDeleteById() throws SQLException {
        AuthorEntity entity = new AuthorEntity(  );
        entity.setAuthorName( "Test AuthorName" );
        Article article = new Article();
        article.setText( "Test Text" );
        entity.getArticleList().add( article );
        repository.save( entity );

        boolean isDeleted = repository.deleteById( entity.getUuid() );
        Assertions.assertTrue( isDeleted );

        AuthorEntity deletedEntity = repository.findById( entity.getUuid() ).orElse( null );
        Assertions.assertNull( deletedEntity );
    }

    @Test
    void testSave() throws SQLException {
        AuthorEntity entity = new AuthorEntity(  );
        entity.setAuthorName( "Test AuthorName" );
        Article article = new Article();
        article.setText( "Test Text" );
        entity.getArticleList().add( article );
        AuthorEntity savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        Assertions.assertEquals( entity.getAuthorName(), savedEntity.getAuthorName() );
        Assertions.assertEquals( 1, savedEntity.getArticleList().size() );

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById( uuidSavedEntity ).orElse( null );
        assert savedEntity != null;
        Assertions.assertEquals( entity.getAuthorName(), savedEntity.getAuthorName() );
        Assertions.assertEquals( entity.getUuid(), savedEntity.getUuid() );
    }

    @Test
    void testUpdate() throws SQLException {
        AuthorEntity entity = new AuthorEntity(  );
        entity.setAuthorName( "Test AuthorName" );
        Article article = new Article();
        article.setText( "Test Text" );
        entity.getArticleList().add( article );
        AuthorEntity savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        Assertions.assertEquals( entity.getAuthorName(), savedEntity.getAuthorName() );
        Assertions.assertEquals( 1, savedEntity.getArticleList().size() );

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById( uuidSavedEntity ).orElse( null );
        assert savedEntity != null;
        Assertions.assertEquals( entity.getAuthorName(), savedEntity.getAuthorName() );
        Assertions.assertEquals( entity.getUuid(), savedEntity.getUuid() );

        String updatedAuthorName = "Updated AuthorName";
        entity.setAuthorName( updatedAuthorName );
        savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        Assertions.assertEquals( entity.getAuthorName(), savedEntity.getAuthorName() );
        Assertions.assertEquals( 1, savedEntity.getArticleList().size() );

        String updatedArticleText = "Updated ArticleText";
        article.setText( updatedArticleText );
        savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        Assertions.assertEquals( 1, savedEntity.getArticleList().size() );
        Article savedArticle = savedEntity.getArticleList().get( 0 );
        Assertions.assertNotNull( savedArticle );
        Assertions.assertEquals( savedArticle.getText(), article.getText() );
    }

    @Test
    void findArticlesAll() throws SQLException {
        AuthorEntity entity = new AuthorEntity(  );
        entity.setAuthorName( "Test AuthorName" );
        Article article = new Article();
        article.setText( "Test Text" );
        entity.getArticleList().add( article );
        Article newArticle = new Article();
        newArticle.setText( "Test Text" );
        entity.getArticleList().add( newArticle );
        repository.save( entity );

        List<Article> articleList = repository.findArticlesAll();

        Assertions.assertEquals( 2,articleList.size() );

    }

    @Test
    void clearAll() throws SQLException {
        repository.save( new AuthorEntity(  ) );
        repository.save( new AuthorEntity(  ) );


        List<AuthorEntity> entities = repository.findAll();
        Assertions.assertEquals( 2, entities.size() );

        repository.clearAll();

        Assertions.assertEquals(0, repository.findAll().size() );
    }
}