package org.example.servlet.dto.BookTagDTO;

import org.example.model.BookEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TagUpdateDTOTest {

    private TagUpdateDTO tagUpdateDTO;

    @BeforeEach
    void setUp() {
        tagUpdateDTO = new TagUpdateDTO();
    }

    @Test
    void testGetAndSetUuid() {
        UUID uuid = UUID.randomUUID();
        tagUpdateDTO.setUuid( uuid );
        UUID retrievedUuid = tagUpdateDTO.getUuid();
        assertNotNull( retrievedUuid );
        assertEquals( uuid, retrievedUuid );
    }

    @Test
    void testGetAndSetTagName() {
        String tagName = "TestTag";
        tagUpdateDTO.setTagName( tagName );
        String retrievedTagName = tagUpdateDTO.getTagName();
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

        tagUpdateDTO.setBookEntities( bookEntities );
        List<BookEntity> retrievedBookEntities = tagUpdateDTO.getBookEntities();
        assertNotNull( retrievedBookEntities );
        assertEquals( bookEntities.size(), retrievedBookEntities.size() );
        assertEquals( bookEntities, retrievedBookEntities );
    }
}
