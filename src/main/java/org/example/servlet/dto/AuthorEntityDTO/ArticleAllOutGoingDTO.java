package org.example.servlet.dto.AuthorEntityDTO;

import org.example.model.Article;

import java.util.List;

public class ArticleAllOutGoingDTO {
    List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
