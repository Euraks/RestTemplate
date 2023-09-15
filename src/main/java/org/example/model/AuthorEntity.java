package org.example.model;

import java.util.List;
import java.util.UUID;


public class AuthorEntity {
    private UUID uuid;
    private String authorName;
    private List<Article> articleList;

    public AuthorEntity() {
    }

    public AuthorEntity(UUID randomUUID, String authorName, List<Article> articleList) {
        this.uuid = randomUUID;
        this.authorName = authorName;
        this.articleList = articleList;
    }

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
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}

