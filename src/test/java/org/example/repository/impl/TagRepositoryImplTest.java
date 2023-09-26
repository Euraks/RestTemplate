package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.BookEntity;
import org.example.model.TagEntity;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class TagRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript( "db.sql" );

    private TagRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };
        repository = new TagRepositoryImpl( testConnectionManager );
    }

    @AfterEach
    void tearDown() throws SQLException {
        repository.clearAll();
    }

    @Test
    void testFindById() throws SQLException {
        TagEntity entity = new TagEntity();
        entity.setTagName( "Test TagName" );
        BookEntity book = new BookEntity();
        book.setBookText( "Test Text" );
        entity.getBookEntities().add( book );
        repository.save( entity );

        TagEntity foundEntity = repository.findById( entity.getUuid() ).orElse( null );
        Assertions.assertNotNull( foundEntity );
        assertEquals( entity.getUuid(), foundEntity.getUuid() );
        assertEquals( entity.getTagName(), foundEntity.getTagName() );
        assertEquals( 1, foundEntity.getBookEntities().size() );
    }

    @Test
    void testFindAll() throws SQLException {
        repository.save( new TagEntity() );
        repository.save( new TagEntity() );


        List<TagEntity> entities = repository.findAll();
        assertEquals( 2, entities.size() );
    }

    @Test
    void testDeleteById() throws SQLException {
        TagEntity entity = new TagEntity();
        entity.setTagName( "Test TagName" );
        BookEntity book = new BookEntity();
        book.setBookText( "Test Text" );
        entity.getBookEntities().add( book );
        repository.save( entity );

        boolean isDeleted = repository.deleteById( entity.getUuid() );
        Assertions.assertTrue( isDeleted );

        TagEntity deletedEntity = repository.findById( entity.getUuid() ).orElse( null );
        Assertions.assertNull( deletedEntity );
    }

    @Test
    void testSave() throws SQLException {
        TagEntity entity = new TagEntity();
        entity.setTagName( "Test TagName" );
        BookEntity book = new BookEntity();
        book.setBookText( "Test Text" );
        entity.getBookEntities().add( book );
        TagEntity savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        assertEquals( entity.getTagName(), savedEntity.getTagName() );
        assertEquals( 1, savedEntity.getBookEntities().size() );

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById( uuidSavedEntity ).orElse( null );
        assert savedEntity != null;
        assertEquals( entity.getTagName(), savedEntity.getTagName() );
        assertEquals( entity.getUuid(), savedEntity.getUuid() );
    }

    @Test
    void testUpdate() throws SQLException {
        TagEntity entity = new TagEntity();
        entity.setTagName( "Test TagName" );
        BookEntity book = new BookEntity();
        book.setBookText( "Test Text" );
        entity.getBookEntities().add( book );
        TagEntity savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        assertEquals( entity.getTagName(), savedEntity.getTagName() );
        assertEquals( 1, savedEntity.getBookEntities().size() );

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById( uuidSavedEntity ).orElse( null );
        assert savedEntity != null;
        assertEquals( entity.getTagName(), savedEntity.getTagName() );
        assertEquals( entity.getUuid(), savedEntity.getUuid() );

        String updatedAuthorName = "Updated AuthorName";
        entity.setTagName( updatedAuthorName );
        savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        assertEquals( entity.getTagName(), savedEntity.getTagName() );
        assertEquals( 1, savedEntity.getBookEntities().size() );

        String updatedArticleText = "Updated ArticleText";
        book.setBookText( updatedArticleText );
        savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        assertEquals( 1, savedEntity.getBookEntities().size() );
        BookEntity savedBook = savedEntity.getBookEntities().get( 0 );
        Assertions.assertNotNull( savedBook );
        assertEquals( savedBook.getBookText(), book.getBookText() );
    }


    @Test
    void clearAll() throws SQLException {
        repository.save( new TagEntity() );
        repository.save( new TagEntity() );


        List<TagEntity> entities = repository.findAll();
        assertEquals( 2, entities.size() );

        repository.clearAll();

        assertEquals( 0, repository.findAll().size() );
    }
}