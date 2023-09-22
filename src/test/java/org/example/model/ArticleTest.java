package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    void testArticleCreation() {
        Article article = new Article();
        assertNull( article.getUuid() );
        assertNull( article.getText() );

        UUID uuid = UUID.randomUUID();
        String text = "Sample text";
        article = new Article( uuid, text );
        assertEquals( uuid, article.getUuid() );
        assertEquals( text, article.getText() );

        article = new Article( text );
        assertNull( article.getUuid() );
        assertEquals( text, article.getText() );

        article = new Article( uuid, text );
        assertEquals( uuid, article.getUuid() );
        assertEquals( text, article.getText() );
    }

    @Test
    void testSettersAndGetters() {
        Article article = new Article();
        UUID uuid = UUID.randomUUID();
        UUID authorUuid = UUID.randomUUID();
        String text = "Another sample text";

        article.setUuid( uuid );
        article.setAuthor_uuid( authorUuid );
        article.setText( text );

        assertEquals( uuid, article.getUuid() );
        assertEquals( authorUuid, article.getAuthor_uuid() );
        assertEquals( text, article.getText() );
    }

    @Test
    void testEqualsAndHashCode() {
        String text = "Yet another sample text";
        Article article1 = new Article( text );
        Article article2 = new Article( text );
        Article article3 = new Article( "Different text" );

        assertEquals( article1, article1 );

        assertEquals( article1, article2 );
        assertNotEquals( article1, article3 );
        assertNotEquals( article2, article3 );

        AuthorEntity author = new AuthorEntity();
        assertNotEquals( article2, author );

        assertEquals( article1.hashCode(), article2.hashCode() );
        assertNotEquals( article1.hashCode(), article3.hashCode() );
    }

    @Test
    void testToString() {
        UUID uuid = UUID.randomUUID();
        UUID authorUuid = UUID.randomUUID();
        String text = "Sample text for toString";
        Article article = new Article( uuid, text );
        article.setAuthor_uuid( authorUuid );

        String expectedString = "Article{" +
                "uuid=" + uuid +
                ", author_uuid=" + authorUuid +
                ", text='" + text + '\'' +
                '}';
        assertEquals( expectedString, article.toString() );
    }
}
