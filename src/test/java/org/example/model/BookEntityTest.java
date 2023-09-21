package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class BookEntityTest {

    private BookEntity bookEntity;

    @BeforeEach
    public void setUp() {
        bookEntity = new BookEntity();
    }

    @Test
    void testInitialization() {
        assertNotNull( bookEntity.getUuid(), "UUID should not be null after initialization" );
    }

    @Test
    void testSetAndGetBookText() {
        String bookText = "This is a test book text";
        bookEntity.setBookText( bookText );
        assertEquals( bookText, bookEntity.getBookText(), "Set and retrieved book text should be the same" );
    }

    @Test
    void testSetAndGetTagEntities() {
        TagEntity tag1 = new TagEntity();
        TagEntity tag2 = new TagEntity();
        List<TagEntity> tags = Arrays.asList( tag1, tag2 );

        bookEntity.setTagEntities( tags );

        List<TagEntity> retrievedTags = bookEntity.getTagEntities();

        assertNotNull( retrievedTags, "Tag entities list should not be null" );
        assertEquals( 2, retrievedTags.size(), "Tag entities list size should be 2" );
        assertTrue( retrievedTags.contains( tag1 ) && retrievedTags.contains( tag2 ), "Retrieved tags should match the set tags" );
    }
}
