package com.danielvflores.writook.model;

import java.util.List;

public class Story {
    private final String storyTitle;
    private final String storySynopsis;
    private final String author;
    private final List<String> genre;
    private final List<String> tags;
    private final List<Chapter> chapters;
    private Double valorization;

    public Story(String storyTitle, String storySynopsis, String author, Double valorization, List<String> genre, List<String> tags, List<Chapter> chapters) {
        this.storyTitle = storyTitle;
        this.storySynopsis = storySynopsis;
        this.author = author;
        this.valorization = valorization;
        this.genre = genre;
        this.tags = tags;
        this.chapters = chapters;
    }

    // ONLY GETTERS ARE NEEDED, SINCE THESE FIELDS DON’T NEED TO BE MODIFIED AFTER CREATING THE OBJECT. A STORY IS IMMUTABLE.
    public String getStoryTitle() {
        return storyTitle;
    }

    public String getStorySynopsis() {
        return storySynopsis;
    }

    public String getAuthor() {
        return author;
    }

    public Double getValorization() {
        return valorization;
    }

    public List<String> getGenre() {
        return genre;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }
    
    // ONLY SETTER FOR VALORIZATION, SINCE IT CAN CHANGE OVER TIME
    public void setValorization(Double valorization) {
        this.valorization = valorization;
    }

}
