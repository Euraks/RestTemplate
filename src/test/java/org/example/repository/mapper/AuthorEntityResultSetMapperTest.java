package org.example.repository.mapper;

import org.example.model.AuthorEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AuthorEntityResultSetMapperTest {

    @Test
    void testMap() throws SQLException {
        ResultSet mockResultSet = Mockito.mock( ResultSet.class );
        UUID expectedUUID = UUID.randomUUID();
        String expectedName = "John Silver";

        when( mockResultSet.getObject( "uuid" ) ).thenReturn( expectedUUID );
        when( mockResultSet.getString( "authorName" ) ).thenReturn( expectedName );

        AuthorEntityResultSetMapper mapper = Mappers.getMapper( AuthorEntityResultSetMapper.class );


        AuthorEntity authorEntity = mapper.map( mockResultSet );


        assertEquals( expectedUUID, authorEntity.getUuid() );
        assertEquals( expectedName, authorEntity.getAuthorName() );
    }
}
