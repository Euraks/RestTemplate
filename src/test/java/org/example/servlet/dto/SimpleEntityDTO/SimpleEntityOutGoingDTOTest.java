package org.example.servlet.dto.SimpleEntityDTO;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SimpleEntityOutGoingDTOTest {

    @Test
    void testGettersAndSetters() {
        SimpleEntityOutGoingDTO dto = new SimpleEntityOutGoingDTO();

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
