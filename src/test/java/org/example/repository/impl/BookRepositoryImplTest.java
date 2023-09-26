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

@Testcontainers
class BookRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13.1")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("db.sql");

    private BookRepositoryImpl repository;
    private TagRepositoryImpl tagRepository;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection("");
            }
        };
        tagRepository = new TagRepositoryImpl(testConnectionManager);
        repository = new BookRepositoryImpl(testConnectionManager, tagRepository );
    }

    @AfterEach
    void tearDown() throws SQLException {
        repository.clearAll();
    }

    @Test
    void testFindById() throws SQLException {
        BookEntity entity = new BookEntity();
        entity.setBookText("Test BookText");
        TagEntity tag = new TagEntity();
        tag.setTagName("Test TagName");
        entity.getTagEntities().add(tag);
        repository.save(entity);

        BookEntity foundEntity = repository.findById(entity.getUuid()).orElse(null);
        Assertions.assertNotNull(foundEntity);
        Assertions.assertEquals(entity.getUuid(), foundEntity.getUuid());
        Assertions.assertEquals(entity.getBookText(), foundEntity.getBookText());
        Assertions.assertEquals(1, foundEntity.getTagEntities().size());
    }

    @Test
    void testFindAll() throws SQLException {
        repository.save(new BookEntity());
        repository.save(new BookEntity());

        List<BookEntity> entities = repository.findAll();
        Assertions.assertEquals(2, entities.size());
    }

    @Test
    void testDeleteById() throws SQLException {
        BookEntity entity = new BookEntity();
        entity.setBookText("Test BookText");
        TagEntity tag = new TagEntity();
        tag.setTagName("Test TagName");
        entity.getTagEntities().add(tag);
        repository.save(entity);

        boolean isDeleted = repository.deleteById(entity.getUuid());
        Assertions.assertTrue(isDeleted);

        BookEntity deletedEntity = repository.findById(entity.getUuid()).orElse(null);
        Assertions.assertNull(deletedEntity);
    }

    @Test
    void testSave() throws SQLException {
        BookEntity entity = new BookEntity();
        entity.setBookText("Test BookText");
        TagEntity tag = new TagEntity();
        tag.setTagName("Test TagName");
        entity.getTagEntities().add(tag);
        BookEntity savedEntity = repository.save(entity).orElse(null);
        Assertions.assertNotNull(savedEntity);
        Assertions.assertEquals(entity.getBookText(), savedEntity.getBookText());
        Assertions.assertEquals(1, savedEntity.getTagEntities().size());

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById(uuidSavedEntity).orElse(null);
        assert savedEntity != null;
        Assertions.assertEquals(entity.getBookText(), savedEntity.getBookText());
        Assertions.assertEquals(entity.getUuid(), savedEntity.getUuid());
    }

    @Test
    void testUpdate() throws SQLException {
        BookEntity entity = new BookEntity();
        entity.setBookText("Test BookText");
        TagEntity tag = new TagEntity();
        tag.setTagName("Test TagName");
        entity.getTagEntities().add(tag);
        BookEntity savedEntity = repository.save(entity).orElse(null);
        Assertions.assertNotNull(savedEntity);
        Assertions.assertEquals(entity.getBookText(), savedEntity.getBookText());
        Assertions.assertEquals(1, savedEntity.getTagEntities().size());

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById(uuidSavedEntity).orElse(null);
        assert savedEntity != null;
        Assertions.assertEquals(entity.getBookText(), savedEntity.getBookText());
        Assertions.assertEquals(entity.getUuid(), savedEntity.getUuid());

        String updatedBookText = "Updated BookText";
        entity.setBookText(updatedBookText);
        savedEntity = repository.save(entity).orElse(null);
        Assertions.assertNotNull(savedEntity);
        Assertions.assertEquals(entity.getBookText(), savedEntity.getBookText());

        String updatedTagName = "Updated TagName";
        tag.setTagName(updatedTagName);
        savedEntity = repository.save(entity).orElse(null);
        Assertions.assertNotNull(savedEntity);
        Assertions.assertEquals(1, savedEntity.getTagEntities().size());
        TagEntity savedTag = savedEntity.getTagEntities().get(0);
        Assertions.assertNotNull(savedTag);
        Assertions.assertEquals(savedTag.getTagName(), tag.getTagName());
    }

    @Test
    void clearAll() throws SQLException {
        repository.save(new BookEntity());
        repository.save(new BookEntity());

        List<BookEntity> entities = repository.findAll();
        Assertions.assertEquals(2, entities.size());

        repository.clearAll();

        Assertions.assertEquals(0, repository.findAll().size());
    }
}
