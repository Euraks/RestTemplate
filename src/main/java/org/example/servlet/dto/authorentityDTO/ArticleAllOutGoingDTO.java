package org.example.servlet.dto.authorentityDTO;

import org.example.model.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleAllOutGoingDTO {

    List<Article> articles;

    public List<Article> getArticles() {
        if (this.articles == null) {
            this.articles = new ArrayList<>();
        }
        return this.articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
