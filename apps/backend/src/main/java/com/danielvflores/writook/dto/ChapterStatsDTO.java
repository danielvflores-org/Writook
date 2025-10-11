package com.danielvflores.writook.dto;

public class ChapterStatsDTO {
    private double averageRating;
    private long totalRatings;
    private long totalComments;
    private long views;

    // Constructors
    public ChapterStatsDTO() {}

    public ChapterStatsDTO(double averageRating, long totalRatings, long totalComments, long views) {
        this.averageRating = averageRating;
        this.totalRatings = totalRatings;
        this.totalComments = totalComments;
        this.views = views;
    }

    // Getters and Setters
    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public long getTotalRatings() { return totalRatings; }
    public void setTotalRatings(long totalRatings) { this.totalRatings = totalRatings; }

    public long getTotalComments() { return totalComments; }
    public void setTotalComments(long totalComments) { this.totalComments = totalComments; }

    public long getViews() { return views; }
    public void setViews(long views) { this.views = views; }
}