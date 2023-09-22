package org.example.repository.mapper;

import org.example.model.BookEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookResultSetMapperTest {

    @Test
    void testMap() throws SQLException {
        ResultSet mockResultSet = Mockito.mock( ResultSet.class );
        UUID expectedUUID = UUID.randomUUID();
        String expectedBookText = "Expected Book Text";

        when( mockResultSet.getObject( "uuid" ) ).thenReturn( expectedUUID );
        when( mockResultSet.getString( "bookText" ) ).thenReturn( expectedBookText );

        BookEntity bookEntity = BookResultSetMapper.INSTANCE.map( mockResultSet );

        // Проверка результата
        assertEquals( expectedUUID, bookEntity.getUuid() );
        assertEquals( expectedBookText, bookEntity.getBookText() );
    }
}
