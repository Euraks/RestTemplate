package org.example.repository.mapper;

import org.example.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Mapper
public interface ArticleResultSetMapper {

    ArticleResultSetMapper INSTANCE = Mappers.getMapper( ArticleResultSetMapper.class );

    default Article map(ResultSet resultSet) throws SQLException {
        Article article = new Article();
        article.setUuid( (UUID) resultSet.getObject( "uuid" ) );
        article.setAuthorUuid( (UUID) resultSet.getObject( "author_id" ) );
        article.setText( resultSet.getString( "text" ) );
        return article;
    }
}
