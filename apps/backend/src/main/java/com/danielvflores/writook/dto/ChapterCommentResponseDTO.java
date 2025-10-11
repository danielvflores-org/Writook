package com.danielvflores.writook.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.danielvflores.writook.model.ChapterComment;

public class ChapterCommentResponseDTO {
    private UUID id;
    private UUID storyId;
    private Integer chapterNumber;
    private UUID userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ChapterCommentResponseDTO() {}

    public ChapterCommentResponseDTO(ChapterComment comment, String username) {
        this.id = comment.getId();
        this.storyId = comment.getStoryId();
        this.chapterNumber = comment.getChapterNumber();
        this.userId = comment.getUserId();
        this.username = username;
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
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

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}