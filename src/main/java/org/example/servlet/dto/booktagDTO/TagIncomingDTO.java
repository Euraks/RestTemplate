package org.example.servlet.dto.booktagDTO;

import org.example.model.BookEntity;

import java.util.List;

public class TagIncomingDTO {
    private String tagName;
    private List<BookEntity> bookEntities;

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
