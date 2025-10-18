// Service for handling story comments
import { ENV_CONFIG } from '../config/environment.js';

const API_BASE = ENV_CONFIG.API_BASE_URL;

export const commentService = {
  // Create a new comment
  async createComment(storyId, content) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/comments/stories/${storyId}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ content })
    });

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        throw new Error('Access denied');
      }
      
      let errorMessage = 'Error creating comment';
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

  // Get comments for a story with pagination
  async getStoryComments(storyId, page = 0, size = 10) {
    const response = await fetch(
      `${API_BASE}/comments/stories/${storyId}?page=${page}&size=${size}`,
      {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        throw new Error('Access denied');
      }
      
      let errorMessage = 'Error fetching comments';
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

  // Update a comment
  async updateComment(commentId, content) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/comments/${commentId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ content })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error updating comment');
    }

    return response.json();
  },

  // Delete a comment
  async deleteComment(commentId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/comments/${commentId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error deleting comment');
    }

    return response.json();
  }
};