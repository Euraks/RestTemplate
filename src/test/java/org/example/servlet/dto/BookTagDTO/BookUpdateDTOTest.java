package org.example.servlet.dto.BookTagDTO;

import org.example.model.TagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BookUpdateDTOTest {

    private BookUpdateDTO bookUpdateDTO;

    @BeforeEach
    void setUp() {
        bookUpdateDTO = new BookUpdateDTO();
    }

    @Test
    void testGetAndSetUuid() {
        UUID uuid = UUID.randomUUID();
        bookUpdateDTO.setUuid( uuid );
        UUID retrievedUuid = bookUpdateDTO.getUuid();
        assertNotNull( retrievedUuid );
        assertEquals( uuid, retrievedUuid );
    }

    @Test
    void testGetAndSetBookText() {
        String bookText = "Test Book Text";
        bookUpdateDTO.setBookText( bookText );
        String retrievedBookText = bookUpdateDTO.getBookText();
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

        bookUpdateDTO.setTagEntities( tagEntities );
        List<TagEntity> retrievedTagEntities = bookUpdateDTO.getTagEntities();
        assertNotNull( retrievedTagEntities );
        assertEquals( tagEntities.size(), retrievedTagEntities.size() );
        assertEquals( tagEntities, retrievedTagEntities );
    }
}
