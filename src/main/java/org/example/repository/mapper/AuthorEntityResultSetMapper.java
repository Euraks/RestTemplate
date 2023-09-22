package org.example.repository.mapper;

import org.example.model.AuthorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Mapper
public interface AuthorEntityResultSetMapper {

    AuthorEntityResultSetMapper INSTANCE = Mappers.getMapper( AuthorEntityResultSetMapper.class );


    default AuthorEntity map(ResultSet resultSet) throws SQLException {
        AuthorEntity authorEntity = new AuthorEntity();
        authorEntity.setUuid( (UUID) resultSet.getObject( "uuid" ) );
        authorEntity.setAuthorName( resultSet.getString( "authorName" ) );
        return authorEntity;
    }
}
