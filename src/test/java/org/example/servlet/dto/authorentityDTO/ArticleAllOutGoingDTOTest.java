package org.example.servlet.dto.authorentityDTO;

import org.example.model.Article;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArticleAllOutGoingDTOTest {

    private ArticleAllOutGoingDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new ArticleAllOutGoingDTO();
    }

    @Test
    void testGetArticlesReturnsEmptyListWhenNotSet() {
        List<Article> articles = dto.getArticles();
        assertNotNull(articles, "Should return an empty list instead of null");
        assertTrue(articles.isEmpty(), "List should be empty");
    }

    @Test
    void testSetAndGetArticles() {
        Article article1 = new Article();
        article1.setText("Article 1");
        Article article2 = new Article();
        article2.setText("Article 2");

        dto.setArticles(Arrays.asList(article1, article2));

        List<Article> retrievedArticles = dto.getArticles();

        assertEquals(2, retrievedArticles.size(), "There should be 2 articles");
        assertEquals("Article 1", retrievedArticles.get(0).getText());
        assertEquals("Article 2", retrievedArticles.get(1).getText());
    }
}
