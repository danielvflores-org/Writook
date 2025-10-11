// Service for handling chapter-specific comments
const API_BASE = 'http://localhost:8080/api/v1/chapters';

export const chapterCommentService = {
  // Create a new chapter comment
  async createChapterComment(storyId, chapterNumber, content) {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('No authentication token');
    }

    const response = await fetch(`${API_BASE}/stories/${storyId}/chapters/${chapterNumber}/comments`, {
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

  // Get comments for a chapter with pagination
  async getChapterComments(storyId, chapterNumber, page = 0, size = 10) {
    const response = await fetch(
      `${API_BASE}/stories/${storyId}/chapters/${chapterNumber}/comments?page=${page}&size=${size}`,
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

  // Update a chapter comment
  async updateChapterComment(commentId, content) {
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

  // Delete a chapter comment
  async deleteChapterComment(commentId) {
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