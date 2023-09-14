package org.example.repository.mapper;

import org.example.model.AuthorEntity;
import org.mapstruct.Mapper;

import java.sql.ResultSet;

@Mapper
public interface AuthorEntityResultSetMapper {
    AuthorEntity map(ResultSet resultSet);
}
