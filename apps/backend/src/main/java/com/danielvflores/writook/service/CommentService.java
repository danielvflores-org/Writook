package com.danielvflores.writook.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danielvflores.writook.model.Comment;
import com.danielvflores.writook.repository.CommentRepository;

@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(UUID storyId, UUID userId, String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        
        if (content.length() > 2000) {
            throw new IllegalArgumentException("Comment cannot exceed 2000 characters");
        }

        Comment comment = new Comment(storyId, userId, content.trim());
        return commentRepository.save(comment);
    }

    public Page<Comment> getStoryComments(UUID storyId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return commentRepository.findByStoryIdOrderByCreatedAtDesc(storyId, pageable);
    }

    public List<Comment> getAllStoryComments(UUID storyId) {
        return commentRepository.findByStoryIdOrderByCreatedAtDesc(storyId);
    }

    public List<Comment> getRecentComments(UUID storyId, int limit) {
        return commentRepository.findRecentCommentsByStoryId(storyId, limit);
    }

    public Long getTotalComments(UUID storyId) {
        return commentRepository.countCommentsByStoryId(storyId);
    }

    public Optional<Comment> getCommentById(UUID commentId) {
        return commentRepository.findById(commentId);
    }

    public Comment updateComment(UUID commentId, UUID userId, String newContent) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        
        if (commentOpt.isEmpty()) {
            throw new IllegalArgumentException("Comment not found");
        }

        Comment comment = commentOpt.get();
        
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only edit your own comments");
        }

        if (newContent == null || newContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Comment content cannot be empty");
        }
        
        if (newContent.length() > 2000) {
            throw new IllegalArgumentException("Comment cannot exceed 2000 characters");
        }

        comment.setContent(newContent.trim());
        return commentRepository.save(comment);
    }

    public boolean deleteComment(UUID commentId, UUID userId) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        
        if (commentOpt.isEmpty()) {
            return false;
        }

        Comment comment = commentOpt.get();
        
        if (!comment.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only delete your own comments");
        }

        commentRepository.delete(comment);
        return true;
    }

    public List<Comment> getUserComments(UUID userId) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
}