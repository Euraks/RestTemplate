package org.example.repository.mapper;

import org.example.model.TagEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Mapper
public interface TagResultSetMapper {

    TagResultSetMapper INSTANCE = Mappers.getMapper( TagResultSetMapper.class );

    default TagEntity map(ResultSet resultSet) throws SQLException {
        TagEntity tagEntity = new TagEntity();
        tagEntity.setUuid((UUID) resultSet.getObject("uuid"));
        tagEntity.setTagName(resultSet.getString("tagName"));
        return tagEntity;
    }
}
