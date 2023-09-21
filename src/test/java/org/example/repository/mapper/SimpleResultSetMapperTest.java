package org.example.repository.mapper;

import org.example.model.SimpleEntity;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleResultSetMapperTest {

    @Test
    void testMap() throws SQLException {
        UUID testUuid = UUID.randomUUID();
        String testDescription = "Test Description";

        ResultSet mockResultSet = mock( ResultSet.class );
        when( mockResultSet.getObject( "uuid", UUID.class ) ).thenReturn( testUuid );
        when( mockResultSet.getString( "description" ) ).thenReturn( testDescription );

        SimpleEntity result = SimpleResultSetMapper.INSTANCE.map( mockResultSet );

        assertNotNull( result );
        assertEquals( testUuid, result.getUuid() );
        assertEquals( testDescription, result.getDescription() );
    }
}
