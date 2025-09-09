package com.danielvflores.writook.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.danielvflores.writook.dto.AuthorDTO;
import com.danielvflores.writook.model.Chapter;
import com.danielvflores.writook.model.Story;

@Service
public class StoryService {
    
    // Business logic related to users will be implemented here
    // MOCK STRUCTURES FOR DEVELOPMENT PURPOSES
    private final List<Story> stories = new ArrayList<>();
    private Long currentId = 1L;

    public StoryService() {
        // MY MOCK DATA IS PROVISIONAL FOR DEVELOPMENT PURPOSES
        // THIS MOCK DATA WILL BE REMOVED ONCE CONNECTED TO A REAL DATABASE
        stories.add(new Story(
            "My First Story", 
            "Once upon a time...", 
            new AuthorDTO("johndoe", "John Doe", "Fantasy writer and dreamer", "https://avatar.url/john.jpg"), 
            4.5, 
            List.of("Fantasy"), 
            List.of("Magic", "Adventure"), 
            List.of(new Chapter("The Beginning", "In a land far away, magic was real...", 1)), 
            1L
        ));
        stories.add(new Story(
            "A Day in the Life", 
            "It was a sunny day...", 
            new AuthorDTO("janesmith", "Jane Smith", "Everyday stories enthusiast", "https://avatar.url/jane.jpg"), 
            4.0, 
            List.of("Slice of Life"), 
            List.of("Everyday", "Realism"), 
            List.of(new Chapter("Morning Routine", "The alarm clock rang at 7 AM...", 1)), 
            2L
        ));
        stories.add(new Story(
            "The Mystery", 
            "The door creaked open...", 
            new AuthorDTO("alicejohnson", "Alice Johnson", "Mystery and thriller writer", "https://avatar.url/alice.jpg"), 
            5.0, 
            List.of("Mystery"), 
            List.of("Suspense", "Thriller"), 
            List.of(new Chapter("The Discovery", "The door creaked open revealing secrets...", 1)), 
            3L
        ));
        currentId = 4L;
    }

    public List<Story> getAllStories() {
        return stories;
    }

    public Story getStoryById(Long id) {
        return stories.stream()
                .filter(story -> story.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Story createStory(Story story) {
        Story storyWithId = new Story(
            story.getTitle(),
            story.getSynopsis(),
            story.getAuthor(),
            story.getRating(),
            story.getGenres(),
            story.getTags(),
            story.getChapters(),
            currentId++
        );
        stories.add(storyWithId);
        return storyWithId;
    }

    public Story updateStory(Long id, Story updatedStory) {
        for (int i = 0; i < stories.size(); i++) {
            if (stories.get(i).getId().equals(id)) {
                Story updatedStoryWithId = new Story(
                    updatedStory.getTitle(),
                    updatedStory.getSynopsis(),
                    updatedStory.getAuthor(),
                    updatedStory.getRating(),
                    updatedStory.getGenres(),
                    updatedStory.getTags(),
                    updatedStory.getChapters(),
                    id
                );
                stories.set(i, updatedStoryWithId);
                return updatedStoryWithId;
            }
        }
        return null;
    }

    public boolean deleteStory(Long id) {
        return stories.removeIf(story -> story.getId().equals(id));
    }

    public List<Story> getStoriesByAuthorUsername(String username) {
        List<Story> result = new ArrayList<>();
        for (Story story : stories) {
            if (story.getAuthor().getUsername().equals(username)) {
                result.add(story);
            }
        }
        return result;
    }

    public List<Story> getStoriesByGenre(String genre) {
        List<Story> result = new ArrayList<>();
        for (Story story : stories) {
            if (story.getGenres().contains(genre)) {
                result.add(story);
            }
        }
        return result;
    }

    public List<Story> getStoriesByTag(String tag) {
        List<Story> result = new ArrayList<>();
        for (Story story : stories) {
            if (story.getTags().contains(tag)) {
                result.add(story);
            }
        }
        return result;
    }

    public List<Story> getTopRatedStories(int limit) {
        return stories.stream()
                .sorted((s1, s2) -> Double.compare(s2.getRating(), s1.getRating()))
                .limit(limit)
                .toList();
    }

    public List<Story> searchStories(String query) {
        String lowerQuery = query.toLowerCase();
        List<Story> result = new ArrayList<>();
        for (Story story : stories) {
            if (story.getTitle().toLowerCase().contains(lowerQuery) || 
                story.getSynopsis().toLowerCase().contains(lowerQuery) || 
                story.getAuthor().getDisplayName().toLowerCase().contains(lowerQuery)) {
                result.add(story);
            }
        }
        return result;
    }
}
