-- V1 initial schema for Writook
-- V1 initial schema for Writook
-- IDs use the PostgreSQL uuid type, but UUID values will be provided by the application
-- (no database-side default generator required) to avoid needing superuser to create extensions.

-- USERS
CREATE TABLE IF NOT EXISTS users (
  id UUID PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  email VARCHAR(254) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  display_name VARCHAR(150),
  bio TEXT,
  profile_picture_url TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- STORIES
CREATE TABLE IF NOT EXISTS stories (
  id UUID PRIMARY KEY,
  title VARCHAR(300) NOT NULL,
  synopsis TEXT,
  rating DOUBLE PRECISION DEFAULT 0,
  views BIGINT DEFAULT 0,
  author_id UUID REFERENCES users(id) ON DELETE SET NULL,
  author_username VARCHAR(100),
  author_email VARCHAR(254),
  author_display_name VARCHAR(150),
  author_bio TEXT,
  author_profile_picture_url TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_stories_author ON stories(author_id);
CREATE INDEX IF NOT EXISTS idx_stories_rating ON stories(rating DESC);

-- CHAPTERS
CREATE TABLE IF NOT EXISTS chapters (
  id UUID PRIMARY KEY,
  story_id UUID NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
  number INT NOT NULL,
  title VARCHAR(300) NOT NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  CONSTRAINT uq_chapter_story_number UNIQUE (story_id, number)
);

CREATE INDEX IF NOT EXISTS idx_chapters_story ON chapters(story_id);

-- GENRES and relationship
CREATE TABLE IF NOT EXISTS genres (
  id UUID PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS story_genres (
  story_id UUID NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
  genre_id UUID NOT NULL REFERENCES genres(id) ON DELETE CASCADE,
  PRIMARY KEY (story_id, genre_id)
);

-- TAGS and relationship
CREATE TABLE IF NOT EXISTS tags (
  id UUID PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS story_tags (
  story_id UUID NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
  tag_id UUID NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
  PRIMARY KEY (story_id, tag_id)
);

-- COMMENTS (optional)
CREATE TABLE IF NOT EXISTS comments (
  id UUID PRIMARY KEY,
  story_id UUID NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
  chapter_id UUID REFERENCES chapters(id) ON DELETE CASCADE,
  author_id UUID REFERENCES users(id) ON DELETE SET NULL,
  content TEXT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Function to update updated_at
CREATE OR REPLACE FUNCTION refresh_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers
CREATE TRIGGER trg_refresh_updated_at_users
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE PROCEDURE refresh_updated_at_column();

CREATE TRIGGER trg_refresh_updated_at_stories
BEFORE UPDATE ON stories
FOR EACH ROW EXECUTE PROCEDURE refresh_updated_at_column();

CREATE TRIGGER trg_refresh_updated_at_chapters
BEFORE UPDATE ON chapters
FOR EACH ROW EXECUTE PROCEDURE refresh_updated_at_column();

-- Grant permissions to application user if it exists
DO $$
BEGIN
    IF EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = 'writook_user') THEN
        GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO writook_user;
        GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO writook_user;
        GRANT USAGE ON SCHEMA public TO writook_user;
        GRANT EXECUTE ON FUNCTION refresh_updated_at_column() TO writook_user;
    END IF;
END
$$;
