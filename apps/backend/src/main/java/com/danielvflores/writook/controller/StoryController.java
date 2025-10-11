package com.danielvflores.writook.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.AuthorDTO;
import com.danielvflores.writook.model.Chapter;
import com.danielvflores.writook.model.Story;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.StoryService;
import com.danielvflores.writook.service.UserService;
import com.danielvflores.writook.utility.TokenJWTUtility;

@RestController
@RequestMapping("/api/v1/stories")
@CrossOrigin(origins = "http://localhost:3000")
public class StoryController {

    // THIS AUTOWIRED WILL BE CHANGE LATER WHEN I PRODUCE THE SERVICE
    @Autowired
    private StoryService storyService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Story> getAllStories() {
        return storyService.getAllStories();
    }

    @GetMapping("/with-stats")
    public List<StoryService.StoryWithStatsDTO> getAllStoriesWithStats() {
        return storyService.getAllStoriesWithStats();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<?> getStoryStats(@PathVariable("id") String id) {
        try {
            com.danielvflores.writook.dto.StoryStatsDTO stats = storyService.getStoryStats(id);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Story getStoryById(@PathVariable("id") String id) {
        return storyService.getStoryById(id);
    }

    @GetMapping("/{id}/ownership")
    public ResponseEntity<?> getStoryByIdWithOwnershipCheck(@PathVariable("id") String id, @RequestHeader("Authorization") String authHeader) {
        try {
            Story story = storyService.getStoryWithOwnershipCheck(id, authHeader);
            return ResponseEntity.ok(story);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("Token") || message.contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            } else if (message.contains("no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }
    }

    @PostMapping
    public Story createStory(@RequestBody Story story, @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // Remove "Bearer "
        String usernameFromToken = TokenJWTUtility.getUsernameFromToken(token);
        
        User authenticatedUser = userService.findByUsername(usernameFromToken);
        if (authenticatedUser == null) {
            authenticatedUser = userService.findByEmail(usernameFromToken);
        }
        
        if (authenticatedUser == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        
        AuthorDTO author = new AuthorDTO();
        author.setUsername(authenticatedUser.getUsername());
        author.setEmail(authenticatedUser.getEmail());
        author.setDisplayName(story.getAuthor() != null ? story.getAuthor().getDisplayName() : authenticatedUser.getDisplayName());
        author.setBio(story.getAuthor() != null ? story.getAuthor().getBio() : authenticatedUser.getBio());
        author.setProfilePictureUrl(story.getAuthor() != null ? story.getAuthor().getProfilePictureUrl() : authenticatedUser.getProfilePictureUrl());

        Story newStory = new Story(
            story.getTitle(),
            story.getSynopsis(),
            author,
            story.getRating(),
            story.getGenres(),
            story.getTags(),
            story.getChapters(),
            null
        );
        return storyService.createStory(newStory);
    }

    @PutMapping("/{id}")
    public Story updateStory(@PathVariable("id") String id, @RequestBody Story updatedStory, @RequestHeader("Authorization") String authHeader) {

        storyService.getStoryWithOwnershipCheck(id, authHeader);
        
        Story existingStory = storyService.getStoryById(id);
        if (existingStory == null) {
            throw new RuntimeException("Historia no encontrada");
        }
        
        Story storyToUpdate = new Story(
            updatedStory.getTitle(),
            updatedStory.getSynopsis(),
            existingStory.getAuthor(),
            updatedStory.getRating(),
            updatedStory.getGenres(),
            updatedStory.getTags(),
            updatedStory.getChapters(),
            id
        );
        
        return storyService.updateStory(id, storyToUpdate);
    }

    @PostMapping("/{storyId}/chapters")
    public ResponseEntity<?> addChapter(@PathVariable("storyId") String storyId, @RequestBody Chapter newChapter, @RequestHeader("Authorization") String authHeader) {
        try {

            storyService.getStoryWithOwnershipCheck(storyId, authHeader);
            
            Story story = storyService.getStoryById(storyId);
            if (story == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Historia no encontrada");
            }

            List<Chapter> chapters = new ArrayList<>(story.getChapters());
            chapters.add(newChapter);

            Story updatedStory = new Story(
                story.getTitle(),
                story.getSynopsis(),
                story.getAuthor(),
                story.getRating(),
                story.getGenres(),
                story.getTags(),
                chapters,
                storyId
            );

            Story result = storyService.updateStory(storyId, updatedStory);
            return ResponseEntity.ok(result);

        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("Token") || message.contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            } else if (message.contains("no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }
    }

    @DeleteMapping("/{id}")
    public String deleteStory(@PathVariable("id") String id) {
        boolean deleted = storyService.deleteStory(id);
        return deleted ? "Story with ID " + id + " deleted successfully" : "Story not found";
    }

    @GetMapping("/author/{username}")
    public List<Story> getStoriesByAuthorUsername(@PathVariable("username") String username) {
        return storyService.getStoriesByAuthorUsername(username);
    }

    @GetMapping("/me")
    public List<Story> getMyStories(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token not provided");
        }
        String token = authHeader.substring(7);
        String usernameFromToken = TokenJWTUtility.getUsernameFromToken(token);
        if (usernameFromToken == null) {
            throw new RuntimeException("Invalid token");
        }
        return storyService.getStoriesByAuthorUsername(usernameFromToken);
    }

    @PutMapping("/{id}/metadata")
    public ResponseEntity<?> updateStoryMetadata(@PathVariable("id") String id, @RequestBody Story updatedStory, @RequestHeader("Authorization") String authHeader) {
        try {
            storyService.getStoryWithOwnershipCheck(id, authHeader);
            
            Story existingStory = storyService.getStoryById(id);
            if (existingStory == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Historia no encontrada");
            }
            
            Story storyToUpdate = new Story(
                updatedStory.getTitle(),
                updatedStory.getSynopsis(),
                existingStory.getAuthor(),
                updatedStory.getRating(),
                updatedStory.getGenres(),
                updatedStory.getTags(),
                existingStory.getChapters(),
                id
            );
            
            Story result = storyService.updateStory(id, storyToUpdate);
            return ResponseEntity.ok(result);
            
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("Token") || message.contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            } else if (message.contains("no encontrada")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }
    }

    @PutMapping("/{storyId}/edit/{chapterId}")
    public ResponseEntity<?> updateChapter(@PathVariable("storyId") String storyId, @PathVariable("chapterId") Long chapterId, @RequestBody Chapter updatedChapter, @RequestHeader("Authorization") String authHeader) {
        try {
            Chapter result = storyService.updateChapter(storyId, chapterId, updatedChapter, authHeader);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            String message = e.getMessage();
            if (message.contains("Token") || message.contains("permiso")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
            } else if (message.contains("no encontrada") || message.contains("no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
            }
        }
    }

    // ADD OTHERS ENDPOINTS LATER FOR GENRES, TAGS, ETC. (MISSING FOR NOW)

}
