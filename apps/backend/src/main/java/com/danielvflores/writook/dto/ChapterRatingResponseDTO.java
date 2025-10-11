package com.danielvflores.writook.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.danielvflores.writook.model.ChapterRating;

public class ChapterRatingResponseDTO {
    private UUID id;
    private UUID storyId;
    private Integer chapterNumber;
    private UUID userId;
    private String username;
    private Integer ratingValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ChapterRatingResponseDTO() {}

    public ChapterRatingResponseDTO(ChapterRating rating, String username) {
        this.id = rating.getId();
        this.storyId = rating.getStoryId();
        this.chapterNumber = rating.getChapterNumber();
        this.userId = rating.getUserId();
        this.username = username;
        this.ratingValue = rating.getRatingValue();
        this.createdAt = rating.getCreatedAt();
        this.updatedAt = rating.getUpdatedAt();
    }

    // Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getStoryId() { return storyId; }
    public void setStoryId(UUID storyId) { this.storyId = storyId; }

    public Integer getChapterNumber() { return chapterNumber; }
    public void setChapterNumber(Integer chapterNumber) { this.chapterNumber = chapterNumber; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Integer getRatingValue() { return ratingValue; }
    public void setRatingValue(Integer ratingValue) { this.ratingValue = ratingValue; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}