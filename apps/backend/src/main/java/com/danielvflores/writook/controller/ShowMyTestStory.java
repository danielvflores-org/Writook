package com.danielvflores.writook.controller;

import java.util.Arrays;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.AuthorDTO;
import com.danielvflores.writook.model.Chapter;
import com.danielvflores.writook.model.Story;
import com.danielvflores.writook.model.User;

@RestController
@RequestMapping("/api/v1")
public class ShowMyTestStory {

    User testUser = new User("danielvflores", 4L,"daniel@email.com", "hashPassword", "Daniel V. Flores", "A passionate writer.", "http://profile.url/image.jpg");
    AuthorDTO testAuthorDTO = new AuthorDTO(testUser);
    Chapter chapter1 = new Chapter("El despertar del dragón", "En un mundo donde los dioses y los humanos coexisten, Issei descubre que es el heredero de un antiguo linaje de dragones...", 1);
    Chapter chapter2 = new Chapter("El poder oculto", "Mientras Issei lucha por controlar sus nuevas habilidades, se encuentra con aliados inesperados y enemigos formidables...", 2);
    Story testStory = new Story(
        "Issei el dios dragón",
        "Este es un fanfic de dxd",
        testAuthorDTO,
        5.0,
        Arrays.asList("Fantasía", "Aventura"),
        Arrays.asList("Dragones", "Poderes", "Batallas épicas", "Issei"),
        Arrays.asList(chapter1, chapter2),
        1L
    );

    @GetMapping("/test-story")
    public Story showTestStory() {
        return testStory;
    }

}