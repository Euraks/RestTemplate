package org.example.servlet.dto.booktagDTO;

import org.example.model.BookEntity;

import java.util.List;
import java.util.UUID;

public class TagOutGoingDTO {

    private UUID uuid;
    private String tagName;
    private List<BookEntity> bookEntities;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public List<BookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<BookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }
}
