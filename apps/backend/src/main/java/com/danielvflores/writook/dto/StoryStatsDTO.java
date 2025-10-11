package com.danielvflores.writook.dto;

import java.util.UUID;

public class StoryStatsDTO {
    private UUID storyId;
    private Double averageRating;
    private Long totalRatings;
    private Long totalComments;
    private Integer totalChapters;
    private String status; // "In Progress", "Completed", "On Hold", etc.

    public StoryStatsDTO() {}

    public StoryStatsDTO(UUID storyId, Double averageRating, Long totalRatings, Long totalComments, Integer totalChapters) {
        this.storyId = storyId;
        this.averageRating = averageRating != null ? averageRating : 0.0;
        this.totalRatings = totalRatings != null ? totalRatings : 0L;
        this.totalComments = totalComments != null ? totalComments : 0L;
        this.totalChapters = totalChapters != null ? totalChapters : 0;
        this.status = calculateStatus(totalChapters);
    }

    // Calcular estado basado en el número de capítulos y otros factores
    private String calculateStatus(Integer totalChapters) {
        if (totalChapters == null || totalChapters == 0) {
            return "Not Started";
        } else if (totalChapters < 5) {
            return "In Progress";
        } else if (totalChapters >= 10) {
            return "Completed";
        } else {
            return "In Progress";
        }
    }

    // Getters and setters
    public UUID getStoryId() {
        return storyId;
    }

    public void setStoryId(UUID storyId) {
        this.storyId = storyId;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(Long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public Long getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(Long totalComments) {
        this.totalComments = totalComments;
    }

    public Integer getTotalChapters() {
        return totalChapters;
    }

    public void setTotalChapters(Integer totalChapters) {
        this.totalChapters = totalChapters;
        this.status = calculateStatus(totalChapters); // Recalcular estado
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}