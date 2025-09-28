package com.danielvflores.writook.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.danielvflores.writook.model.User;

class AuthorDTOTest {

    @Test
    void testAuthorDTOCreationFromUser() {
        User user = new User("danielvflores", null, "daniel@email.com", "hashPassword", "Daniel V. Flores", "A passionate writer.", "http://profile.url/image.jpg");
        AuthorDTO dto = new AuthorDTO(user);

        assertEquals("danielvflores", dto.getUsername());
        assertEquals("Daniel V. Flores", dto.getDisplayName());
    }
}