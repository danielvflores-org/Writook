package com.danielvflores.writook.model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class RedifineStoryParametersTest {
    @Test
    void testRedefineTitle() {
        List<String> genres = Arrays.asList("Romance", "Drama");
        List<String> tags = Arrays.asList("Wattpad", "Escritura");
        List<Chapter> chapters = Arrays.asList(new Chapter("Capítulo 1", "Texto", 1));
        User author = new User("Autor", "autor@example.com", "password", "Autor Display", "Bio del autor", "url-perfil");
        Story original = new Story("Título A", "Sinopsis", author, 4.5, genres, tags, chapters);
        Story modificado = new Story("Título B", original.getStorySynopsis(), original.getAuthor(), original.getValorization(), original.getGenre(), original.getTags(), original.getChapters());
        assertEquals("Título B", modificado.getStoryTitle());
        assertEquals(original.getStorySynopsis(), modificado.getStorySynopsis());
        assertEquals(author, modificado.getAuthor());
        assertEquals(original.getValorization(), modificado.getValorization());
        assertEquals(original.getGenre(), modificado.getGenre());
        assertEquals(original.getTags(), modificado.getTags());
        assertEquals(original.getChapters(), modificado.getChapters());
    }
}
