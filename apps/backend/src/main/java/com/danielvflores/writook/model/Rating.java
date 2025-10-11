package com.danielvflores.writook.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ratings", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"story_id", "user_id"})
})
public class Rating {
    
    @Id
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;
    
    @Column(name = "story_id", columnDefinition = "UUID", nullable = false)
    private UUID storyId;
    
    @Column(name = "user_id", columnDefinition = "UUID", nullable = false)
    private UUID userId;
    
    @Column(name = "rating_value", nullable = false)
    private Integer ratingValue; // 1-5 stars
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Rating() {
        this.id = UUID.randomUUID();
        this.createdAt = LocalDateTime.now();
    }

    public Rating(UUID storyId, UUID userId, Integer ratingValue) {
        this();
        this.storyId = storyId;
        this.userId = userId;
        this.ratingValue = ratingValue;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

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

    public Integer getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Integer ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
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