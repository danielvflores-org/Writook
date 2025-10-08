package com.danielvflores.writook.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danielvflores.writook.entity.StoryEntity;

@Repository
public interface StoryRepository extends JpaRepository<StoryEntity, UUID> {
    List<StoryEntity> findByAuthorUsername(String username);
    Optional<StoryEntity> findById(UUID id);
}
