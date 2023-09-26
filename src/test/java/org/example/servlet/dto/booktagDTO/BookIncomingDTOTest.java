package org.example.servlet.dto.booktagDTO;

import org.example.model.TagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookIncomingDTOTest {

    private BookIncomingDTO bookIncomingDTO;

    @BeforeEach
    void setUp() {
        bookIncomingDTO = new BookIncomingDTO();
    }

    @Test
    void testGetAndSetBookText() {
        String bookText = "Test Book Text";
        bookIncomingDTO.setBookText( bookText );
        String retrievedBookText = bookIncomingDTO.getBookText();
        assertNotNull( retrievedBookText );
        assertEquals( bookText, retrievedBookText );
    }

    @Test
    void testGetAndSetTagEntities() {
        List<TagEntity> tagEntities = new ArrayList<>();
        // Create some TagEntity objects and add them to the list
        TagEntity tag1 = new TagEntity();
        tag1.setTagName( "Tag 1" );
        tagEntities.add( tag1 );

        TagEntity tag2 = new TagEntity();
        tag2.setTagName( "Tag 2" );
        tagEntities.add( tag2 );

        bookIncomingDTO.setTagEntities( tagEntities );
        List<TagEntity> retrievedTagEntities = bookIncomingDTO.getTagEntities();
        assertNotNull( retrievedTagEntities );
        assertEquals( tagEntities.size(), retrievedTagEntities.size() );
        assertEquals( tagEntities, retrievedTagEntities );
    }
}
