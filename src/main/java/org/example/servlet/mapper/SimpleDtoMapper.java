package org.example.servlet.mapper;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.IncomingDto;
import org.example.servlet.dto.OutGoingDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SimpleDtoMapper {

    SimpleDtoMapper INSTANCE = Mappers.getMapper( SimpleDtoMapper.class );

    SimpleEntity map(IncomingDto incomingDto);

    OutGoingDto map(SimpleEntity simpleEntity);
}
