// API configuration service
// Centralizes all API endpoint management for the application

import { ENV_CONFIG } from '../config/environment.js';

class ApiService {
  constructor() {
    this.baseUrl = ENV_CONFIG.API_BASE_URL;
  }

  // Get the base API URL
  getBaseUrl() {
    return this.baseUrl;
  }

  // Build full API endpoint URL
  getEndpoint(path) {
    // Remove leading slash if present to avoid double slashes
    const cleanPath = path.startsWith('/') ? path.slice(1) : path;
    return `${this.baseUrl}/${cleanPath}`;
  }

  // Common request headers
  getHeaders(includeAuth = false) {
    const headers = {
      'Content-Type': 'application/json'
    };

    if (includeAuth) {
      const token = localStorage.getItem('authToken');
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }
    }

    return headers;
  }

  // Helper method for authenticated requests
  async authenticatedFetch(endpoint, options = {}) {
    const url = this.getEndpoint(endpoint);
    const headers = this.getHeaders(true);
    
    const config = {
      ...options,
      headers: {
        ...headers,
        ...options.headers
      }
    };

    return fetch(url, config);
  }

  // Helper method for public requests
  async publicFetch(endpoint, options = {}) {
    const url = this.getEndpoint(endpoint);
    const headers = this.getHeaders(false);
    
    const config = {
      ...options,
      headers: {
        ...headers,
        ...options.headers
      }
    };

    return fetch(url, config);
  }
}

// Export a singleton instance
export const apiService = new ApiService();

// Export individual endpoint builders for convenience
export const endpoints = {
  // Auth endpoints
  auth: {
    login: 'auth/login',
    register: 'auth/register'
  },
  
  // Story endpoints
  stories: {
    all: 'stories',
    withStats: 'stories/with-stats',
    byId: (id) => `stories/${id}`,
    ownership: (id) => `stories/${id}/ownership`,
    chapters: (id) => `stories/${id}/chapters`,
    chapter: (storyId, chapterNum) => `stories/${storyId}/chapters/${chapterNum}`
  },
  
  // Rating endpoints
  ratings: {
    story: (id) => `stories/${id}/ratings`,
    chapter: (storyId, chapterNum) => `chapters/stories/${storyId}/chapters/${chapterNum}/ratings`
  },
  
  // Comment endpoints
  comments: {
    story: (id) => `stories/${id}/comments`,
    chapter: (storyId, chapterNum) => `chapters/stories/${storyId}/chapters/${chapterNum}/comments`
  },
  
  // User endpoints
  users: {
    profile: 'users/profile'
  }
};