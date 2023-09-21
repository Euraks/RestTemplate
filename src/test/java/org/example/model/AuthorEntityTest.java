package org.example.model;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorEntityTest {

    @Test
    void testConstructorInitializesUuid() {
        AuthorEntity author = new AuthorEntity();
        assertNotNull(author.getUuid());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthorEntity author1 = new AuthorEntity();
        AuthorEntity author2 = new AuthorEntity();

        author1.setAuthorName("Author Name");
        author1.setArticleList(Arrays.asList(new Article()));

        author2.setAuthorName("Author Name");
        author2.setArticleList(Arrays.asList(new Article()));

        assertTrue(author1.equals(author2) && author2.equals(author1));
        assertEquals(author1.hashCode(), author2.hashCode());
    }

    @Test
    void testGettersAndSetters() {
        AuthorEntity author = new AuthorEntity();
        UUID uuid = UUID.randomUUID();
        author.setUuid(uuid);
        author.setAuthorName("Test Author");
        author.setArticleList(Arrays.asList(new Article()));

        assertEquals(uuid, author.getUuid());
        assertEquals("Test Author", author.getAuthorName());
        assertEquals(1, author.getArticleList().size());
    }
}
