package com.danielvflores.writook.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.danielvflores.writook.model.Rating;

public class RatingResponseDTO {
    private UUID id;
    private UUID storyId;
    private UUID userId;
    private String username; // Para mostrar quién hizo la valoración
    private Integer ratingValue;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RatingResponseDTO() {}

    public RatingResponseDTO(Rating rating) {
        this.id = rating.getId();
        this.storyId = rating.getStoryId();
        this.userId = rating.getUserId();
        this.ratingValue = rating.getRatingValue();
        this.createdAt = rating.getCreatedAt();
        this.updatedAt = rating.getUpdatedAt();
    }

    public RatingResponseDTO(Rating rating, String username) {
        this(rating);
        this.username = username;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStoryId() {
        return storyId;
    }

    public void setStoryId(UUID storyId) {
        this.storyId = storyId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        this.ratingValue = ratingValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}