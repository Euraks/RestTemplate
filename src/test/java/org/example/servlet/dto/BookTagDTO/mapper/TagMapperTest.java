package org.example.servlet.dto.BookTagDTO.mapper;

import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.example.servlet.dto.BookTagDTO.TagAllOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.TagIncomingDTO;
import org.example.servlet.dto.BookTagDTO.TagOutGoingDTO;
import org.example.servlet.dto.BookTagDTO.TagUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagMapperTest {

    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        tagMapper = Mappers.getMapper( TagMapper.class );
    }

    @Test
    void testMapTagEntityToTagOutGoingDTO() {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setUuid( UUID.randomUUID() );
        tagEntity.setTagName( "TestTag" );

        TagOutGoingDTO tagOutGoingDTO = tagMapper.map( tagEntity );

        assertEquals( tagEntity.getUuid(), tagOutGoingDTO.getUuid() );
        assertEquals( tagEntity.getTagName(), tagOutGoingDTO.getTagName() );
    }

    @Test
    void testMapTagUpdateDTOToTagEntity() {
        TagUpdateDTO tagUpdateDTO = new TagUpdateDTO();
        tagUpdateDTO.setUuid( UUID.randomUUID() );
        tagUpdateDTO.setTagName( "UpdatedTag" );

        TagEntity tagEntity = tagMapper.map( tagUpdateDTO );

        assertEquals( tagUpdateDTO.getUuid(), tagEntity.getUuid() );
        assertEquals( tagUpdateDTO.getTagName(), tagEntity.getTagName() );
    }

    @Test
    void testMapListToTagAllOutGoingDTO() {
        List<TagEntity> tagEntities = new ArrayList<>();
        TagEntity tag1 = new TagEntity();
        tag1.setUuid( UUID.randomUUID() );
        tag1.setTagName( "Tag1" );

        TagEntity tag2 = new TagEntity();
        tag2.setUuid( UUID.randomUUID() );
        tag2.setTagName( "Tag2" );

        tagEntities.add( tag1 );
        tagEntities.add( tag2 );

        TagAllOutGoingDTO tagAllOutGoingDTO = tagMapper.mapListToDto( tagEntities );

        assertEquals( tagEntities.size(), tagAllOutGoingDTO.getTagEntities().size() );
    }

    @Test
    void testMapTagIncomingDTOToTagEntity() {
        TagIncomingDTO tagIncomingDTO = new TagIncomingDTO();
        tagIncomingDTO.setTagName( "NewTag" );

        BookEntity bookEntity = new BookEntity();
        bookEntity.setUuid( UUID.randomUUID() );
        bookEntity.setBookText( "BookText" );

        tagIncomingDTO.setBookEntities( Collections.singletonList( bookEntity ) );

        TagEntity tagEntity = tagMapper.map( tagIncomingDTO );

        assertEquals( tagIncomingDTO.getTagName(), tagEntity.getTagName() );
        assertEquals( 1, tagEntity.getBookEntities().size() );
    }
}
