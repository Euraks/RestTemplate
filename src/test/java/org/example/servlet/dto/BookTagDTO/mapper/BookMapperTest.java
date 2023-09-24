package org.example.servlet.dto.BookTagDTO.mapper;

import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.servlet.dto.BookTagDTO.BookAllOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.BookIncomingDTO;
import org.example.servlet.dto.BookTagDTO.BookOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.BookUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BookMapperTest {

    private BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        bookMapper = Mappers.getMapper(BookMapper.class);
    }

    @Test
    void testMapBookEntityToBookOutGoingDTO() {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setUuid(UUID.randomUUID());
        bookEntity.setBookText("Test Book Text");

        BookOutGoingDTO bookOutGoingDTO = bookMapper.map(bookEntity);

        assertEquals(bookEntity.getUuid(), bookOutGoingDTO.getUuid());
        assertEquals(bookEntity.getBookText(), bookOutGoingDTO.getBookText());
    }

    @Test
    void testMapBookUpdateDTOToBookEntity() {
        BookUpdateDTO bookUpdateDTO = new BookUpdateDTO();
        bookUpdateDTO.setUuid(UUID.randomUUID());
        bookUpdateDTO.setBookText("Updated Book Text");

        BookEntity bookEntity = bookMapper.map(bookUpdateDTO);

        assertEquals(bookUpdateDTO.getUuid(), bookEntity.getUuid());
        assertEquals(bookUpdateDTO.getBookText(), bookEntity.getBookText());
    }

    @Test
    void testMapListToBookAllOutGoingDTO() {
        List<BookEntity> bookEntities = new ArrayList<>();
        BookEntity book1 = new BookEntity();
        book1.setUuid(UUID.randomUUID());
        book1.setBookText("Book 1");

        BookEntity book2 = new BookEntity();
        book2.setUuid( UUID.randomUUID());
        book2.setBookText("Book 2");

        bookEntities.add(book1);
        bookEntities.add(book2);

        BookAllOutGoingDTO bookAllOutGoingDTO = bookMapper.mapListToDto(bookEntities);

        assertEquals(bookEntities.size(), bookAllOutGoingDTO.getBookEntities().size());
    }

    @Test
    void testMapBookIncomingDTOToBookEntity() {
        BookIncomingDTO bookIncomingDTO = new BookIncomingDTO();
        bookIncomingDTO.setBookText("New Book Text");

        TagEntity tagEntity = new TagEntity();
        tagEntity.setUuid(UUID.randomUUID());
        tagEntity.setTagName("Tag");

        bookIncomingDTO.setTagEntities( Collections.singletonList(tagEntity));

        BookEntity bookEntity = bookMapper.map(bookIncomingDTO);

        assertEquals(bookIncomingDTO.getBookText(), bookEntity.getBookText());
        assertEquals(1, bookEntity.getTagEntities().size());
    }
}
