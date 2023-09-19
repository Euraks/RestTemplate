package org.example.servlet.mapper;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.IncomingSimplyDto;
import org.example.servlet.dto.OutGoingSimplyDto;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityAllOutGoingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SimpleDtoMapper {

    SimpleDtoMapper INSTANCE = Mappers.getMapper( SimpleDtoMapper.class );

    SimpleEntity map(IncomingSimplyDto incomingSimplyDto);

    OutGoingSimplyDto map(SimpleEntity simpleEntity);

    default SimpleEntityAllOutGoingDTO mapListToDto(List<SimpleEntity> simpleEntityList){
        SimpleEntityAllOutGoingDTO simpleEntityAllOutGoingDTO = new SimpleEntityAllOutGoingDTO();
        simpleEntityAllOutGoingDTO.setSimpleEntityList( simpleEntityList );
        return simpleEntityAllOutGoingDTO;
    }
}
