package com.danielvflores.writook.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.danielvflores.writook.model.ChapterRating;

@Repository
public interface ChapterRatingRepository extends JpaRepository<ChapterRating, UUID> {
    
    /**
     * Find a specific user's rating for a chapter
     */
    Optional<ChapterRating> findByStoryIdAndChapterNumberAndUserId(
        UUID storyId, Integer chapterNumber, UUID userId);
    
    /**
     * Get all ratings for a specific chapter
     */
    Page<ChapterRating> findByStoryIdAndChapterNumber(
        UUID storyId, Integer chapterNumber, Pageable pageable);
    
    /**
     * Calculate average rating for a chapter
     */
    @Query("SELECT AVG(cr.ratingValue) FROM ChapterRating cr WHERE cr.storyId = :storyId AND cr.chapterNumber = :chapterNumber")
    Double calculateAverageRatingByStoryIdAndChapterNumber(
        @Param("storyId") UUID storyId, @Param("chapterNumber") Integer chapterNumber);
    
    /**
     * Count total ratings for a chapter
     */
    @Query("SELECT COUNT(cr) FROM ChapterRating cr WHERE cr.storyId = :storyId AND cr.chapterNumber = :chapterNumber")
    Long countRatingsByStoryIdAndChapterNumber(
        @Param("storyId") UUID storyId, @Param("chapterNumber") Integer chapterNumber);
    
    /**
     * Delete all ratings for a specific chapter
     */
    void deleteByStoryIdAndChapterNumber(UUID storyId, Integer chapterNumber);
    
    /**
     * Delete all ratings for a story (when story is deleted)
     */
    void deleteByStoryId(UUID storyId);
}