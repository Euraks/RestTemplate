package org.example.servlet.dto.booktagDTO;

import org.example.model.TagEntity;

import java.util.List;

public class BookIncomingDTO {
    private String bookText;
    private List<TagEntity> tagEntities;

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
