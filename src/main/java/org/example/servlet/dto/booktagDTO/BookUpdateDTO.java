package org.example.servlet.dto.booktagDTO;

import org.example.model.TagEntity;

import java.util.List;
import java.util.UUID;

public class BookUpdateDTO {

    private UUID uuid;
    private String bookText;
    private List<TagEntity> tagEntities;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getBookText() {
        return bookText;
    }

    public void setBookText(String bookText) {
        this.bookText = bookText;
    }

    public List<TagEntity> getTagEntities() {
        return tagEntities;
    }

    public void setTagEntities(List<TagEntity> tagEntities) {
        this.tagEntities = tagEntities;
    }
}
