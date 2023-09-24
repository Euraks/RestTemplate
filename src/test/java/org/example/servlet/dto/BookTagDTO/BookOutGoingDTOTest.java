package org.example.servlet.dto.BookTagDTO;

import org.example.model.TagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookOutGoingDTOTest {

    private BookOutGoingDTO bookOutGoingDTO;

    @BeforeEach
    void setUp() {
        bookOutGoingDTO = new BookOutGoingDTO();
    }

    @Test
    void testGetAndSetUuid() {
        UUID uuid = UUID.randomUUID();
        bookOutGoingDTO.setUuid( uuid );
        UUID retrievedUuid = bookOutGoingDTO.getUuid();
        assertNotNull( retrievedUuid );
        assertEquals( uuid, retrievedUuid );
    }

    @Test
    void testGetAndSetBookText() {
        String bookText = "Test Book Text";
        bookOutGoingDTO.setBookText( bookText );
        String retrievedBookText = bookOutGoingDTO.getBookText();
        assertNotNull( retrievedBookText );
        assertEquals( bookText, retrievedBookText );
    }

    @Test
    void testGetAndSetTagEntities() {
        List<TagEntity> tagEntities = new ArrayList<>();
        TagEntity tag1 = new TagEntity();
        tag1.setTagName( "Tag 1" );
        tagEntities.add( tag1 );

        TagEntity tag2 = new TagEntity();
        tag2.setTagName( "Tag 2" );
        tagEntities.add( tag2 );

        bookOutGoingDTO.setTagEntities( tagEntities );
        List<TagEntity> retrievedTagEntities = bookOutGoingDTO.getTagEntities();
        assertNotNull( retrievedTagEntities );
        assertEquals( tagEntities.size(), retrievedTagEntities.size() );
        assertEquals( tagEntities, retrievedTagEntities );
    }
}
