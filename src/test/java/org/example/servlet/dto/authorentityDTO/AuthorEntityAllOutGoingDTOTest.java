package org.example.servlet.dto.authorentityDTO;

import org.example.model.AuthorEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthorEntityAllOutGoingDTOTest {

    private AuthorEntityAllOutGoingDTO dto;

    @BeforeEach
    public void setUp() {
        dto = new AuthorEntityAllOutGoingDTO();
    }

    @Test
    void testGetAuthorEntityListReturnsEmptyListWhenNotSet() {
        List<AuthorEntity> authorEntities = dto.getAuthorEntityList();
        assertNotNull(authorEntities, "Should return an empty list instead of null");
        assertTrue(authorEntities.isEmpty(), "List should be empty");
    }

    @Test
    void testSetAndGetAuthorEntityList() {
        AuthorEntity authorEntity1 = new AuthorEntity();
        authorEntity1.setAuthorName("Author 1");
        AuthorEntity authorEntity2 = new AuthorEntity();
        authorEntity2.setAuthorName("Author 2");

        dto.setAuthorEntityList(Arrays.asList(authorEntity1, authorEntity2));

        List<AuthorEntity> retrievedAuthorEntities = dto.getAuthorEntityList();

        assertEquals(2, retrievedAuthorEntities.size(), "There should be 2 authors");
        assertEquals("Author 1", retrievedAuthorEntities.get(0).getAuthorName());
        assertEquals("Author 2", retrievedAuthorEntities.get(1).getAuthorName());
    }
}
