package org.example.servlet.dto.authorentityDTO.mapper;

import org.example.model.Article;
import org.example.servlet.dto.authorentityDTO.ArticleAllOutGoingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper( ArticleMapper.class );

    default ArticleAllOutGoingDTO mapListToDto(List<Article> articles) {
        ArticleAllOutGoingDTO articleAllOutGoingDTO = new ArticleAllOutGoingDTO();
        articleAllOutGoingDTO.setArticles( articles );
        return articleAllOutGoingDTO;
    }
}
