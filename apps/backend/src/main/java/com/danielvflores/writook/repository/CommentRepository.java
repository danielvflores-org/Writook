package com.danielvflores.writook.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.danielvflores.writook.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
}
