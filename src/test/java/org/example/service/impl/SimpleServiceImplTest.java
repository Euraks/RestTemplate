package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SimpleServiceImplTest {

    private Repository<SimpleEntity, UUID> mockRepository;
    private SimpleServiceImpl service;

    @BeforeEach
    public void setUp() {
        mockRepository = mock( Repository.class );
        service = new SimpleServiceImpl( mockRepository );
    }

    @Test
    void testSaveEntity() throws SQLException {
        SimpleEntity entity = new SimpleEntity();
        entity.setUuid( UUID.randomUUID() );
        entity.setDescription( "Test" );

        when( mockRepository.save( entity ) ).thenReturn( entity );

        SimpleEntity savedEntity = service.save( entity );

        assertEquals( entity, savedEntity );
        verify( mockRepository, times( 1 ) ).save( entity );
    }


    @Test
    void testFindById() {
        UUID uuid = UUID.randomUUID();
        SimpleEntity entity = new SimpleEntity();
        entity.setUuid( uuid );
        entity.setDescription( "Test" );

        when( mockRepository.findById( uuid ) ).thenReturn( entity );

        SimpleEntity foundEntity = service.findById( uuid );

        assertEquals( entity, foundEntity );
        verify( mockRepository, times( 1 ) ).findById( uuid );
    }

    @Test
    void testFindAll() throws SQLException {
        SimpleEntity entity1 = new SimpleEntity();
        entity1.setUuid( UUID.randomUUID() );
        entity1.setDescription( "Test1" );

        SimpleEntity entity2 = new SimpleEntity();
        entity2.setUuid( UUID.randomUUID() );
        entity2.setDescription( "Test2" );

        when( mockRepository.findAll() ).thenReturn( Arrays.asList( entity1, entity2 ) );

        List<SimpleEntity> entities = service.findAll();

        assertEquals( 2, entities.size() );
        verify( mockRepository, times( 1 ) ).findAll();
    }

    @Test
    void testDelete() {
        UUID uuid = UUID.randomUUID();
        when( mockRepository.deleteById( uuid ) ).thenReturn( true );

        boolean result = service.delete( uuid );

        assertTrue( result );
        verify( mockRepository, times( 1 ) ).deleteById( uuid );
    }

    @Test
    void testGetRepository() {
        Repository<SimpleEntity, UUID> result = service.getRepository();
        assertEquals(mockRepository, result);
    }

}