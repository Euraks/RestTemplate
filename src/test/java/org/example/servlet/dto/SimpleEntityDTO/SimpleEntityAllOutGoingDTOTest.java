package org.example.servlet.dto.SimpleEntityDTO;

import org.example.model.SimpleEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimpleEntityAllOutGoingDTOTest {

    @Test
    void testGettersAndSetters() {
        SimpleEntityAllOutGoingDTO dto = new SimpleEntityAllOutGoingDTO();


        assertNull( dto.getSimpleEntityList() );


        SimpleEntity entity1 = new SimpleEntity();
        SimpleEntity entity2 = new SimpleEntity();
        List<SimpleEntity> expectedList = Arrays.asList( entity1, entity2 );

        dto.setSimpleEntityList( expectedList );
        assertEquals( expectedList, dto.getSimpleEntityList() );


        dto.setSimpleEntityList( Collections.emptyList() );
        assertTrue( dto.getSimpleEntityList().isEmpty() );
    }
}
