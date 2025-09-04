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
        List<Object> chapters = Arrays.asList();
        Story story = new Story("Título", "Sinopsis", "Autor", 4.5, genres, tags, chapters);
        assertEquals("Título", story.getStoryTitle());
        assertEquals("Sinopsis", story.getStorySynopsis());
        assertEquals("Autor", story.getAuthor());
        assertEquals(4.5, story.getValorization());
        assertEquals(genres, story.getGenre());
        assertEquals(tags, story.getTags());
        assertEquals(chapters, story.getChapters());
    }
}
