package org.example.servlet.dto.SimpleEntityDTO.mapper;

import org.example.model.SimpleEntity;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityAllOutGoingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityIncomingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityOutGoingDTO;
import org.example.servlet.dto.SimpleEntityDTO.SimpleEntityUpdateDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleDtoMapperTest {

    private final SimpleDtoMapper mapper = SimpleDtoMapper.INSTANCE;

    @Test
    void testMapIncomingDtoToEntity() {
        SimpleEntityIncomingDTO dto = new SimpleEntityIncomingDTO();
        dto.setDescription( "TestDescription" );

        SimpleEntity entity = mapper.map( dto );

        assertEquals( dto.getDescription(), entity.getDescription() );
    }

    @Test
    void testMapEntityToOutGoingDto() {
        SimpleEntity entity = new SimpleEntity();
        entity.setUuid( UUID.randomUUID() );
        entity.setDescription( "TestDescription" );

        SimpleEntityOutGoingDTO dto = mapper.map( entity );

        assertEquals( entity.getUuid(), dto.getUuid() );
        assertEquals( entity.getDescription(), dto.getDescription() );
    }

    @Test
    void testMapUpdateDtoToEntity() {
        SimpleEntityUpdateDTO updateDTO = new SimpleEntityUpdateDTO();
        updateDTO.setUuid( UUID.randomUUID() );
        updateDTO.setDescription( "UpdatedDescription" );

        SimpleEntity entity = mapper.map( updateDTO );

        assertEquals( updateDTO.getUuid(), entity.getUuid() );
        assertEquals( updateDTO.getDescription(), entity.getDescription() );
    }

    @Test
    void testMapListToDto() {
        SimpleEntity entity1 = new SimpleEntity();
        SimpleEntity entity2 = new SimpleEntity();
        List<SimpleEntity> entities = Arrays.asList( entity1, entity2 );

        SimpleEntityAllOutGoingDTO dto = mapper.mapListToDto( entities );

        assertEquals( entities.size(), dto.getSimpleEntityList().size() );
    }
}
