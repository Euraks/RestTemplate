package org.example.repository.mapper;

import org.example.model.TagEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TagResultSetMapperTest {

    @Test
    void testMap() throws SQLException {

        ResultSet mockResultSet = Mockito.mock( ResultSet.class );
        UUID exceptedUUID = UUID.randomUUID();
        String expectedTagName = "Expected Tag Name";

        when( mockResultSet.getObject( "uuid" ) ).thenReturn( exceptedUUID );
        when( mockResultSet.getString( "tagName" ) ).thenReturn( expectedTagName );

        TagEntity tagEntity = TagResultSetMapper.INSTANCE.map( mockResultSet );

        assertEquals( exceptedUUID, tagEntity.getUuid() );
        assertEquals( expectedTagName, tagEntity.getTagName() );
    }
}
