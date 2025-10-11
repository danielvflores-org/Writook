// Service for handling story ratings
const API_BASE = 'http://localhost:8080/api/v1';

export const ratingService = {
  // Rate a story (create or update rating)
  async rateStory(storyId, ratingValue) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/ratings/stories/${storyId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ ratingValue })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error rating story');
    }

    return response.json();
  },

  // Get all ratings for a story
  async getStoryRatings(storyId) {
    const response = await fetch(`${API_BASE}/ratings/stories/${storyId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error fetching ratings');
    }

    return response.json();
  },

  // Get current user's rating for a story
  async getMyRating(storyId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      return { success: true, data: null }; // No token means no rating
    }

    const response = await fetch(`${API_BASE}/ratings/stories/${storyId}/my-rating`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        return { success: true, data: null }; // Access denied means no rating
      }
      if (response.status === 404) {
        return { success: true, data: null }; // No rating found
      }
      
      let errorMessage = 'Error fetching user rating';
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

  // Delete user's rating for a story
  async deleteRating(storyId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/ratings/stories/${storyId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        throw new Error('Access denied');
      }
      
      let errorMessage = 'Error deleting rating';
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
  }
};