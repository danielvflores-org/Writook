// Service for handling chapter-specific ratings
const API_BASE = 'http://localhost:8080/api/v1/chapters';

export const chapterRatingService = {
  // Rate a chapter
  async rateChapter(storyId, chapterNumber, ratingValue) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories/${storyId}/chapters/${chapterNumber}/ratings`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ ratingValue })
    });

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        throw new Error('Access denied');
      }
      
      let errorMessage = 'Error rating chapter';
      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        // Response is not JSON, use status text
        errorMessage = response.statusText || errorMessage;
      }
      throw new Error(errorMessage);
    }

    return response.json();
  },

  // Get user's rating for a chapter
  async getUserChapterRating(storyId, chapterNumber) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      return null; // No token means no rating
    }

    try {
      const response = await fetch(`${API_BASE}/stories/${storyId}/chapters/${chapterNumber}/ratings/user`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });

      if (!response.ok) {
        if (response.status === 401 || response.status === 403) {
          return null;
        }
        throw new Error('Error fetching user rating');
      }

      const result = await response.json();
      return result.data ? result.data.ratingValue : null;
    } catch (error) {
      console.error('Error fetching user chapter rating:', error);
      return null;
    }
  },

  // Get chapter statistics
  async getChapterStats(storyId, chapterNumber) {
    const response = await fetch(`${API_BASE}/stories/${storyId}/chapters/${chapterNumber}/stats`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      let errorMessage = 'Error fetching chapter stats';
      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = response.statusText || errorMessage;
      }
      throw new Error(errorMessage);
    }

    const result = await response.json();
    return result.data || { averageRating: 0, totalRatings: 0, totalComments: 0, views: 0 };
  },

  // Get all ratings for a chapter (with pagination)
  async getChapterRatings(storyId, chapterNumber, page = 0, size = 10) {
    const response = await fetch(
      `${API_BASE}/stories/${storyId}/chapters/${chapterNumber}/ratings?page=${page}&size=${size}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    if (!response.ok) {
      let errorMessage = 'Error fetching chapter ratings';
      try {
        const errorData = await response.json();
        errorMessage = errorData.message || errorMessage;
      } catch (e) {
        errorMessage = response.statusText || errorMessage;
      }
      throw new Error(errorMessage);
    }

    return response.json();
  },

  // Delete a chapter rating
  async deleteChapterRating(ratingId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/ratings/${ratingId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error deleting rating');
    }

    return response.json();
  }
};