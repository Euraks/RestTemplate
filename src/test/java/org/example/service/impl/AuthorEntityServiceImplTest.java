package org.example.service.impl;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.repository.AuthorEntityRepository;
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

class AuthorEntityServiceImplTest {

    @Mock
    private AuthorEntityRepository<AuthorEntity, UUID> repository;

    @InjectMocks
    private AuthorEntityServiceImpl authorEntityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    void testSaveAuthorEntitySuccessfully() throws SQLException {
        AuthorEntity authorEntity = new AuthorEntity();

        when(repository.save(authorEntity)).thenReturn( Optional.of( authorEntity ) );

        Optional<AuthorEntity> savedEntity = authorEntityService.save(authorEntity);

        assertTrue(savedEntity.isPresent());
        assertEquals(authorEntity, savedEntity.get());
        verify(repository, times(1)).save(authorEntity);
    }


    @Test
    void testSaveAuthorEntityWithSQLException() throws SQLException {
        AuthorEntity authorEntity = new AuthorEntity();

        doThrow( new SQLException( "Simulated SQL Exception" ) ).when( repository ).save( authorEntity );

        Optional<AuthorEntity> savedEntity = authorEntityService.save( authorEntity );

        assertFalse( savedEntity.isPresent() );
        verify( repository, times( 1 ) ).save( authorEntity );
    }

    @Test
    void testFindById() {
        UUID uuid = UUID.randomUUID();
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setUuid( uuid );

        when( repository.findById( uuid ) ).thenReturn( Optional.of( authorEntity ) );

        Optional<AuthorEntity> foundEntity = authorEntityService.findById( uuid );

        assertTrue( foundEntity.isPresent() );
        assertEquals( authorEntity, foundEntity.get() );
    }

    @Test
    void testFindAllSuccessfully() throws SQLException {
        AuthorEntity authorEntity = new AuthorEntity();

        when( repository.findAll() ).thenReturn( Collections.singletonList( authorEntity ) );

        List<AuthorEntity> allEntities = authorEntityService.findAll();

        assertFalse( allEntities.isEmpty() );
        assertEquals( 1, allEntities.size() );
        assertEquals( authorEntity, allEntities.get( 0 ) );
    }

    @Test
    void testFindAllWithSQLException() throws SQLException {
        when( repository.findAll() ).thenThrow( new SQLException( "Simulated SQL Exception" ) );

        List<AuthorEntity> allEntities = authorEntityService.findAll();

        assertNotNull( allEntities );
        assertTrue( allEntities.isEmpty() );
    }

    @Test
    void testDeleteAuthorEntitySuccessfully() {
        UUID uuid = UUID.randomUUID();

        when( repository.deleteById( uuid ) ).thenReturn( true );

        boolean deleted = authorEntityService.delete( uuid );

        assertTrue( deleted );
    }

    @Test
    void testDeleteAuthorEntityNotFound() {
        UUID uuid = UUID.randomUUID();

        when( repository.deleteById( uuid ) ).thenReturn( false );

        boolean deleted = authorEntityService.delete( uuid );

        assertFalse( deleted );
    }

    @Test
    void testDeleteAuthorEntityWithException() {
        UUID uuid = UUID.randomUUID();

        when( repository.deleteById( uuid ) ).thenThrow( new RuntimeException( "Simulated Exception" ) );

        boolean deleted = authorEntityService.delete( uuid );

        assertFalse( deleted );
    }

    @Test
    void testGetRepository() {
        AuthorEntityRepository<AuthorEntity, UUID> serviceRepository = authorEntityService.getRepository();

        assertNotNull( serviceRepository );
        assertEquals( repository, serviceRepository );
    }

    @Test
    void testFindArticlesAll() {
        List<Article> articles = Collections.singletonList( new Article() );

        when( repository.findArticlesAll() ).thenReturn( articles );

        List<Article> foundArticles = authorEntityService.findArticlesAll();

        assertNotNull( foundArticles );
        assertEquals( articles, foundArticles );
    }
}
