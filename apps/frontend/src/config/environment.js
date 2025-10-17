// Environment configuration for Writook frontend
// This file manages API endpoints and other environment-specific settings

const config = {
  development: {
    API_BASE_URL: 'http://localhost:8080/api/v1',
    APP_NAME: 'Writook (Dev)',
    DEBUG: true
  },
  production: {
    API_BASE_URL: import.meta.env.VITE_API_BASE_URL || 'https://your-backend-domain.railway.app/api/v1',
    APP_NAME: 'Writook',
    DEBUG: false
  }
};

// Determine environment based on Vite's MODE
const environment = import.meta.env.MODE || 'development';

// Export the configuration for the current environment
export const ENV_CONFIG = config[environment] || config.development;

// Utility function to get the full API URL
export const getApiUrl = (endpoint) => {
  return `${ENV_CONFIG.API_BASE_URL}${endpoint}`;
};

// Log current environment (only in development)
if (ENV_CONFIG.DEBUG) {
  console.log('🌍 Environment:', environment);
  console.log('🔗 API Base URL:', ENV_CONFIG.API_BASE_URL);
}