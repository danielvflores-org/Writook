package com.danielvflores.writook.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielvflores.writook.entity.UserEntity;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService() {}

    private static User entityToModel(UserEntity e) {
        if (e == null) return null;
        User u = new User(e.getUsername(), e.getId() != null ? e.getId().toString() : null, e.getEmail(), e.getPassword(), e.getDisplayName(), e.getBio(), e.getProfilePictureUrl());
        return u;
    }

    private static UserEntity modelToEntity(User u) {
        UserEntity e = new UserEntity();
        if (u.getId() != null) {
            e.setId(UUID.fromString(u.getId()));
        } else {
            e.setId(UUID.randomUUID());
        }
        e.setUsername(u.getUsername());
        e.setEmail(u.getEmail());
        e.setPassword(u.getPassword());
        e.setDisplayName(u.getDisplayName());
        e.setBio(u.getBio());
        e.setProfilePictureUrl(u.getProfilePictureUrl());
        return e;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream().map(UserService::entityToModel).collect(Collectors.toList());
    }

    public User getUserById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<UserEntity> opt = userRepository.findById(uuid);
            return opt.map(UserService::entityToModel).orElse(null);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public User createUser(User user) {
        UserEntity entity = modelToEntity(user);
        logger.info("Creating user username={} email={} id={}", entity.getUsername(), entity.getEmail(), entity.getId());
        UserEntity saved = userRepository.save(entity);
        logger.info("Saved user id={} username={}", saved.getId(), saved.getUsername());
        return entityToModel(saved);
    }

    public User updateUser(String id, User updatedUser) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<UserEntity> existing = userRepository.findById(uuid);
            if (existing.isPresent()) {
                UserEntity toSave = modelToEntity(updatedUser);
                toSave.setId(uuid);
                UserEntity saved = userRepository.save(toSave);
                return entityToModel(saved);
            }
        } catch (IllegalArgumentException ex) {
            // invalid uuid
        }
        return null;
    }

    public boolean deleteUser(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            if (userRepository.existsById(uuid)) {
                userRepository.deleteById(uuid);
                return true;
            }
        } catch (IllegalArgumentException ex) {
            // invalid uuid
        }
        return false;
    }

    public User getUserProfile(String id) {
        return getUserById(id);
    }

    public User findByUsername(String username) {
        Optional<UserEntity> opt = userRepository.findByUsername(username);
        return opt.map(UserService::entityToModel).orElse(null);
    }

    public User findByEmail(String email) {
        Optional<UserEntity> opt = userRepository.findByEmail(email);
        return opt.map(UserService::entityToModel).orElse(null);
    }
}
