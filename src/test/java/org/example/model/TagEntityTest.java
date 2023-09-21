package org.example.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TagEntityTest {

    private TagEntity tagEntity;

    @BeforeEach
    public void setUp() {
        tagEntity = new TagEntity();
    }

    @Test
    void testDefaultUuidNotNull() {
        assertNotNull(tagEntity.getUuid());
    }

    @Test
    void testSetAndGetUuid() {
        UUID uuid = UUID.randomUUID();
        tagEntity.setUuid(uuid);
        assertEquals(uuid, tagEntity.getUuid());
    }

    @Test
    void testSetAndGetTagName() {
        tagEntity.setTagName("Test");
        assertEquals("Test", tagEntity.getTagName());
    }

    @Test
    void testSetAndGetBooks() {
        BookEntity book1 = new BookEntity();
        BookEntity book2 = new BookEntity();

        tagEntity.setBookEntities(Arrays.asList(book1, book2));

        assertEquals(2, tagEntity.getBookEntities().size());
        assertTrue(tagEntity.getBookEntities().contains(book1));
        assertTrue(tagEntity.getBookEntities().contains(book2));
    }

    @Test
    void testEquals() {
        TagEntity tag1 = new TagEntity();
        TagEntity tag2 = new TagEntity();
        TagEntity tag3 = new TagEntity();

        tag1.setTagName("Test");
        tag2.setTagName("Test");
        tag3.setTagName("Another");

        assertEquals(tag1, tag2);
        assertNotEquals(tag1, tag3);
    }

    @Test
    void testHashCode() {
        TagEntity tag1 = new TagEntity();
        TagEntity tag2 = new TagEntity();

        tag1.setTagName("Test");
        tag2.setTagName("Test");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    void testToString() {
        tagEntity.setTagName("Test");
        assertTrue(tagEntity.toString().contains("Test"));
    }
}
