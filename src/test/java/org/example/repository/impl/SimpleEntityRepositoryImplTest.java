package org.example.repository.impl;

import org.example.db.ConnectionManager;
import org.example.model.SimpleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public class SimpleEntityRepositoryImplTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>( "postgres:13.1" )
            .withDatabaseName( "test-db" )
            .withUsername( "test" )
            .withPassword( "test" )
            .withInitScript("db.sql");

    private SimpleEntityRepositoryImpl repository;

    @BeforeEach
    void setUp() {

        ConnectionManager testConnectionManager = new ConnectionManager() {
            @Override
            public Connection getConnection() throws SQLException {
                return postgreSQLContainer.createConnection("");
            }
        };
        repository = new SimpleEntityRepositoryImpl(testConnectionManager);
    }

    @Test
    void testSave() {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        SimpleEntity savedEntity = repository.save( entity );
        assertNotNull( savedEntity );
        assertEquals( entity.getDescription(), savedEntity.getDescription() );
        entity.setDescription( "Update description" );
        savedEntity = repository.save( entity );
        assertNotNull( savedEntity );
        assertEquals( entity.getDescription(), savedEntity.getDescription() );
    }

    @Test
    void testFindById() {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        repository.save( entity );

        SimpleEntity foundEntity = repository.findById( entity.getUuid() );
        assertNotNull( foundEntity );
        assertEquals( entity.getUuid(), foundEntity.getUuid() );
    }

    @Test
    void testDeleteById() {
        SimpleEntity entity = new SimpleEntity( "Test description" );
        repository.save( entity );

        boolean isDeleted = repository.deleteById( entity.getUuid() );
        Assertions.assertTrue( isDeleted );

        SimpleEntity deletedEntity = repository.findById( entity.getUuid() );
        Assertions.assertNull( deletedEntity );
    }

    @Test
    void testFindAll() {
        repository.save( new SimpleEntity( "Test1" ) );
        repository.save( new SimpleEntity( "Test2" ) );

        List<SimpleEntity> entities = repository.findAll();
        assertEquals( 2, entities.size() );
    }
}

