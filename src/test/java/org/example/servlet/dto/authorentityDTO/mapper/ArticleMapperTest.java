package org.example.servlet.dto.authorentityDTO.mapper;

import org.example.model.Article;
import org.example.servlet.dto.authorentityDTO.ArticleAllOutGoingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleMapperTest {

    private ArticleMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = ArticleMapper.INSTANCE;
    }

    @Test
    void testMapListToDto() {
        Article article1 = new Article();
        article1.setText("Article 1");
        Article article2 = new Article();
        article2.setText("Article 2");

        ArticleAllOutGoingDTO allOutGoingDTO = mapper.mapListToDto(Arrays.asList(article1, article2));

        assertEquals(2, allOutGoingDTO.getArticles().size());
        assertEquals("Article 1", allOutGoingDTO.getArticles().get(0).getText());
        assertEquals("Article 2", allOutGoingDTO.getArticles().get(1).getText());
    }
}
