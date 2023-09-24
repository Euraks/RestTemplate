package org.example.servlet.dto.BookTagDTO;

import org.example.model.TagEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TagAllOutGoingDTOTest {

    private TagAllOutGoingDTO tagAllOutGoingDTO;

    @BeforeEach
    void setUp() {
        tagAllOutGoingDTO = new TagAllOutGoingDTO();
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

        tagAllOutGoingDTO.setTagEntities( tagEntities );
        List<TagEntity> retrievedTagEntities = tagAllOutGoingDTO.getTagEntities();
        assertNotNull( retrievedTagEntities );
        assertEquals( tagEntities.size(), retrievedTagEntities.size() );
        assertEquals( tagEntities, retrievedTagEntities );
    }
}
