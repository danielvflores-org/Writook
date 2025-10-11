package com.danielvflores.writook.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.danielvflores.writook.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    List<Rating> findByStoryId(UUID storyId);

    Optional<Rating> findByStoryIdAndUserId(UUID storyId, UUID userId);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.storyId = :storyId")
    Double calculateAverageRatingByStoryId(@Param("storyId") UUID storyId);

    @Query("SELECT COUNT(r) FROM Rating r WHERE r.storyId = :storyId")
    Long countRatingsByStoryId(@Param("storyId") UUID storyId);

    List<Rating> findByUserId(UUID userId);

    @Query("SELECT r.ratingValue, COUNT(r) FROM Rating r WHERE r.storyId = :storyId GROUP BY r.ratingValue ORDER BY r.ratingValue")
    List<Object[]> getRatingDistributionByStoryId(@Param("storyId") UUID storyId);
}