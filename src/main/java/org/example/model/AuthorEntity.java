package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class AuthorEntity {
    private UUID uuid;
    private String authorName;
    private List<Article> articleList;

    public AuthorEntity() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
        if (articleList!=null){
            for (Article article:articleList) {
                article.setAuthor_uuid( uuid );
            }
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorEntity)) return false;
        AuthorEntity that = (AuthorEntity) o;
        return Objects.equals( getAuthorName(), that.getAuthorName() ) && Objects.equals( getArticleList(), that.getArticleList() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getAuthorName(), getArticleList() );
    }

    @Override
    public String toString() {
        return "AuthorEntity{" +
                "uuid=" + uuid +
                ", authorName='" + authorName + '\'' +
                ", articleList=" + articleList +
                '}';
    }
}

