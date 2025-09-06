package com.danielvflores.writook.model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.danielvflores.writook.dto.AuthorDTO;

class StoryTest {

    @Test
    void testStoryCreation() {
        User user = new User("danielvflores", "daniel@email.com", "hashPassword", "Daniel V. Flores", "A passionate writer.", "http://profile.url/image.jpg");
        AuthorDTO authorDTO = new AuthorDTO(user);
        List<String> genres = Arrays.asList("Fantasía", "Aventura");
        List<String> tags = Arrays.asList("Dragones", "Poderes");
        Chapter chapter1 = new Chapter("Capítulo 1", "Contenido 1", 1);
        Chapter chapter2 = new Chapter("Capítulo 2", "Contenido 2", 2);
        List<Chapter> chapters = Arrays.asList(chapter1, chapter2);

        Story story = new Story("Título", "Sinopsis", authorDTO, 4.5, genres, tags, chapters);

        assertEquals("Título", story.getTitle());
        assertEquals("Sinopsis", story.getSynopsis());
        assertEquals(authorDTO, story.getAuthor());
        assertEquals(4.5, story.getRating());
        assertEquals(genres, story.getGenres());
        assertEquals(tags, story.getTags());
        assertEquals(chapters, story.getChapters());
    }
}