package org.example.servlet.dto.AuthorEntityDTO.mapper;


import org.example.model.AuthorEntity;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityAllOutGoingDTO;
import org.example.servlet.dto.AuthorEntityDTO.AuthorEntityIncomingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AuthorEntityMapper {

    AuthorEntityMapper INSTANCE = Mappers.getMapper( AuthorEntityMapper.class );



    default AuthorEntityAllOutGoingDTO mapListToDto(List<AuthorEntity> simpleEntityList) {
        AuthorEntityAllOutGoingDTO authorEntityAllOutGoingDTO = new AuthorEntityAllOutGoingDTO();
        authorEntityAllOutGoingDTO.setAuthorEntityList( simpleEntityList );
        return authorEntityAllOutGoingDTO;
    }

    default AuthorEntity map(AuthorEntityIncomingDTO authorEntityIncomingDTO){
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setAuthorName( authorEntityIncomingDTO.getAuthorName() );
        authorEntity.setArticleList( authorEntityIncomingDTO.getArticleList() );
        return authorEntity;
    }


}
