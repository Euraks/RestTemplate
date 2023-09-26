package org.example.service.impl;

import org.example.model.BookEntity;
import org.example.repository.Repository;
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

class BookServiceImplTest {

    @Mock
    private Repository<BookEntity, UUID> repository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks( this );
    }

    @Test
    void testSaveBookEntitySuccessfully() throws SQLException {
        BookEntity bookEntity = new BookEntity();

        when( repository.save( bookEntity ) ).thenReturn( Optional.of( bookEntity ) );

        Optional<BookEntity> savedEntity = bookService.save( bookEntity );

        assertTrue( savedEntity.isPresent() );
        assertEquals( bookEntity, savedEntity.get() );
        verify( repository, times( 1 ) ).save( bookEntity );
    }

    @Test
    void testSaveBookEntityWithSQLException() throws SQLException {
        BookEntity bookEntity = new BookEntity();

        doThrow( new SQLException( "Simulated SQL Exception" ) ).when( repository ).save( bookEntity );

        Optional<BookEntity> savedEntity = bookService.save( bookEntity );

        assertFalse( savedEntity.isPresent() );
        verify( repository, times( 1 ) ).save( bookEntity );
    }

    @Test
    void testFindById() throws SQLException {
        UUID uuid = UUID.randomUUID();
        BookEntity bookEntity = new BookEntity();
        bookEntity.setUuid( uuid );

        when( repository.findById( uuid ) ).thenReturn( Optional.of( bookEntity ) );

        Optional<BookEntity> foundEntity = bookService.findById( uuid );

        assertTrue( foundEntity.isPresent() );
        assertEquals( bookEntity, foundEntity.get() );
    }

    @Test
    void testFindAllSuccessfully() throws SQLException {
        BookEntity bookEntity = new BookEntity();

        when( repository.findAll() ).thenReturn( Collections.singletonList( bookEntity ) );

        List<BookEntity> allEntities = bookService.findAll();

        assertFalse( allEntities.isEmpty() );
        assertEquals( 1, allEntities.size() );
        assertEquals( bookEntity, allEntities.get( 0 ) );
    }

    @Test
    void testFindAllWithSQLException() throws SQLException {
        when( repository.findAll() ).thenThrow( new SQLException( "Simulated SQL Exception" ) );

        List<BookEntity> allEntities = bookService.findAll();

        assertNotNull( allEntities );
        assertTrue( allEntities.isEmpty() );
    }

    @Test
    void testDeleteBookEntitySuccessfully() throws SQLException {
        UUID uuid = UUID.randomUUID();

        when( repository.deleteById( uuid ) ).thenReturn( true );

        boolean deleted = bookService.delete( uuid );

        assertTrue( deleted );
    }

    @Test
    void testDeleteBookEntityNotFound() throws SQLException {
        UUID uuid = UUID.randomUUID();

        when( repository.deleteById( uuid ) ).thenReturn( false );

        boolean deleted = bookService.delete( uuid );

        assertFalse( deleted );
    }

    @Test
    void testDeleteBookEntityWithException() throws SQLException {
        UUID uuid = UUID.randomUUID();

        when(repository.deleteById(uuid)).thenReturn(false);

        boolean deleted = bookService.delete(uuid);

        assertFalse(deleted);
    }

    @Test
    void testGetRepository() {
        Repository<BookEntity, UUID> serviceRepository = bookService.getRepository();

        assertNotNull( serviceRepository );
        assertEquals( repository, serviceRepository );
    }
}
