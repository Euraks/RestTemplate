package org.example.servlet.dto.AuthorEntityDTO.mapper;


import org.example.model.Article;
import org.example.model.AuthorEntity;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityIncomingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mapper
public interface AuthorEntityMapper {

    AuthorEntityMapper INSTANCE = Mappers.getMapper( AuthorEntityMapper.class );

    AuthorEntityOutGoingDTO map(AuthorEntity authorEntity);

    AuthorEntity map(AuthorEntityUpdateDTO authorEntityUpdateDTO);

    default AuthorEntityAllOutGoingDTO mapListToDto(List<AuthorEntity> simpleEntityList) {
        AuthorEntityAllOutGoingDTO authorEntityAllOutGoingDTO = new AuthorEntityAllOutGoingDTO();
        authorEntityAllOutGoingDTO.setAuthorEntityList( simpleEntityList );
        return authorEntityAllOutGoingDTO;
    }

    default AuthorEntity map(AuthorEntityIncomingDTO authorEntityIncomingDTO) {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setAuthorName( authorEntityIncomingDTO.getAuthorName() );
        List<Article> articles = new ArrayList<>();
        for (Article article : authorEntityIncomingDTO.getArticleList()) {
            if (article.getUuid()==null){
                article.setUuid( UUID.randomUUID() );
            }
            article.setAuthorUuid( authorEntity.getUuid() );
            articles.add( article );
        }
        authorEntity.setArticleList( articles );
        return authorEntity;
    }


}
