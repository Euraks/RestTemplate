package org.example.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {

    @Test
    void testSave() throws SQLException {

        Service<MyEntity, Long> service = mock(Service.class);
        MyEntity entity = new MyEntity();


        when(service.save(entity)).thenReturn(entity);


        MyEntity result = service.save(entity);


        assertEquals(entity, result);
    }

    @Test
    void testFindById() {

        Service<MyEntity, Long> service = mock(Service.class);
        MyEntity entity = new MyEntity();


        when(service.findById(1L)).thenReturn(entity);


        MyEntity result = service.findById(1L);


        assertEquals(entity, result);
    }



    static class MyEntity {

    }
}
