package org.example.repository.mapper;

import org.example.model.BookEntity;
import org.example.model.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Mapper
public interface BookResultSetMapper {

    BookResultSetMapper INSTANCE = Mappers.getMapper( BookResultSetMapper.class );

    default BookEntity map(ResultSet resultSet) throws SQLException {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setUuid( (UUID) resultSet.getObject( "uuid" ) );
        bookEntity.setBookText( resultSet.getString( "bookText" ) );
        return bookEntity;
    }
}
