package org.example.service.impl;

import org.example.model.SimpleEntity;
import org.example.repository.Repository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleServiceImplTest {

    private Repository<SimpleEntity, UUID> mockRepository;
    private SimpleServiceImpl service;

    @BeforeEach
    public void setUp() {
        mockRepository = mock(Repository.class);
        service = new SimpleServiceImpl(mockRepository);
    }

    @Test
    void testSaveEntity() throws SQLException {
        SimpleEntity entity = new SimpleEntity();
        entity.setUuid(UUID.randomUUID());
        entity.setDescription("Test");

        when(mockRepository.save(entity)).thenReturn(Optional.of(entity));

        Optional<SimpleEntity> savedEntityOpt = service.save(entity);

        assertTrue(savedEntityOpt.isPresent());
        assertEquals(entity, savedEntityOpt.get());
    }

    @Test
    void testFindById() throws SQLException {
        UUID uuid = UUID.randomUUID();
        SimpleEntity entity = new SimpleEntity();
        entity.setUuid(uuid);
        entity.setDescription("Test");

        when(mockRepository.findById(uuid)).thenReturn(Optional.of(entity));

        Optional<SimpleEntity> foundEntityOpt = service.findById(uuid);

        assertTrue(foundEntityOpt.isPresent());
        assertEquals(entity, foundEntityOpt.get());
    }

    @Test
    void testFindAll() throws SQLException {
        SimpleEntity entity1 = new SimpleEntity();
        entity1.setUuid(UUID.randomUUID());
        entity1.setDescription("Test1");

        SimpleEntity entity2 = new SimpleEntity();
        entity2.setUuid(UUID.randomUUID());
        entity2.setDescription("Test2");

        when(mockRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        List<SimpleEntity> entities = service.findAll();

        assertEquals(2, entities.size());
    }

    @Test
    void testDelete() throws SQLException {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.deleteById(uuid)).thenReturn(true);

        boolean result = service.delete(uuid);

        assertTrue(result);
    }

    @Test
    void testDeleteException() throws SQLException {
        UUID uuid = UUID.randomUUID();
        when(mockRepository.deleteById(uuid)).thenThrow(new RuntimeException("Delete error"));

        assertThrows( RuntimeException.class, () -> service.delete( uuid ) );
    }

    @Test
    void testGetRepository() {
        Repository<SimpleEntity, UUID> result = service.getRepository();
        assertEquals(mockRepository, result);
    }
}
