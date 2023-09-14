package org.example.model;

import java.util.List;
import java.util.UUID;

public class AuthorEntity {
    private UUID uuid;
    private String authorName;
    private List<Article> articleList;

    public AuthorEntity() {
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

    public List<Article> getInnerEntityList() {
        return articleList;
    }

    public void setInnerEntityList(List<Article> articleList) {
        this.articleList = articleList;
    }
}

