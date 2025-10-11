package com.danielvflores.writook.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.ApiResponseDTO;
import com.danielvflores.writook.dto.ChapterCommentResponseDTO;
import com.danielvflores.writook.model.ChapterComment;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.service.ChapterCommentService;
import com.danielvflores.writook.service.UserService;

@RestController
@RequestMapping("/api/v1/chapters")
@CrossOrigin(origins = "http://localhost:3000")
public class ChapterCommentController {

    @Autowired
    private ChapterCommentService chapterCommentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    /**
     * Create a new chapter comment
     */
    @PostMapping("/stories/{storyId}/chapters/{chapterNumber}/comments")
    public ResponseEntity<ApiResponseDTO> createChapterComment(
            @PathVariable String storyId,
            @PathVariable Integer chapterNumber,
            @RequestBody CommentRequest request) {
        
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            ChapterComment comment = chapterCommentService.createChapterComment(
                storyUuid, chapterNumber, userUuid, request.getContent());
            
            ChapterCommentResponseDTO response = new ChapterCommentResponseDTO(comment, currentUser.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentario de capítulo creado exitosamente", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Get chapter comments with pagination
     */
    @GetMapping("/stories/{storyId}/chapters/{chapterNumber}/comments")
    public ResponseEntity<ApiResponseDTO> getChapterComments(
            @PathVariable String storyId,
            @PathVariable Integer chapterNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            UUID storyUuid = UUID.fromString(storyId);
            Page<ChapterComment> commentsPage = chapterCommentService.getChapterComments(
                storyUuid, chapterNumber, page, size);
            
            List<ChapterCommentResponseDTO> comments = commentsPage.getContent().stream()
                .map(comment -> {
                    User user = userService.getUserById(comment.getUserId().toString());
                    String username = user != null ? user.getUsername() : "Usuario desconocido";
                    return new ChapterCommentResponseDTO(comment, username);
                })
                .collect(Collectors.toList());

            ChapterCommentsPageResponse response = new ChapterCommentsPageResponse();
            response.setComments(comments);
            response.setCurrentPage(commentsPage.getNumber());
            response.setTotalPages(commentsPage.getTotalPages());
            response.setTotalElements(commentsPage.getTotalElements());
            response.setHasNext(commentsPage.hasNext());
            response.setHasPrevious(commentsPage.hasPrevious());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentarios del capítulo obtenidos", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Update a chapter comment
     */
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDTO> updateChapterComment(
            @PathVariable String commentId,
            @RequestBody CommentRequest request) {
        
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID commentUuid = UUID.fromString(commentId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            ChapterComment comment = chapterCommentService.updateChapterComment(
                commentUuid, userUuid, request.getContent());
            
            ChapterCommentResponseDTO response = new ChapterCommentResponseDTO(comment, currentUser.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentario de capítulo actualizado exitosamente", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Delete a chapter comment
     */
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponseDTO> deleteChapterComment(@PathVariable String commentId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID commentUuid = UUID.fromString(commentId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            chapterCommentService.deleteChapterComment(commentUuid, userUuid);

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentario eliminado exitosamente", null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    // DTOs internos
    public static class CommentRequest {
        private String content;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class ChapterCommentsPageResponse {
        private List<ChapterCommentResponseDTO> comments;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;

        // Getters and setters
        public List<ChapterCommentResponseDTO> getComments() { return comments; }
        public void setComments(List<ChapterCommentResponseDTO> comments) { this.comments = comments; }
        
        public int getCurrentPage() { return currentPage; }
        public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
        
        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
        
        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
        
        public boolean isHasNext() { return hasNext; }
        public void setHasNext(boolean hasNext) { this.hasNext = hasNext; }
        
        public boolean isHasPrevious() { return hasPrevious; }
        public void setHasPrevious(boolean hasPrevious) { this.hasPrevious = hasPrevious; }
    }
}