// Service for handling story operations with statistics
import { apiService, endpoints } from './apiService.js';

export const storyService = {
  // Get all stories with statistics
  async getAllStoriesWithStats() {
    const response = await apiService.publicFetch(endpoints.stories.withStats);

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error fetching stories with stats');
    }

    return response.json();
  },

  // Get user's own stories
  async getUserStories() {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories/me`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error fetching user stories');
    }

    return response.json();
  },

  // Get stories by author username
  async getStoriesByAuthor(username) {
    const response = await fetch(`${API_BASE}/stories/author/${username}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error fetching author stories');
    }

    return response.json();
  },

  // Get single story by ID
  async getStoryById(storyId) {
    const response = await fetch(`${API_BASE}/stories/${storyId}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error fetching story');
    }

    return response.json();
  },

  // Get story statistics
  async getStoryStats(storyId) {
    const response = await fetch(`${API_BASE}/stories/${storyId}/stats`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        throw new Error('Access denied');
      }
      
      let errorMessage = 'Error fetching story stats';
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

  // Create a new story
  async createStory(storyData) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(storyData)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error creating story');
    }

    return response.json();
  },

  // Update story metadata
  async updateStoryMetadata(storyId, metadata) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories/${storyId}/metadata`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(metadata)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error updating story metadata');
    }

    return response.json();
  },

  // Check story ownership
  async checkStoryOwnership(storyId) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories/${storyId}/ownership`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!response.ok) {
      // Handle non-JSON error responses
      if (response.status === 403 || response.status === 401) {
        throw new Error('Access denied');
      }
      
      let errorMessage = 'Error checking story ownership';
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

  // Add comment to story (using old endpoint for compatibility)
  async addComment(storyId, content) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories/${storyId}/comments`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify({ content })
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.message || 'Error adding comment');
    }

    return response.json();
  }
};