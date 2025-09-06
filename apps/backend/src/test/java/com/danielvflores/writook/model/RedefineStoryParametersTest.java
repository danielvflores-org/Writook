package com.danielvflores.writook.model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.danielvflores.writook.dto.AuthorDTO;

class RedefineStoryParametersTest {

    @Test
    void testRedefineTitle() {
        User author = new User("danielvflores", "daniel@email.com", "hashPassword", "Daniel V. Flores", "A passionate writer.", "http://profile.url/image.jpg");
        AuthorDTO authorDTO = new AuthorDTO(author);
        List<String> genres = Arrays.asList("Fantasía", "Aventura");
        List<String> tags = Arrays.asList("Dragones", "Poderes");
        Chapter chapter1 = new Chapter("Capítulo 1", "Contenido 1", 1);
        Chapter chapter2 = new Chapter("Capítulo 2", "Contenido 2", 2);
        List<Chapter> chapters = Arrays.asList(chapter1, chapter2);

        Story original = new Story("Título A", "Sinopsis", authorDTO, 4.5, genres, tags, chapters);
        Story modificado = new Story("Título B", original.getSynopsis(), original.getAuthor(), original.getRating(), original.getGenres(), original.getTags(), original.getChapters());

        assertEquals("Título A", original.getTitle());
        assertEquals("Título B", modificado.getTitle());
        assertEquals(original.getSynopsis(), modificado.getSynopsis());
        assertEquals(original.getAuthor(), modificado.getAuthor());
        assertEquals(original.getRating(), modificado.getRating());
        assertEquals(original.getGenres(), modificado.getGenres());
        assertEquals(original.getTags(), modificado.getTags());
        assertEquals(original.getChapters(), modificado.getChapters());
    }
}