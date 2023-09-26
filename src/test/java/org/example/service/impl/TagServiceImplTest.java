package org.example.service.impl;

import org.example.model.TagEntity;
import org.example.repository.Repository;
import org.example.repository.impl.TagRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TagServiceImplTest {

    @Mock
    private TagRepositoryImpl repository;

    @InjectMocks
    private TagServiceImpl tagService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTagEntitySuccessfully() throws SQLException {
        TagEntity tagEntity = new TagEntity();

        when(repository.save(tagEntity)).thenReturn(Optional.of(tagEntity));

        Optional<TagEntity> savedEntity = tagService.save(tagEntity);

        assertTrue(savedEntity.isPresent());
        assertEquals(tagEntity, savedEntity.get());
        verify(repository, times(1)).save(tagEntity);
    }

    @Test
    void testSaveTagEntityWithSQLException() throws SQLException {
        TagEntity tagEntity = new TagEntity();

        doThrow(new SQLException("Simulated SQL Exception")).when(repository).save(tagEntity);

        Optional<TagEntity> savedEntity = tagService.save(tagEntity);

        assertFalse(savedEntity.isPresent());
        verify(repository, times(1)).save(tagEntity);
    }

    @Test
    void testFindById() throws SQLException {
        UUID uuid = UUID.randomUUID();
        TagEntity tagEntity = new TagEntity();
        tagEntity.setUuid(uuid);

        when(repository.findById(uuid)).thenReturn(Optional.of(tagEntity));

        Optional<TagEntity> foundEntity = tagService.findById(uuid);

        assertTrue(foundEntity.isPresent());
        assertEquals(tagEntity, foundEntity.get());
    }

    @Test
    void testFindAllSuccessfully() throws SQLException {
        TagEntity tagEntity = new TagEntity();

        when(repository.findAll()).thenReturn(Collections.singletonList(tagEntity));

        List<TagEntity> allEntities = tagService.findAll();

        assertFalse(allEntities.isEmpty());
        assertEquals(1, allEntities.size());
        assertEquals(tagEntity, allEntities.get(0));
    }

    @Test
    void testFindAllWithSQLException() throws SQLException {
        when(repository.findAll()).thenThrow(new SQLException("Simulated SQL Exception"));

        List<TagEntity> allEntities = tagService.findAll();

        assertNotNull(allEntities);
        assertTrue(allEntities.isEmpty());
    }

    @Test
    void testDeleteTagEntitySuccessfully() throws SQLException {
        UUID uuid = UUID.randomUUID();

        when(repository.deleteById(uuid)).thenReturn(true);

        boolean deleted = tagService.delete(uuid);

        assertTrue(deleted);
    }

    @Test
    void testDeleteTagEntityNotFound() throws SQLException {
        UUID uuid = UUID.randomUUID();

        when(repository.deleteById(uuid)).thenReturn(false);

        boolean deleted = tagService.delete(uuid);

        assertFalse(deleted);
    }

    @Test
    void testDeleteTagEntityWithException() throws SQLException {
        UUID uuid = UUID.randomUUID();

        when(repository.deleteById(uuid)).thenThrow(new RuntimeException("Simulated Exception"));

        boolean deleted = tagService.delete(uuid);

        assertFalse(deleted);
    }

    @Test
    void testGetRepository() {
        Repository<TagEntity, UUID> serviceRepository = tagService.getRepository();

        assertNotNull(serviceRepository);
        assertEquals(repository, serviceRepository);
    }
}
