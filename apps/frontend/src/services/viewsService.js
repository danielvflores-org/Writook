// Utility functions for chapter view management
export const viewsService = {
  // Get views for a specific chapter
  getChapterViews(storyId, chapterNumber) {
    const viewKey = `chapter_${storyId}_${chapterNumber}_views`;
    return parseInt(localStorage.getItem(viewKey) || '0');
  },

  // Increment views for a chapter
  incrementChapterViews(storyId, chapterNumber) {
    const viewKey = `chapter_${storyId}_${chapterNumber}_views`;
    const currentViews = this.getChapterViews(storyId, chapterNumber);
    const newViews = currentViews + 1;
    localStorage.setItem(viewKey, newViews.toString());
    return newViews;
  },

  // Set views for a chapter (for manual setting)
  setChapterViews(storyId, chapterNumber, views) {
    const viewKey = `chapter_${storyId}_${chapterNumber}_views`;
    localStorage.setItem(viewKey, views.toString());
  },

  // Get all views for a story (all chapters)
  getStoryViews(storyId, chapters) {
    return chapters.reduce((total, chapter) => {
      return total + this.getChapterViews(storyId, chapter.number);
    }, 0);
  },

  // Check if user has viewed a chapter
  hasViewedChapter(storyId, chapterNumber) {
    return this.getChapterViews(storyId, chapterNumber) > 0;
  }
};