package com.danielvflores.writook.model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class StoryTest {
    @Test
    void testStoryCreation() {
        List<String> genres = Arrays.asList("Romance", "Drama");
        List<String> tags = Arrays.asList("Wattpad", "Escritura");
        List<Chapter> chapters = Arrays.asList(new Chapter("Capítulo 1", "Texto", 1));
        User author = new User("Autor", "autor@example.com", "password", "Autor Display", "Bio del autor", "url-perfil");
        Story story = new Story("Título", "Sinopsis", author, 4.5, genres, tags, chapters);
        assertEquals("Título", story.getStoryTitle());
        assertEquals("Sinopsis", story.getStorySynopsis());
        assertEquals(author, story.getAuthor());
        assertEquals(4.5, story.getValorization());
        assertEquals(genres, story.getGenre());
        assertEquals(tags, story.getTags());
        assertEquals(chapters, story.getChapters());
    }
}
