package com.danielvflores.writook.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.danielvflores.writook.dto.AuthorDTO;
import com.danielvflores.writook.entity.ChapterEntity;
import com.danielvflores.writook.entity.StoryEntity;
import com.danielvflores.writook.model.Chapter;
import com.danielvflores.writook.model.Story;
import com.danielvflores.writook.model.User;
import com.danielvflores.writook.repository.ChapterRepository;
import com.danielvflores.writook.repository.StoryRepository;

@Service
public class StoryService {

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private com.danielvflores.writook.repository.GenreRepository genreRepository;

    @Autowired
    private com.danielvflores.writook.repository.TagRepository tagRepository;

    @Autowired
    private com.danielvflores.writook.repository.CommentRepository commentRepository;

    public void addComment(String storyId, String chapterId, String authorId, String content) {
        try {
            UUID sId = UUID.fromString(storyId);
            UUID authorUuid = UUID.fromString(authorId);
            
            // Usar el nuevo sistema de comentarios
            com.danielvflores.writook.model.Comment comment = 
                new com.danielvflores.writook.model.Comment(sId, authorUuid, content);
            commentRepository.save(comment);
            
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("ID inválido");
        }
    }

    @Autowired
    private UserService userService;

    public StoryService() {}

    private static Story entityToModel(StoryEntity e) {
        if (e == null) return null;
        AuthorDTO author = new AuthorDTO(e.getAuthorUsername(), e.getAuthorEmail(), e.getAuthorDisplayName(), e.getAuthorBio(), e.getAuthorProfilePictureUrl());
        List<Chapter> chapters = e.getChapters().stream().map(c -> new Chapter(c.getTitle(), c.getContent(), c.getNumber())).collect(Collectors.toList());
        Story s = new Story(e.getTitle(), e.getSynopsis(), author, e.getRating(), List.of(), List.of(), chapters, e.getId() != null ? e.getId().toString() : null);
        return s;
    }

    private static StoryEntity modelToEntity(Story s) {
        StoryEntity e = new StoryEntity();
        if (s.getId() != null) {
            e.setId(UUID.fromString(s.getId()));
        } else {
            e.setId(UUID.randomUUID());
        }
        e.setTitle(s.getTitle());
        e.setSynopsis(s.getSynopsis());
        if (s.getAuthor() != null) {
            e.setAuthorUsername(s.getAuthor().getUsername());
            e.setAuthorEmail(s.getAuthor().getEmail());
            e.setAuthorDisplayName(s.getAuthor().getDisplayName());
            e.setAuthorBio(s.getAuthor().getBio());
            e.setAuthorProfilePictureUrl(s.getAuthor().getProfilePictureUrl());
        }
        return e;
    }

    public List<Story> getAllStories() {
        return storyRepository.findAll().stream().map(StoryService::entityToModel).collect(Collectors.toList());
    }

    public Story getStoryById(String id) {
        try {
            Optional<StoryEntity> opt = storyRepository.findById(UUID.fromString(id));
            return opt.map(StoryService::entityToModel).orElse(null);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public Story createStory(Story story) {
        StoryEntity e = modelToEntity(story);
        // try to resolve author id from existing users
        if (story.getAuthor() != null) {
            User user = null;
            if (story.getAuthor().getUsername() != null) user = userService.findByUsername(story.getAuthor().getUsername());
            if (user == null && story.getAuthor().getEmail() != null) user = userService.findByEmail(story.getAuthor().getEmail());
            if (user != null) e.setAuthorId(UUID.fromString(user.getId()));
        }
        // handle genres
        if (story.getGenres() != null) {
            java.util.Set<com.danielvflores.writook.entity.GenreEntity> genreEntities = new java.util.HashSet<>();
            for (String g : story.getGenres()) {
                com.danielvflores.writook.entity.GenreEntity ge = genreRepository.findByName(g).orElseGet(() -> {
                    com.danielvflores.writook.entity.GenreEntity n = new com.danielvflores.writook.entity.GenreEntity();
                    n.setId(UUID.randomUUID());
                    n.setName(g);
                    return genreRepository.save(n);
                });
                genreEntities.add(ge);
            }
            e.setGenres(genreEntities);
        }

        // handle tags
        if (story.getTags() != null) {
            java.util.Set<com.danielvflores.writook.entity.TagEntity> tagEntities = new java.util.HashSet<>();
            for (String t : story.getTags()) {
                com.danielvflores.writook.entity.TagEntity te = tagRepository.findByName(t).orElseGet(() -> {
                    com.danielvflores.writook.entity.TagEntity n = new com.danielvflores.writook.entity.TagEntity();
                    n.setId(UUID.randomUUID());
                    n.setName(t);
                    return tagRepository.save(n);
                });
                tagEntities.add(te);
            }
            e.setTags(tagEntities);
        }

        StoryEntity saved = storyRepository.save(e);

        // handle chapters
        if (story.getChapters() != null) {
            for (Chapter ch : story.getChapters()) {
                ChapterEntity ce = new ChapterEntity();
                ce.setId(UUID.randomUUID());
                ce.setStory(saved);
                ce.setNumber(ch.getNumber());
                ce.setTitle(ch.getTitle());
                ce.setContent(ch.getContent());
                chapterRepository.save(ce);
            }
        }

        return entityToModel(storyRepository.findById(saved.getId()).get());
    }

    public Story updateStory(String id, Story updatedStory) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<StoryEntity> existing = storyRepository.findById(uuid);
            if (existing.isPresent()) {
                StoryEntity toSave = modelToEntity(updatedStory);
                toSave.setId(uuid);
                if (updatedStory.getAuthor() != null) {
                    User user = null;
                    if (updatedStory.getAuthor().getUsername() != null) user = userService.findByUsername(updatedStory.getAuthor().getUsername());
                    if (user == null && updatedStory.getAuthor().getEmail() != null) user = userService.findByEmail(updatedStory.getAuthor().getEmail());
                    if (user != null) toSave.setAuthorId(UUID.fromString(user.getId()));
                }
                // update genres/tags similar to create
                if (updatedStory.getGenres() != null) {
                    java.util.Set<com.danielvflores.writook.entity.GenreEntity> genreEntities = new java.util.HashSet<>();
                    for (String g : updatedStory.getGenres()) {
                        com.danielvflores.writook.entity.GenreEntity ge = genreRepository.findByName(g).orElseGet(() -> {
                            com.danielvflores.writook.entity.GenreEntity n = new com.danielvflores.writook.entity.GenreEntity();
                            n.setId(UUID.randomUUID());
                            n.setName(g);
                            return genreRepository.save(n);
                        });
                        genreEntities.add(ge);
                    }
                    toSave.setGenres(genreEntities);
                }
                if (updatedStory.getTags() != null) {
                    java.util.Set<com.danielvflores.writook.entity.TagEntity> tagEntities = new java.util.HashSet<>();
                    for (String t : updatedStory.getTags()) {
                        com.danielvflores.writook.entity.TagEntity te = tagRepository.findByName(t).orElseGet(() -> {
                            com.danielvflores.writook.entity.TagEntity n = new com.danielvflores.writook.entity.TagEntity();
                            n.setId(UUID.randomUUID());
                            n.setName(t);
                            return tagRepository.save(n);
                        });
                        tagEntities.add(te);
                    }
                    toSave.setTags(tagEntities);
                }
                StoryEntity saved = storyRepository.save(toSave);

                // replace chapters
                // delete old chapters for story
                chapterRepository.findAll().stream().filter(c -> c.getStory() != null && c.getStory().getId().equals(saved.getId())).forEach(c -> chapterRepository.delete(c));
                if (updatedStory.getChapters() != null) {
                    for (Chapter ch : updatedStory.getChapters()) {
                        ChapterEntity ce = new ChapterEntity();
                        ce.setId(UUID.randomUUID());
                        ce.setStory(saved);
                        ce.setNumber(ch.getNumber());
                        ce.setTitle(ch.getTitle());
                        ce.setContent(ch.getContent());
                        chapterRepository.save(ce);
                    }
                }
                return entityToModel(storyRepository.findById(saved.getId()).get());
            }
        } catch (IllegalArgumentException ex) {
        }
        return null;
    }

    public boolean deleteStory(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            if (storyRepository.existsById(uuid)) {
                storyRepository.deleteById(uuid);
                return true;
            }
        } catch (IllegalArgumentException ex) {}
        return false;
    }

    public List<Story> getStoriesByAuthorUsername(String username) {
        return storyRepository.findByAuthorUsername(username).stream().map(StoryService::entityToModel).collect(Collectors.toList());
    }

    public Chapter updateChapter(String storyId, Long chapterId, Chapter updatedChapter, String authHeader) {
        // Validate owner
        User authenticatedUser = validateTokenAndGetUser(authHeader);
        Story story = getStoryById(storyId);
        if (story == null) throw new RuntimeException("Historia no encontrada");
        if (!isStoryOwner(story, authenticatedUser)) throw new SecurityException("No autorizado: solo el autor puede editar capítulos");

        // Find chapter entity
        List<ChapterEntity> chapterEntities = chapterRepository.findAll().stream().filter(c -> c.getStory() != null && c.getStory().getId().toString().equals(storyId) && c.getNumber() == chapterId.intValue()).collect(Collectors.toList());
        if (chapterEntities.isEmpty()) throw new RuntimeException("Capítulo no encontrado");
        ChapterEntity ce = chapterEntities.get(0);
        ce.setTitle(updatedChapter.getTitle());
        ce.setContent(updatedChapter.getContent());
        chapterRepository.save(ce);
        return new Chapter(ce.getTitle(), ce.getContent(), ce.getNumber());
    }

    // Public helper to check ownership and return story (used by controllers)
    public Story getStoryWithOwnershipCheck(String id, String authHeader) {
        User authenticatedUser = validateTokenAndGetUser(authHeader);
        Story story = getStoryById(id);
        if (story == null) throw new RuntimeException("Historia no encontrada");
        if (!isStoryOwner(story, authenticatedUser)) throw new RuntimeException("No tienes permiso para acceder a este espacio de trabajo");
        return story;
    }

    private User validateTokenAndGetUser(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token de autorización requerido");
        }

        String token = authHeader.substring(7);
        if (!com.danielvflores.writook.utility.TokenJWTUtility.validateToken(token)) {
            throw new RuntimeException("Token inválido");
        }

        String userFromToken = com.danielvflores.writook.utility.TokenJWTUtility.getUsernameFromToken(token);
        if (userFromToken == null) throw new RuntimeException("No se pudo extraer usuario del token");

        User authenticatedUser = userService.findByUsername(userFromToken);
        if (authenticatedUser == null) authenticatedUser = userService.findByEmail(userFromToken);
        if (authenticatedUser == null) throw new RuntimeException("Usuario no encontrado");
        return authenticatedUser;
    }

    private boolean isStoryOwner(Story story, User authenticatedUser) {
        if (story == null || story.getAuthor() == null || authenticatedUser == null) return false;
        String authorUsername = story.getAuthor().getUsername();
        String authorEmail = story.getAuthor().getEmail();
        String authUserUsername = authenticatedUser.getUsername();
        String authUserEmail = authenticatedUser.getEmail();

        if (authUserUsername != null && authUserUsername.equals(authorUsername)) return true;
        if (authUserEmail != null && authorEmail != null && authUserEmail.equals(authorEmail)) return true;
        if (authorEmail != null && authUserUsername != null && authUserUsername.equals(authorEmail)) return true;
        if (authUserEmail != null && authUserEmail.equals(authorUsername)) return true;
        if (authorEmail != null && authorEmail.contains("@") && authUserUsername != null) {
            String localPart = authorEmail.substring(0, authorEmail.indexOf("@"));
            if (authUserUsername.equals(localPart)) return true;
        }
        if (authorUsername != null && authorUsername.contains("@") && authUserUsername != null) {
            String localPart = authorUsername.substring(0, authorUsername.indexOf("@"));
            if (authUserUsername.equals(localPart)) return true;
        }
        return false;
    }

    public List<Story> getTopRatedStories(int limit) {
        return storyRepository.findAll().stream().sorted((s1, s2) -> Double.compare(s2.getRating(), s1.getRating())).limit(limit).map(StoryService::entityToModel).collect(Collectors.toList());
    }

    public List<Story> searchStories(String query) {
        String lowerQuery = query.toLowerCase();
        return storyRepository.findAll().stream().filter(e -> (e.getTitle() != null && e.getTitle().toLowerCase().contains(lowerQuery)) || (e.getSynopsis() != null && e.getSynopsis().toLowerCase().contains(lowerQuery)) || (e.getAuthorDisplayName() != null && e.getAuthorDisplayName().toLowerCase().contains(lowerQuery))).map(StoryService::entityToModel).collect(Collectors.toList());
    }

    // Nuevos métodos para estadísticas reales
    @Autowired
    private com.danielvflores.writook.service.RatingService ratingService;

    @Autowired
    private com.danielvflores.writook.service.CommentService commentService;

    /**
     * Obtener estadísticas completas de una historia
     */
    public com.danielvflores.writook.dto.StoryStatsDTO getStoryStats(String storyId) {
        try {
            UUID storyUuid = UUID.fromString(storyId);
            
            // Obtener métricas reales
            Double averageRating = ratingService.getAverageRating(storyUuid);
            Long totalRatings = ratingService.getTotalRatings(storyUuid);
            Long totalComments = commentService.getTotalComments(storyUuid);
            
            // Contar capítulos
            Story story = getStoryById(storyId);
            Integer totalChapters = story != null && story.getChapters() != null ? 
                story.getChapters().size() : 0;
            
            return new com.danielvflores.writook.dto.StoryStatsDTO(
                storyUuid, averageRating, totalRatings, totalComments, totalChapters);
        } catch (Exception e) {
            // Retornar estadísticas vacías en caso de error
            return new com.danielvflores.writook.dto.StoryStatsDTO(
                UUID.fromString(storyId), 0.0, 0L, 0L, 0);
        }
    }

    /**
     * Obtener todas las historias con sus estadísticas
     */
    public List<StoryWithStatsDTO> getAllStoriesWithStats() {
        List<Story> stories = getAllStories();
        return stories.stream()
            .map(story -> {
                com.danielvflores.writook.dto.StoryStatsDTO stats = getStoryStats(story.getId());
                return new StoryWithStatsDTO(story, stats);
            })
            .collect(Collectors.toList());
    }

    // DTO para combinar historia con estadísticas
    public static class StoryWithStatsDTO {
        private Story story;
        private com.danielvflores.writook.dto.StoryStatsDTO stats;

        public StoryWithStatsDTO(Story story, com.danielvflores.writook.dto.StoryStatsDTO stats) {
            this.story = story;
            this.stats = stats;
        }

        public Story getStory() { return story; }
        public void setStory(Story story) { this.story = story; }
        
        public com.danielvflores.writook.dto.StoryStatsDTO getStats() { return stats; }
        public void setStats(com.danielvflores.writook.dto.StoryStatsDTO stats) { this.stats = stats; }
    }
}
