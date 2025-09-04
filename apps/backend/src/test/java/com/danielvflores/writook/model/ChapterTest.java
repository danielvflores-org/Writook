package com.danielvflores.writook.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ChapterTest {
    @Test
    void testChapterCreation() {
        Chapter chapter = new Chapter("Capítulo 1", "Contenido del capítulo", 1);
        assertEquals("Capítulo 1", chapter.getChapterTitle());
        assertEquals("Contenido del capítulo", chapter.getChapterContent());
        assertEquals(1, chapter.getChapterNumber());
    }
}
