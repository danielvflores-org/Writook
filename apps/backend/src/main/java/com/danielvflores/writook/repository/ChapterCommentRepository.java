package com.danielvflores.writook.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.danielvflores.writook.model.ChapterComment;

@Repository
public interface ChapterCommentRepository extends JpaRepository<ChapterComment, UUID> {
    
    /**
     * Get all comments for a specific chapter with pagination
     */
    Page<ChapterComment> findByStoryIdAndChapterNumberOrderByCreatedAtDesc(
        UUID storyId, Integer chapterNumber, Pageable pageable);
    
    /**
     * Count total comments for a chapter
     */
    @Query("SELECT COUNT(cc) FROM ChapterComment cc WHERE cc.storyId = :storyId AND cc.chapterNumber = :chapterNumber")
    Long countCommentsByStoryIdAndChapterNumber(
        @Param("storyId") UUID storyId, @Param("chapterNumber") Integer chapterNumber);
    
    /**
     * Find comments by user for a specific chapter
     */
    Page<ChapterComment> findByStoryIdAndChapterNumberAndUserIdOrderByCreatedAtDesc(
        UUID storyId, Integer chapterNumber, UUID userId, Pageable pageable);
    
    /**
     * Delete all comments for a specific chapter
     */
    void deleteByStoryIdAndChapterNumber(UUID storyId, Integer chapterNumber);
    
    /**
     * Delete all comments for a story (when story is deleted)
     */
    void deleteByStoryId(UUID storyId);
}