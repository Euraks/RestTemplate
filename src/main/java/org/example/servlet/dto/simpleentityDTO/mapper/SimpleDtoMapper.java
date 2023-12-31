package org.example.servlet.dto.simpleentityDTO.mapper;

import org.example.model.SimpleEntity;

import org.example.servlet.dto.simpleentityDTO.SimpleEntityAllOutGoingDTO;
import org.example.servlet.dto.simpleentityDTO.SimpleEntityIncomingDTO;
import org.example.servlet.dto.simpleentityDTO.SimpleEntityOutGoingDTO;
import org.example.servlet.dto.simpleentityDTO.SimpleEntityUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SimpleDtoMapper {

    SimpleDtoMapper INSTANCE = Mappers.getMapper( SimpleDtoMapper.class );

    SimpleEntity map(SimpleEntityIncomingDTO simpleEntityIncomingDTO);

    SimpleEntityOutGoingDTO map(SimpleEntity simpleEntity);

    SimpleEntity map(SimpleEntityUpdateDTO simpleEntityUpdateDTO);

    default SimpleEntityAllOutGoingDTO mapListToDto(List<SimpleEntity> simpleEntityList){
        SimpleEntityAllOutGoingDTO simpleEntityAllOutGoingDTO = new SimpleEntityAllOutGoingDTO();
        simpleEntityAllOutGoingDTO.setSimpleEntityList( simpleEntityList );
        return simpleEntityAllOutGoingDTO;
    }
}
