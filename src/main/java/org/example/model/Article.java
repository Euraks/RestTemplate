package org.example.model;

import java.util.Objects;
import java.util.UUID;

public class Article {

    private UUID uuid;
    private UUID authorUuid;
    private String text;

    public Article() {
    }

    public Article(String text) {
        this.text = text;
    }

    public Article(UUID uuid, String text) {
        this.uuid = uuid;
        this.text = text;
    }

    public UUID getAuthorUuid() {
        return authorUuid;
    }

    public void setAuthorUuid(UUID uuid) {
        this.authorUuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;
        return Objects.equals( getText(), article.getText() );
    }

    @Override
    public int hashCode() {
        return Objects.hash( getText() );
    }

    @Override
    public String toString() {
        return "Article{" +
                "uuid=" + uuid +
                ", author_uuid=" + authorUuid +
                ", text='" + text + '\'' +
                '}';
    }
}
