package org.example.servlet.dto.AuthorEntityDTO;

import org.example.model.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthorEntityUpdateDTO {
    private UUID uuid;
    private String authorName;
    private List<Article> articleList;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<Article> getArticleList() {
        if (this.articleList == null) {
            this.articleList = new ArrayList<>();
        }
        return this.articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
