package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SimpleServiceImplTest {

    private SimpleServiceImpl service;
    private Repository<SimpleEntity, UUID> mockRepository;

    @BeforeEach
    public void setUp() {
        mockRepository = mock( Repository.class );
        service = new SimpleServiceImpl( mockRepository );
    }

    @Test
    void testSave() throws SQLException {
        SimpleEntity entityToSave = new SimpleEntity( "Test" );
        SimpleEntity savedEntity = new SimpleEntity( "Test" );

        when( mockRepository.save( entityToSave ) ).thenReturn( savedEntity );

        SimpleEntity result = service.save( entityToSave );

        assertEquals( savedEntity, result );
        verify( mockRepository ).save( entityToSave );
    }

    @Test
    void testFindById() {
        UUID testUuid = UUID.randomUUID();
        SimpleEntity foundEntity = new SimpleEntity( "Found" );

        when( mockRepository.findById( testUuid ) ).thenReturn( foundEntity );

        SimpleEntity result = service.findById( testUuid );

        assertEquals( foundEntity, result );
    }

    @Test
    void testFindAll() throws SQLException {
        List<SimpleEntity> entities = Arrays.asList( new SimpleEntity( "One" ), new SimpleEntity( "Two" ) );

        when( mockRepository.findAll() ).thenReturn( entities );

        List<SimpleEntity> result = service.findAll();

        assertEquals( entities, result );
    }

    @Test
    void testDelete() {
        UUID testUuid = UUID.randomUUID();
        when( mockRepository.deleteById( testUuid ) ).thenReturn( true ); // Предполагая, что ваш метод deleteById возвращает boolean

        boolean result = service.delete( testUuid );

        Assertions.assertTrue( result );
    }
}
