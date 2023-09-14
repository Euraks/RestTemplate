package org.example.repository.mapper;

import org.example.model.SimpleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;


@Mapper
public interface SimpleResultSetMapper {

    SimpleResultSetMapper INSTANCE = Mappers.getMapper( SimpleResultSetMapper.class );


    default SimpleEntity map(ResultSet resultSet) throws SQLException {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setUuid( resultSet.getObject( 1, UUID.class ) );
        simpleEntity.setDescription( resultSet.getString( "description" ) );
        return simpleEntity;
    }

}
