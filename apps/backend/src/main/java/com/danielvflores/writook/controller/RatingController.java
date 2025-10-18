package com.danielvflores.writook.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.danielvflores.writook.dto.ApiResponseDTO;
import com.danielvflores.writook.dto.RatingResponseDTO;
import com.danielvflores.writook.model.Rating;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.service.AuthService;
import com.danielvflores.writook.service.RatingService;
import com.danielvflores.writook.service.UserService;

@RestController
@RequestMapping("/api/v1/ratings")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/stories/{storyId}")
    public ResponseEntity<ApiResponseDTO> rateStory(
            @PathVariable String storyId, 
            @RequestBody RatingRequest request) {
        
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            Rating rating = ratingService.rateStory(storyUuid, userUuid, request.getRatingValue());
            RatingResponseDTO response = new RatingResponseDTO(rating, currentUser.getUsername());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Valoración guardada exitosamente", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    @GetMapping("/stories/{storyId}")
    public ResponseEntity<ApiResponseDTO> getStoryRatings(@PathVariable String storyId) {
        try {
            UUID storyUuid = UUID.fromString(storyId);
            List<Rating> ratings = ratingService.getStoryRatings(storyUuid);
            
            List<RatingResponseDTO> response = ratings.stream()
                .map(rating -> {
                    User user = userService.getUserById(rating.getUserId().toString());
                    String username = user != null ? user.getUsername() : "Usuario desconocido";
                    return new RatingResponseDTO(rating, username);
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(new ApiResponseDTO(true, "Valoraciones obtenidas", response));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    @GetMapping("/stories/{storyId}/my-rating")
    public ResponseEntity<ApiResponseDTO> getMyRating(@PathVariable String storyId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            Optional<Rating> rating = ratingService.getUserRatingForStory(storyUuid, userUuid);
            
            if (rating.isPresent()) {
                RatingResponseDTO response = new RatingResponseDTO(rating.get(), currentUser.getUsername());
                return ResponseEntity.ok(new ApiResponseDTO(true, "Valoración encontrada", response));
            } else {
                return ResponseEntity.ok(new ApiResponseDTO(true, "No hay valoración", null));
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    @DeleteMapping("/stories/{storyId}")
    public ResponseEntity<ApiResponseDTO> deleteRating(@PathVariable String storyId) {
        try {
            User currentUser = authService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponseDTO(false, "Usuario no autenticado", null));
            }

            UUID storyUuid = UUID.fromString(storyId);
            UUID userUuid = UUID.fromString(currentUser.getId());

            boolean deleted = ratingService.deleteRating(storyUuid, userUuid);
            
            if (deleted) {
                return ResponseEntity.ok(new ApiResponseDTO(true, "Valoración eliminada", null));
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(new ApiResponseDTO(false, "ID de historia inválido", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponseDTO(false, "Error interno del servidor", null));
        }
    }

    public static class RatingRequest {
        private Integer ratingValue;

        public Integer getRatingValue() {
            return ratingValue;
        }

        public void setRatingValue(Integer ratingValue) {
            this.ratingValue = ratingValue;
        }
    }
}