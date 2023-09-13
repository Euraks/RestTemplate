package org.example.repository.mapper;

import org.example.model.SimpleEntity;
import org.mapstruct.Mapper;

import java.sql.ResultSet;


public interface SimpleResultSetMapper {
    SimpleEntity map(ResultSet resultSet);
}
