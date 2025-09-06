package com.danielvflores.writook.model;

import java.util.List;

import com.danielvflores.writook.dto.AuthorDTO;

public class Story {
    private final String title;
    private final String synopsis;
    private final AuthorDTO author; // THIS IS A AGGREGATION RELATIONSHIP, THE AUTHOR CAN EXIST WITHOUT THE STORY.
    private double rating;
    private final List<String> genres;
    private final List<String> tags;
    private final List<Chapter> chapters; // THIS IS A COMPOSITION RELATIONSHIP, IF THE STORY IS DELETED, THE CHAPTERS ARE DELETED TOO.

    public Story(String title, String synopsis, AuthorDTO author, double rating, List<String> genres, List<String> tags, List<Chapter> chapters) {
        this.title = title;
        this.synopsis = synopsis;
        this.author = author;
        this.rating = rating;
        this.genres = genres;
        this.tags = tags;
        this.chapters = chapters;
    }

    // ONLY GETTERS ARE REQUIRED — THE STORY OBJECT IS IMMUTABLE.
    public String getTitle() {
        return title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<Chapter> getChapters() {
        return chapters;
    }

    // ONLY SETTER FOR RATING, SINCE IT CAN CHANGE OVER TIME
    public void setRating(double rating) {
        this.rating = rating;
    }
}