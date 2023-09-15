package org.example.model;

import java.util.UUID;

public class Article {
    private UUID uuid;
    private String text;

    public Article() {
    }

    public Article(UUID uuid, String text) {
        this.uuid = uuid;
        this.text = text;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Article(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
