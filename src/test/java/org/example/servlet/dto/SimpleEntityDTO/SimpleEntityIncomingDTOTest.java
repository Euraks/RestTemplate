package org.example.servlet.dto.SimpleEntityDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SimpleEntityIncomingDTOTest {

    @Test
    public void testGettersAndSetters() {
        SimpleEntityIncomingDTO dto = new SimpleEntityIncomingDTO();

        assertNull( dto.getDescription() );

        String expectedDescription = "Test Description";
        dto.setDescription( expectedDescription );

        assertEquals( expectedDescription, dto.getDescription() );
    }
}
