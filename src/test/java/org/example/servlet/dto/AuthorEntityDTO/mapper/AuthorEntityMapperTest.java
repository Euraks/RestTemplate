package org.example.servlet.dto.AuthorEntityDTO.mapper;

import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityIncomingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthorEntityMapperTest {

    private AuthorEntityMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = AuthorEntityMapper.INSTANCE;
    }

    @Test
    void testMapFromEntityToOutGoingDTO() {
        AuthorEntity entity = new AuthorEntity();
        entity.setAuthorName( "John Doe" );
        Article article = new Article( "Test Article" );
        entity.getArticleList().add( article );

        AuthorEntityOutGoingDTO dto = mapper.map( entity );

        assertEquals( "John Doe", dto.getAuthorName() );
        String articleText = dto.getArticleList().get( 0 ).getText();
        assertEquals( "Test Article", articleText );
    }

    @Test
    void testMapFromUpdateDTOToEntity() {
        AuthorEntityUpdateDTO dto = new AuthorEntityUpdateDTO();
        dto.setAuthorName( "John Doe" );
        Article article = new Article( "Test Article" );
        dto.getArticleList().add( article );

        AuthorEntity entity = mapper.map( dto );

        assertEquals( "John Doe", entity.getAuthorName() );
        String articleText = dto.getArticleList().get( 0 ).getText();
        assertEquals( "Test Article", articleText );
    }

    @Test
    void testMapListToDto() {
        AuthorEntity entity1 = new AuthorEntity();
        AuthorEntity entity2 = new AuthorEntity();

        AuthorEntityAllOutGoingDTO allOutGoingDTO = mapper.mapListToDto( Arrays.asList( entity1, entity2 ) );

        assertEquals( 2, allOutGoingDTO.getAuthorEntityList().size() );
    }

    @Test
    void testMapFromIncomingDTOToEntity() {
        AuthorEntityIncomingDTO dto = new AuthorEntityIncomingDTO();
        dto.setAuthorName( "John Doe" );
        Article article = new Article();
        dto.setArticleList( Arrays.asList( article ) );

        AuthorEntity entity = mapper.map( dto );

        assertEquals( "John Doe", entity.getAuthorName() );
        assertNotNull( entity.getArticleList() );
        assertEquals( 1, entity.getArticleList().size() );
        assertNotNull( entity.getArticleList().get( 0 ).getUuid() );
    }
}
