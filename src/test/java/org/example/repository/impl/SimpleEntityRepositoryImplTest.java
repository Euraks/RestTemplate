package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
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
public class SimpleEntityRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript( "db.sql" );

    private SimpleEntityRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection( "" );
            }
        };
        repository = new SimpleEntityRepositoryImpl( testConnectionManager );
    }

    @AfterEach
    void tearDown() throws SQLException {
        repository.clearAll();
    }

    @Test
    void testSave() throws SQLException {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        SimpleEntity savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        Assertions.assertEquals( entity.getDescription(), savedEntity.getDescription() );

        UUID uuidSavedEntity = entity.getUuid();
        savedEntity = repository.findById( uuidSavedEntity ).orElse( null );
        assert savedEntity != null;
        Assertions.assertEquals( entity.getDescription(), savedEntity.getDescription() );
        Assertions.assertEquals( entity.getUuid(), savedEntity.getUuid() );
    }

    @Test
    void testSaveUpdate() throws SQLException {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        UUID uuidSavedEntity = entity.getUuid();

        SimpleEntity savedEntity = repository.save( entity ).orElse( null );
        Assertions.assertNotNull( savedEntity );
        Assertions.assertEquals( entity.getDescription(), savedEntity.getDescription() );

        entity = repository.findById( uuidSavedEntity ).orElse( null );
        Assertions.assertNotNull( entity );
        entity.setDescription( "Updated description" );
        repository.save( entity );

        savedEntity = repository.findById( uuidSavedEntity ).orElse( null );
        assert savedEntity != null;
        Assertions.assertEquals( entity.getDescription(), savedEntity.getDescription() );
        Assertions.assertEquals( entity.getUuid(), savedEntity.getUuid() );
    }

    @Test
    void testFindById() throws SQLException {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        repository.save( entity );

        SimpleEntity foundEntity = repository.findById( entity.getUuid() ).orElse( null );
        Assertions.assertNotNull( foundEntity );
        Assertions.assertEquals( entity.getUuid(), foundEntity.getUuid() );
        Assertions.assertEquals( entity.getDescription(), foundEntity.getDescription() );
    }

    @Test
    void testDeleteById() throws SQLException {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        repository.save( entity );

        boolean isDeleted = repository.deleteById( entity.getUuid() );
        Assertions.assertTrue( isDeleted );

        SimpleEntity deletedEntity = repository.findById( entity.getUuid() ).orElse( null );
        Assertions.assertNull( deletedEntity );
    }

    @Test
    void testFindAll() throws SQLException {
        repository.save( new SimpleEntity( "Test1" ) );
        repository.save( new SimpleEntity( "Test2" ) );

        List<SimpleEntity> entities = repository.findAll();
        Assertions.assertEquals( 2, entities.size() );
    }

    @Test
    void clearAll() throws SQLException {
        repository.save( new SimpleEntity( "Test1" ) );
        repository.save( new SimpleEntity( "Test2" ) );

        List<SimpleEntity> entities = repository.findAll();
        Assertions.assertEquals( 2, entities.size() );

        repository.clearAll();

        Assertions.assertEquals(0, repository.findAll().size() );
    }
}
