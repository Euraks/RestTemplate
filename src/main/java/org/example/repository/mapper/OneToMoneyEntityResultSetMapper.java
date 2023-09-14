package org.example.repository.mapper;

import org.example.model.OneToManyEntity;
import org.mapstruct.Mapper;

import java.sql.ResultSet;

@Mapper
public interface OneToMoneyEntityResultSetMapper {
    OneToManyEntity map(ResultSet resultSet);
}
