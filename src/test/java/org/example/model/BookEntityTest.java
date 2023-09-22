package org.example.model;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BookEntityTest {

    @Test
    void testConstructor() {
        BookEntity book = new BookEntity();
        assertNotNull(book.getUuid());
    }

    @Test
    void testGettersAndSetters() {
        BookEntity book = new BookEntity();
        UUID uuid = UUID.randomUUID();
        book.setUuid(uuid);
        book.setBookText("Test book");
        book.setTagEntities(Arrays.asList(new TagEntity()));

        assertEquals(uuid, book.getUuid());
        assertEquals("Test book", book.getBookText());
        assertEquals(1, book.getTagEntities().size());
    }

    @Test
    void testEquals() {
        BookEntity book1 = new BookEntity();
        book1.setBookText("Test book");
        book1.setTagEntities(Arrays.asList(new TagEntity()));

        BookEntity book2 = new BookEntity();
        book2.setBookText("Test book");
        book2.setTagEntities(Arrays.asList(new TagEntity()));

        assertEquals(book1, book1);
        assertEquals(book1, book2);

        book2.setBookText("Different text");
        assertNotEquals(book1, book2);

        TagEntity tagEntity = new TagEntity();
        assertNotEquals( book1, tagEntity);
    }

    @Test
    void testHashCode() {
        BookEntity book1 = new BookEntity();
        book1.setBookText("Test book");
        book1.setTagEntities(Arrays.asList(new TagEntity()));

        BookEntity book2 = new BookEntity();
        book2.setBookText("Test book");
        book2.setTagEntities(Arrays.asList(new TagEntity()));

        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testToString() {
        BookEntity book = new BookEntity();
        String expectedString = "BookEntity{uuid=" + book.getUuid() + ", bookText='null', tagEntities=null}";
        assertEquals(expectedString, book.toString());
    }
}
