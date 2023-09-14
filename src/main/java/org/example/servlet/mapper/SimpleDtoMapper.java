package org.example.servlet.mapper;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.IncomingSimplyDto;
import org.example.servlet.dto.OutGoingSimplyDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SimpleDtoMapper {

    SimpleDtoMapper INSTANCE = Mappers.getMapper( SimpleDtoMapper.class );

    SimpleEntity map(IncomingSimplyDto incomingSimplyDto);

    OutGoingSimplyDto map(SimpleEntity simpleEntity);

    default OutGoingSimplyDto mapListToDto(List<SimpleEntity> simpleEntityList){
        OutGoingSimplyDto outGoingSimplyDto = new OutGoingSimplyDto();
        outGoingSimplyDto.setSimpleEntityList( simpleEntityList );
        return outGoingSimplyDto;
    }
}
