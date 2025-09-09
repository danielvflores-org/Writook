package com.danielvflores.writook.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.model.Story;
import com.danielvflores.writook.service.StoryService;

@RestController
@RequestMapping("/api/v1/stories")
@CrossOrigin(origins = "http://localhost:3000")
public class StoryController {

    // THIS AUTOWIRED WILL BE CHANGE LATER WHEN I PRODUCE THE SERVICE
    @Autowired
    private StoryService storyService;

    @GetMapping
    public List<Story> getAllStories() {
        return storyService.getAllStories();
    }

    @GetMapping("/{id}")
    public Story getStoryById(@PathVariable Long id) {
        return storyService.getStoryById(id);
    }

    @PostMapping
    public Story createStory(@RequestBody Story story) {
        return storyService.createStory(story);
    }

    @PutMapping("/{id}")
    public Story updateStory(@PathVariable Long id, @RequestBody Story updatedStory) {
        return storyService.updateStory(id, updatedStory);
    }

    @DeleteMapping("/{id}")
    public String deleteStory(@PathVariable Long id) {
        boolean deleted = storyService.deleteStory(id);
        return deleted ? "Story with ID " + id + " deleted successfully" : "Story not found";
    }

    @GetMapping("/author/{username}")
    public List<Story> getStoriesByAuthorUsername(@PathVariable String username) {
        return storyService.getStoriesByAuthorUsername(username);
    }

    // ADD OTHERS ENDPOINTS LATER FOR GENRES, TAGS, ETC. (MISSING FOR NOW)

}
