-- V3__add_chapter_ratings_and_comments_tables.sql
-- Add chapter-specific ratings and comments tables

-- Create chapter ratings table
CREATE TABLE chapter_ratings (
    id UUID PRIMARY KEY,
    story_id UUID NOT NULL,
    chapter_number INTEGER NOT NULL,
    user_id UUID NOT NULL,
    rating_value INTEGER NOT NULL CHECK (rating_value >= 1 AND rating_value <= 5),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    
    -- Ensure one rating per user per chapter
    CONSTRAINT unique_user_chapter_rating UNIQUE (story_id, chapter_number, user_id),
    
    -- Foreign key constraints
    CONSTRAINT fk_chapter_rating_story FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    CONSTRAINT fk_chapter_rating_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create chapter comments table
CREATE TABLE chapter_comments (
    id UUID PRIMARY KEY,
    story_id UUID NOT NULL,
    chapter_number INTEGER NOT NULL,
    user_id UUID NOT NULL,
    content TEXT NOT NULL CHECK (length(content) > 0 AND length(content) <= 2000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_chapter_comment_story FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    CONSTRAINT fk_chapter_comment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_chapter_ratings_story_chapter ON chapter_ratings(story_id, chapter_number);
CREATE INDEX idx_chapter_ratings_user ON chapter_ratings(user_id);
CREATE INDEX idx_chapter_ratings_story_chapter_user ON chapter_ratings(story_id, chapter_number, user_id);

CREATE INDEX idx_chapter_comments_story_chapter ON chapter_comments(story_id, chapter_number);
CREATE INDEX idx_chapter_comments_user ON chapter_comments(user_id);
CREATE INDEX idx_chapter_comments_created_at ON chapter_comments(created_at DESC);
CREATE INDEX idx_chapter_comments_story_chapter_created ON chapter_comments(story_id, chapter_number, created_at DESC);

-- Grant permissions to application user if it exists
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'writook_user') THEN
        GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO writook_user;
        GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO writook_user;
    END IF;
END
$$;