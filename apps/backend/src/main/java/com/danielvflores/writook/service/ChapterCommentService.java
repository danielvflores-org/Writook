package com.danielvflores.writook.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.danielvflores.writook.model.ChapterComment;
import com.danielvflores.writook.repository.ChapterCommentRepository;

@Service
public class ChapterCommentService {

    @Autowired
    private ChapterCommentRepository chapterCommentRepository;

    /**
     * Create a new chapter comment
     */
    public ChapterComment createChapterComment(UUID storyId, Integer chapterNumber, UUID userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Comment content cannot exceed 2000 characters");
        }

        ChapterComment comment = new ChapterComment(storyId, chapterNumber, userId, content.trim());
        return chapterCommentRepository.save(comment);
    }

    /**
     * Get chapter comments with pagination
     */
    public Page<ChapterComment> getChapterComments(UUID storyId, Integer chapterNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chapterCommentRepository.findByStoryIdAndChapterNumberOrderByCreatedAtDesc(
            storyId, chapterNumber, pageable);
    }

    /**
     * Update a chapter comment
     */
    public ChapterComment updateChapterComment(UUID commentId, UUID userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Comment content cannot exceed 2000 characters");
        }

        Optional<ChapterComment> commentOpt = chapterCommentRepository.findById(commentId);
        if (!commentOpt.isPresent()) {
            throw new IllegalArgumentException("Comment not found");
        }

        ChapterComment comment = commentOpt.get();
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to update this comment");
        }

        comment.setContent(content.trim());
        return chapterCommentRepository.save(comment);
    }

    /**
     * Delete a chapter comment
     */
    public void deleteChapterComment(UUID commentId, UUID userId) {
        Optional<ChapterComment> commentOpt = chapterCommentRepository.findById(commentId);
        if (!commentOpt.isPresent()) {
            throw new IllegalArgumentException("Comment not found");
        }

        ChapterComment comment = commentOpt.get();
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User not authorized to delete this comment");
        }

        chapterCommentRepository.delete(comment);
    }

    /**
     * Get comment by ID
     */
    public Optional<ChapterComment> getChapterCommentById(UUID commentId) {
        return chapterCommentRepository.findById(commentId);
    }
}