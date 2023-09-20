package org.example.servlet.dto.BookTagDTO.mapper;

import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.servlet.dto.BookTagDTO.BookAllOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.BookIncomingDTO;
import org.example.servlet.dto.BookTagDTO.BookOutGoingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public interface BookMapper {

    BookMapper INSTANCE = Mappers.getMapper( BookMapper.class );

    BookOutGoingDTO map(BookEntity bookEntity);

    default BookAllOutGoingDTO mapListToDto(List<BookEntity> bookEntities) {
        BookAllOutGoingDTO bookAllOutGoingDTO = new BookAllOutGoingDTO();
        bookAllOutGoingDTO.setBookEntities( bookEntities );
        return bookAllOutGoingDTO;
    }

    default BookEntity map(BookIncomingDTO bookIncomingDTO) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setBookText( bookIncomingDTO.getBookText() );
        List<TagEntity> tagEntities = new ArrayList<>();
        for (TagEntity tagEntity : bookIncomingDTO.getTagEntities()) {
            if (tagEntity.getUuid() == null) {
                tagEntity.setUuid( UUID.randomUUID() );
            }
            tagEntities.add( tagEntity );
        }
        bookEntity.setTagEntities( tagEntities );
        return bookEntity;
    }
}
