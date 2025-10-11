-- V2__add_ratings_and_comments_tables.sql
-- Add ratings and comments tables to support story interactions

-- Drop existing comments table from V1 (it has different structure)
DROP TABLE IF EXISTS comments;

-- Create ratings table
CREATE TABLE ratings (
    id UUID PRIMARY KEY,
    story_id UUID NOT NULL,
    user_id UUID NOT NULL,
    rating_value INTEGER NOT NULL CHECK (rating_value >= 1 AND rating_value <= 5),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    
    -- Ensure one rating per user per story
    CONSTRAINT unique_user_story_rating UNIQUE (story_id, user_id),
    
    -- Foreign key constraints
    CONSTRAINT fk_rating_story FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    CONSTRAINT fk_rating_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create new comments table with proper structure
CREATE TABLE comments (
    id UUID PRIMARY KEY,
    story_id UUID NOT NULL,
    user_id UUID NOT NULL,
    content TEXT NOT NULL CHECK (length(content) > 0 AND length(content) <= 2000),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_comment_story FOREIGN KEY (story_id) REFERENCES stories(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_ratings_story_id ON ratings(story_id);
CREATE INDEX idx_ratings_user_id ON ratings(user_id);
CREATE INDEX idx_ratings_story_user ON ratings(story_id, user_id);

CREATE INDEX idx_comments_story_id ON comments(story_id);
CREATE INDEX idx_comments_user_id ON comments(user_id);
CREATE INDEX idx_comments_created_at ON comments(created_at DESC);
CREATE INDEX idx_comments_story_created ON comments(story_id, created_at DESC);