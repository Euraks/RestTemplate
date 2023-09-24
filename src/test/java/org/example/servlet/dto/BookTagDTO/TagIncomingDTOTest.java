package org.example.servlet.dto.BookTagDTO;

import org.example.model.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TagIncomingDTOTest {

    private TagIncomingDTO tagIncomingDTO;

    @BeforeEach
    void setUp() {
        tagIncomingDTO = new TagIncomingDTO();
    }

    @Test
    void testGetAndSetTagName() {
        String tagName = "TestTag";
        tagIncomingDTO.setTagName( tagName );
        String retrievedTagName = tagIncomingDTO.getTagName();
        assertNotNull( retrievedTagName );
        assertEquals( tagName, retrievedTagName );
    }

    @Test
    void testGetAndSetBookEntities() {
        List<BookEntity> bookEntities = new ArrayList<>();
        // Create some BookEntity objects and add them to the list
        BookEntity book1 = new BookEntity();
        book1.setBookText( "Book 1" );
        bookEntities.add( book1 );

        BookEntity book2 = new BookEntity();
        book2.setBookText( "Book 2" );
        bookEntities.add( book2 );

        tagIncomingDTO.setBookEntities( bookEntities );
        List<BookEntity> retrievedBookEntities = tagIncomingDTO.getBookEntities();
        assertNotNull( retrievedBookEntities );
        assertEquals( bookEntities.size(), retrievedBookEntities.size() );
        assertEquals( bookEntities, retrievedBookEntities );
    }
}
