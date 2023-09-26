package org.example.servlet.dto.booktagDTO;

import org.example.model.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookAllOutGoingDTOTest {

    private BookAllOutGoingDTO bookAllOutGoingDTO;

    @BeforeEach
    void setUp() {
        bookAllOutGoingDTO = new BookAllOutGoingDTO();
    }

    @Test
    void testGetAndSetBookEntities() {
        List<BookEntity> bookEntities = new ArrayList<>();
        BookEntity book1 = new BookEntity();
        book1.setBookText( "Book 1" );
        bookEntities.add( book1 );

        BookEntity book2 = new BookEntity();
        book2.setBookText( "Book 2" );
        bookEntities.add( book2 );

        bookAllOutGoingDTO.setBookEntities( bookEntities );
        List<BookEntity> retrievedBookEntities = bookAllOutGoingDTO.getBookEntities();
        assertNotNull( retrievedBookEntities );
        assertEquals( bookEntities.size(), retrievedBookEntities.size() );
        assertEquals( bookEntities, retrievedBookEntities );
    }
}
