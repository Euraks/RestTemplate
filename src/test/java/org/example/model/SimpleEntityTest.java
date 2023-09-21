package org.example.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SimpleEntityTest {

    @Test
    void testDefaultConstructorGeneratesUuid() {
        SimpleEntity entity = new SimpleEntity();
        assertNotNull( entity.getUuid(), "UUID should be generated" );
    }

    @Test
    void testDescriptionConstructor() {
        String description = "test description";
        SimpleEntity entity = new SimpleEntity( description );

        assertNotNull( entity.getUuid(), "UUID should be generated" );
        assertEquals( description, entity.getDescription(), "Description should match the provided value" );
    }

    @Test
    void testSettersAndGetters() {
        SimpleEntity entity = new SimpleEntity();
        UUID testUuid = UUID.randomUUID();
        String description = "Another description";

        entity.setUuid( testUuid );
        entity.setDescription( description );

        assertEquals( testUuid, entity.getUuid(), "UUID should match the set value" );
        assertEquals( description, entity.getDescription(), "Description should match the set value" );
    }

    @Test
    void testToString() {
        String description = "Sample description";
        SimpleEntity entity = new SimpleEntity( description );

        assertTrue( entity.toString().contains( entity.getUuid().toString() ), "toString should contain the UUID" );
        assertTrue( entity.toString().contains( description ), "toString should contain the description" );
    }

    @Test
    void testEqualsAndHashCode() {
        String description1 = "description1";
        String description2 = "description2";

        SimpleEntity entity1 = new SimpleEntity( description1 );
        SimpleEntity entity2 = new SimpleEntity( description1 );
        SimpleEntity entity3 = new SimpleEntity( description2 );


        assertEquals( entity1, entity1, "Entity should be equal to itself" );


        assertTrue( entity1.equals( entity2 ) && entity2.equals( entity1 ), "Entities with the same description should be equal" );


        assertNotEquals( entity1, entity3, "Entities with different descriptions should not be equal" );


        assertNotEquals( null, entity1, "Entity should not be equal to null" );


        assertEquals( entity1.hashCode(), entity2.hashCode(), "Entities with the same description should have the same hashcode" );
        assertNotEquals( entity1.hashCode(), entity3.hashCode(), "Entities with different descriptions should have different hashcodes" );
    }
}
