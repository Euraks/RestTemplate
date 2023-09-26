package org.example.model;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorEntityTest extends Throwable {

    @Test
    void testConstructorInitializesUuid() {
        AuthorEntity author = new AuthorEntity();
        assertNotNull(author.getUuid());
    }

    @Test
    void testEqualsAndHashCode() {
        AuthorEntity author1 = new AuthorEntity();
        AuthorEntity author2 = new AuthorEntity();

        author1.setAuthorName( "Author Name" );
        author1.setArticleList( Arrays.asList( new Article() ) );

        author2.setAuthorName( "Author Name" );
        author2.setArticleList( Arrays.asList( new Article() ) );

        assertTrue( author1.equals( author2 ) && author2.equals( author1 ) );
        assertTrue( author1.getAuthorName().equals( author2.getAuthorName() ) && author2.getArticleList().equals( author1.getArticleList() ) );
        assertEquals( author1.hashCode(), author2.hashCode() );

        Article article = new Article();
        assertNotEquals( author1, article );

        assertEquals( author1, author1 );
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

    @Test
    void equalsWithSameValues() {
        AuthorEntity author1 = new AuthorEntity();
        author1.setAuthorName("John Doe");
        author1.setArticleList(Arrays.asList(new Article(), new Article()));

        AuthorEntity author2 = new AuthorEntity();
        author2.setAuthorName("John Doe");
        author2.setArticleList(Arrays.asList(new Article(), new Article()));

        assertTrue(author1.equals(author2) && author2.equals(author1));
    }

    @Test
    void equalsWithDifferentAuthorNames() {
        AuthorEntity author1 = new AuthorEntity();
        author1.setAuthorName("John Doe");
        author1.setArticleList(Arrays.asList(new Article(), new Article()));

        AuthorEntity author2 = new AuthorEntity();
        author2.setAuthorName("Jane Smith");
        author2.setArticleList(Arrays.asList(new Article(), new Article()));

        assertFalse(author1.equals(author2) || author2.equals(author1));
    }

    @Test
    void equalsWithDifferentArticleLists() {
        AuthorEntity author1 = new AuthorEntity();
        author1.setAuthorName("John Doe");
        author1.setArticleList(Arrays.asList(new Article()));

        AuthorEntity author2 = new AuthorEntity();
        author2.setAuthorName("John Doe");
        author2.setArticleList(Arrays.asList(new Article(), new Article()));

        assertFalse(author1.equals(author2) || author2.equals(author1));
    }

    @Test
    void notEqualsWithNullOrDifferentType() {
        AuthorEntity author = new AuthorEntity();
        author.setAuthorName("John Doe");
        author.setArticleList(Arrays.asList(new Article()));

        assertNotEquals( null, author );
        assertNotEquals( "a string", author );
    }

    @Test
    void testToString() {
        AuthorEntity author = new AuthorEntity();
        UUID expectedUuid = UUID.randomUUID();
        author.setUuid(expectedUuid);
        author.setAuthorName("John Doe");
        author.setArticleList(Arrays.asList(new Article(), new Article()));

        String expectedString = "AuthorEntity{" +
                "uuid=" + expectedUuid +
                ", authorName='John Doe'" +
                ", articleList=" + author.getArticleList() +
                '}';

        assertEquals(expectedString, author.toString());
    }
}

