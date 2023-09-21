package org.example.model;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArticleTest {

    @Test
    void testEmptyConstructor() {
        Article article = new Article();
        assertNull(article.getUuid());
        assertNull(article.getAuthor_uuid());
        assertNull(article.getText());
    }

    @Test
    void testConstructorWithUuidAndText() {
        UUID uuid = UUID.randomUUID();
        String text = "Sample Text";
        Article article = new Article(uuid, text);

        assertEquals(uuid, article.getUuid());
        assertNull(article.getAuthor_uuid());
        assertEquals(text, article.getText());
    }

    @Test
    void testConstructorWithText() {
        String text = "Sample Text";
        Article article = new Article(text);

        assertNull(article.getUuid());
        assertNull(article.getAuthor_uuid());
        assertEquals(text, article.getText());
    }

    @Test
    void testSettersAndGetters() {
        Article article = new Article();

        UUID uuid = UUID.randomUUID();
        UUID authorUuid = UUID.randomUUID();
        String text = "New Sample Text";

        article.setUuid(uuid);
        article.setAuthor_uuid(authorUuid);
        article.setText(text);

        assertEquals(uuid, article.getUuid());
        assertEquals(authorUuid, article.getAuthor_uuid());
        assertEquals(text, article.getText());
    }
}
