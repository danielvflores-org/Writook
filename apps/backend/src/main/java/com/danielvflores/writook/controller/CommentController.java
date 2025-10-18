package com.danielvflores.writook.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.danielvflores.writook.dto.CommentResponseDTO;
import com.danielvflores.writook.model.Comment;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.service.CommentService;
import com.danielvflores.writook.service.UserService;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    /**
     * Crear un nuevo comentario
     */
    @PostMapping("/stories/{storyId}")
    public ResponseEntity<ApiResponseDTO> createComment(
            @PathVariable String storyId, 
            @RequestBody CommentRequest request) {
        
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            Comment comment = commentService.createComment(storyUuid, userUuid, request.getContent());
            CommentResponseDTO response = new CommentResponseDTO(comment, currentUser.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentario creado exitosamente", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Obtener comentarios de una historia con paginación
     */
    @GetMapping("/stories/{storyId}")
    public ResponseEntity<ApiResponseDTO> getStoryComments(
            @PathVariable String storyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            UUID storyUuid = UUID.fromString(storyId);
            Page<Comment> commentsPage = commentService.getStoryComments(storyUuid, page, size);
            
            List<CommentResponseDTO> comments = commentsPage.getContent().stream()
                .map(comment -> {
                    User user = userService.getUserById(comment.getUserId().toString());
                    String username = user != null ? user.getUsername() : "Usuario desconocido";
                    return new CommentResponseDTO(comment, username);
                })
                .collect(Collectors.toList());

            // Crear respuesta con información de paginación
            CommentsPageResponse response = new CommentsPageResponse();
            response.setComments(comments);
            response.setCurrentPage(commentsPage.getNumber());
            response.setTotalPages(commentsPage.getTotalPages());
            response.setTotalElements(commentsPage.getTotalElements());
            response.setHasNext(commentsPage.hasNext());
            response.setHasPrevious(commentsPage.hasPrevious());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentarios obtenidos", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Actualizar un comentario
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO> updateComment(
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

            Comment comment = commentService.updateComment(commentUuid, userUuid, request.getContent());
            CommentResponseDTO response = new CommentResponseDTO(comment, currentUser.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Comentario actualizado exitosamente", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Eliminar un comentario
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponseDTO> deleteComment(@PathVariable String commentId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID commentUuid = UUID.fromString(commentId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            boolean deleted = commentService.deleteComment(commentUuid, userUuid);
            
            if (deleted) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Comentario eliminado", null));
            } else {
                return ResponseEntity.notFound().build();
            }

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class CommentsPageResponse {
        private List<CommentResponseDTO> comments;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;

        // Getters and setters
        public List<CommentResponseDTO> getComments() { return comments; }
        public void setComments(List<CommentResponseDTO> comments) { this.comments = comments; }
        
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