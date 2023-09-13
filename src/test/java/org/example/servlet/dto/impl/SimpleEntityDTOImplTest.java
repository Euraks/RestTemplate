package org.example.servlet.dto.impl;

import org.example.model.SimpleEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class SimpleEntityDTOImplTest {

    private static SimpleEntityDTOImpl simpleEntityDTO;


    @BeforeAll
    static void init() {
       simpleEntityDTO = new SimpleEntityDTOImpl();
    }

    @Test
    void getSimpleEntityForId() {
        List<SimpleEntity> simpleEntities = simpleEntityDTO.getAll();
        for (SimpleEntity se:simpleEntities) {
            assertNotNull( simpleEntityDTO.getSimpleEntityForId( se.getUuid() ) );
        }
    }

    @Test
    void getAll() {
        assertNotNull( simpleEntityDTO.getAll() );
        List<SimpleEntity> simpleEntities = simpleEntityDTO.getAll();
        for (SimpleEntity se:simpleEntities) {
            assertNotNull( se );
        }
    }
}