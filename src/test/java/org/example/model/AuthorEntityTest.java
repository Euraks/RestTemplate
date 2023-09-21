package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthorEntityTest {

    private AuthorEntity authorEntity;

    @BeforeEach
    public void setUp() {
        authorEntity = new AuthorEntity();
    }

    @Test
    void testInitialization() {
        assertNotNull(authorEntity.getUuid(), "UUID should not be null after initialization");
    }

    @Test
    void testSetAndGetUuid() {
        UUID newUuid = UUID.randomUUID();
        authorEntity.setUuid(newUuid);

        assertEquals(newUuid, authorEntity.getUuid(), "UUID should match the set value");
    }

    @Test
    void testSetAndGetAuthorName() {
        String name = "Steve Wozniak";
        authorEntity.setAuthorName(name);

        assertEquals(name, authorEntity.getAuthorName(), "Author name should match the set value");
    }

    @Test
    void testSetAndGetArticleList() {
        Article article1 = new Article(); // предположим, у вас есть конструктор по умолчанию
        Article article2 = new Article();

        List<Article> articles = Arrays.asList(article1, article2);
        authorEntity.setArticleList(articles);

        assertEquals(2, authorEntity.getArticleList().size(), "Article list should contain 2 articles");
        assertTrue(authorEntity.getArticleList().containsAll(articles), "Article list should match the set value");
    }
}
