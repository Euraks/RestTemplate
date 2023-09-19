package org.example.servlet.dto.AuthorEntityDTO;

import org.example.model.Article;

import java.util.List;
import java.util.UUID;

public class AuthorEntityIncomingDTO {

    private String authorName;
    private List<Article> articleList;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
