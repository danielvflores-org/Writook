package com.danielvflores.writook.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danielvflores.writook.dto.ChapterStatsDTO;
import com.danielvflores.writook.model.ChapterRating;
import com.danielvflores.writook.repository.ChapterCommentRepository;
import com.danielvflores.writook.repository.ChapterRatingRepository;

@Service
public class ChapterRatingService {

    @Autowired
    private ChapterRatingRepository chapterRatingRepository;

    @Autowired
    private ChapterCommentRepository chapterCommentRepository;

    /**
     * Create or update a chapter rating
     */
    public ChapterRating rateChapter(UUID storyId, Integer chapterNumber, UUID userId, Integer ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Optional<ChapterRating> existingRating = chapterRatingRepository
            .findByStoryIdAndChapterNumberAndUserId(storyId, chapterNumber, userId);

        if (existingRating.isPresent()) {
            // Update existing rating
            ChapterRating rating = existingRating.get();
            rating.setRatingValue(ratingValue);
            return chapterRatingRepository.save(rating);
        } else {
            // Create new rating
            ChapterRating newRating = new ChapterRating(storyId, chapterNumber, userId, ratingValue);
            return chapterRatingRepository.save(newRating);
        }
    }

    /**
     * Get user's rating for a specific chapter
     */
    public Optional<ChapterRating> getUserChapterRating(UUID storyId, Integer chapterNumber, UUID userId) {
        return chapterRatingRepository.findByStoryIdAndChapterNumberAndUserId(storyId, chapterNumber, userId);
    }

    /**
     * Get chapter ratings with pagination
     */
    public Page<ChapterRating> getChapterRatings(UUID storyId, Integer chapterNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chapterRatingRepository.findByStoryIdAndChapterNumber(storyId, chapterNumber, pageable);
    }

    /**
     * Delete a chapter rating
     */
    public void deleteChapterRating(UUID ratingId, UUID userId) {
        Optional<ChapterRating> rating = chapterRatingRepository.findById(ratingId);
        if (rating.isPresent() && rating.get().getUserId().equals(userId)) {
            chapterRatingRepository.delete(rating.get());
        } else {
            throw new IllegalArgumentException("Rating not found or user not authorized");
        }
    }

    /**
     * Get chapter statistics
     */
    public ChapterStatsDTO getChapterStats(UUID storyId, Integer chapterNumber) {
        Double averageRating = chapterRatingRepository
            .calculateAverageRatingByStoryIdAndChapterNumber(storyId, chapterNumber);
        Long totalRatings = chapterRatingRepository
            .countRatingsByStoryIdAndChapterNumber(storyId, chapterNumber);
        Long totalComments = chapterCommentRepository
            .countCommentsByStoryIdAndChapterNumber(storyId, chapterNumber);

        return new ChapterStatsDTO(
            averageRating != null ? averageRating : 0.0,
            totalRatings != null ? totalRatings : 0,
            totalComments != null ? totalComments : 0,
            0 // Views will be implemented later
        );
    }
}