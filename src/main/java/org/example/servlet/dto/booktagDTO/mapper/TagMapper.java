package org.example.servlet.dto.booktagDTO.mapper;

import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.servlet.dto.booktagDTO.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper( TagMapper.class );

    TagOutGoingDTO map(TagEntity tagEntity);

    TagEntity map(TagUpdateDTO tagUpdateDTO);

    default TagAllOutGoingDTO mapListToDto(List<TagEntity> tagEntities) {
        TagAllOutGoingDTO tagAllOutGoingDTO = new TagAllOutGoingDTO();
        tagAllOutGoingDTO.setTagEntities( tagEntities );
        return tagAllOutGoingDTO;
    }

    default TagEntity map(TagIncomingDTO tagIncomingDTO) {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setTagName( tagIncomingDTO.getTagName() );
        List<BookEntity> bookEntities = new ArrayList<>();
        for (BookEntity book : tagIncomingDTO.getBookEntities()) {
            if (book.getUuid() == null) {
                book.setUuid( UUID.randomUUID() );
            }
            bookEntities.add( book);
        }
        tagEntity.setBookEntities(  bookEntities );
        return tagEntity;
    }
}

