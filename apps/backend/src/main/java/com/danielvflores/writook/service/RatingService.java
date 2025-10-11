package com.danielvflores.writook.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.danielvflores.writook.model.Rating;
import com.danielvflores.writook.repository.RatingRepository;

@Service
@Transactional
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public Rating rateStory(UUID storyId, UUID userId, Integer ratingValue) {
        if (ratingValue < 1 || ratingValue > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Optional<Rating> existingRating = ratingRepository.findByStoryIdAndUserId(storyId, userId);
        
        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setRatingValue(ratingValue);
            return ratingRepository.save(rating);
        } else {
            Rating newRating = new Rating(storyId, userId, ratingValue);
            return ratingRepository.save(newRating);
        }
    }

    public Optional<Rating> getUserRatingForStory(UUID storyId, UUID userId) {
        return ratingRepository.findByStoryIdAndUserId(storyId, userId);
    }

    public List<Rating> getStoryRatings(UUID storyId) {
        return ratingRepository.findByStoryId(storyId);
    }

    public Double getAverageRating(UUID storyId) {
        Double average = ratingRepository.calculateAverageRatingByStoryId(storyId);
        return average != null ? Math.round(average * 10.0) / 10.0 : 0.0; // Redondear a 1 decimal
    }

    public Long getTotalRatings(UUID storyId) {
        return ratingRepository.countRatingsByStoryId(storyId);
    }

    public boolean deleteRating(UUID storyId, UUID userId) {
        Optional<Rating> rating = ratingRepository.findByStoryIdAndUserId(storyId, userId);
        if (rating.isPresent()) {
            ratingRepository.delete(rating.get());
            return true;
        }
        return false;
    }

    public List<Object[]> getRatingDistribution(UUID storyId) {
        return ratingRepository.getRatingDistributionByStoryId(storyId);
    }

    public List<Rating> getUserRatings(UUID userId) {
        return ratingRepository.findByUserId(userId);
    }
}