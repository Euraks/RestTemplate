package org.example.servlet.dto.booktagDTO;

import org.example.model.BookEntity;

import java.util.List;

public class BookAllOutGoingDTO {

    List<BookEntity> bookEntities;

    public List<BookEntity> getBookEntities() {
        return bookEntities;
    }

    public void setBookEntities(List<BookEntity> bookEntities) {
        this.bookEntities = bookEntities;
    }
}
