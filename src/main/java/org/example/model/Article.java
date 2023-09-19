package org.example.model;

import java.util.UUID;

public class Article {
    private UUID uuid;
    private UUID author_uuid;
    private String text;

    public Article() {
    }

    public Article(UUID uuid, String text) {
        this.uuid = uuid;
        this.text = text;
    }

    public UUID getAuthor_uuid() {
        return author_uuid;
    }

    public void setAuthor_uuid(UUID author_uuid) {
        this.author_uuid = author_uuid;
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
