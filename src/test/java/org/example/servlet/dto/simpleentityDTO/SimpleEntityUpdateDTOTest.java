package org.example.servlet.dto.simpleentityDTO;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SimpleEntityUpdateDTOTest {

    @Test
    void testGettersAndSetters() {
        SimpleEntityUpdateDTO dto = new SimpleEntityUpdateDTO();

        assertNull( dto.getUuid() );
        assertNull( dto.getDescription() );

        UUID expectedUUID = UUID.randomUUID();
        String expectedDescription = "Test Description";

        dto.setUuid( expectedUUID );
        dto.setDescription( expectedDescription );

        assertEquals( expectedUUID, dto.getUuid() );
        assertEquals( expectedDescription, dto.getDescription() );
    }
}
