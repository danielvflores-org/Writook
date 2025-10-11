package com.danielvflores.writook.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.danielvflores.writook.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    
    Page<Comment> findByStoryIdOrderByCreatedAtDesc(UUID storyId, Pageable pageable);
    
    List<Comment> findByStoryIdOrderByCreatedAtDesc(UUID storyId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.storyId = :storyId")
    Long countCommentsByStoryId(@Param("storyId") UUID storyId);
 
    List<Comment> findByUserIdOrderByCreatedAtDesc(UUID userId);

    @Query("SELECT c FROM Comment c WHERE c.storyId = :storyId ORDER BY c.createdAt DESC LIMIT :limit")
    List<Comment> findRecentCommentsByStoryId(@Param("storyId") UUID storyId, @Param("limit") int limit);
}
