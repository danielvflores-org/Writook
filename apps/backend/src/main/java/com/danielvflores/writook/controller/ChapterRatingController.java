package com.danielvflores.writook.controller;

import java.util.List;
import java.util.Optional;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.ApiResponseDTO;
import com.danielvflores.writook.dto.ChapterRatingResponseDTO;
import com.danielvflores.writook.dto.ChapterStatsDTO;
import com.danielvflores.writook.model.ChapterRating;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.service.ChapterRatingService;
import com.danielvflores.writook.service.UserService;

@RestController
@RequestMapping("/api/v1/chapters")
@CrossOrigin(origins = "http://localhost:3000")
public class ChapterRatingController {

    @Autowired
    private ChapterRatingService chapterRatingService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    /**
     * Rate a chapter
     */
    @PostMapping("/stories/{storyId}/chapters/{chapterNumber}/ratings")
    public ResponseEntity<ApiResponseDTO> rateChapter(
            @PathVariable String storyId,
            @PathVariable Integer chapterNumber,
            @RequestBody RatingRequest request) {
        
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            ChapterRating rating = chapterRatingService.rateChapter(
                storyUuid, chapterNumber, userUuid, request.getRatingValue());
            
            ChapterRatingResponseDTO response = new ChapterRatingResponseDTO(rating, currentUser.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Calificación de capítulo enviada exitosamente", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Get user's rating for a chapter
     */
    @GetMapping("/stories/{storyId}/chapters/{chapterNumber}/ratings/user")
    public ResponseEntity<ApiResponseDTO> getUserChapterRating(
            @PathVariable String storyId,
            @PathVariable Integer chapterNumber) {
        
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            Optional<ChapterRating> rating = chapterRatingService.getUserChapterRating(
                storyUuid, chapterNumber, userUuid);

            if (rating.isPresent()) {
                ChapterRatingResponseDTO response = new ChapterRatingResponseDTO(rating.get(), currentUser.getUsername());
                return ResponseEntity.ok(new ApiResponseDTO(true, "Calificación encontrada", response));
            } else {
                return ResponseEntity.ok(new ApiResponseDTO(true, "No hay calificación del usuario", null));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Get chapter statistics
     */
    @GetMapping("/stories/{storyId}/chapters/{chapterNumber}/stats")
    public ResponseEntity<ApiResponseDTO> getChapterStats(
            @PathVariable String storyId,
            @PathVariable Integer chapterNumber) {
        
        try {
            UUID storyUuid = UUID.fromString(storyId);
            ChapterStatsDTO stats = chapterRatingService.getChapterStats(storyUuid, chapterNumber);

            return ResponseEntity.ok(new ApiResponseDTO(true, "Estadísticas del capítulo obtenidas", stats));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Get all ratings for a chapter
     */
    @GetMapping("/stories/{storyId}/chapters/{chapterNumber}/ratings")
    public ResponseEntity<ApiResponseDTO> getChapterRatings(
            @PathVariable String storyId,
            @PathVariable Integer chapterNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            UUID storyUuid = UUID.fromString(storyId);
            Page<ChapterRating> ratingsPage = chapterRatingService.getChapterRatings(
                storyUuid, chapterNumber, page, size);
            
            List<ChapterRatingResponseDTO> ratings = ratingsPage.getContent().stream()
                .map(rating -> {
                    User user = userService.getUserById(rating.getUserId().toString());
                    String username = user != null ? user.getUsername() : "Usuario desconocido";
                    return new ChapterRatingResponseDTO(rating, username);
                })
                .collect(Collectors.toList());

            ChapterRatingsPageResponse response = new ChapterRatingsPageResponse();
            response.setRatings(ratings);
            response.setCurrentPage(ratingsPage.getNumber());
            response.setTotalPages(ratingsPage.getTotalPages());
            response.setTotalElements(ratingsPage.getTotalElements());
            response.setHasNext(ratingsPage.hasNext());
            response.setHasPrevious(ratingsPage.hasPrevious());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Calificaciones del capítulo obtenidas", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    /**
     * Delete a chapter rating
     */
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<ApiResponseDTO> deleteChapterRating(@PathVariable String ratingId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID ratingUuid = UUID.fromString(ratingId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            chapterRatingService.deleteChapterRating(ratingUuid, userUuid);

            return ResponseEntity.ok(new ApiResponseDTO(true, "Calificación eliminada exitosamente", null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    // DTOs internos
    public static class RatingRequest {
        private Integer ratingValue;

        public Integer getRatingValue() { return ratingValue; }
        public void setRatingValue(Integer ratingValue) { this.ratingValue = ratingValue; }
    }

    public static class ChapterRatingsPageResponse {
        private List<ChapterRatingResponseDTO> ratings;
        private int currentPage;
        private int totalPages;
        private long totalElements;
        private boolean hasNext;
        private boolean hasPrevious;

        // Getters and setters
        public List<ChapterRatingResponseDTO> getRatings() { return ratings; }
        public void setRatings(List<ChapterRatingResponseDTO> ratings) { this.ratings = ratings; }
        
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