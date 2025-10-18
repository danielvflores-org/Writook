import { createContext, useContext, useState, useEffect } from 'react';
import { ENV_CONFIG } from './environment';

// Create the context
const AuthContext = createContext();

// AuthProvider component
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('authToken');
    const cachedUser = localStorage.getItem('authUser');

    // If we have a cached user, restore it synchronously so the UI can react immediately.
    if (cachedUser) {
      try {
        setUser(JSON.parse(cachedUser));
      } catch (e) {
        // invalid cached user -> remove
        localStorage.removeItem('authUser');
      }
    }

    if (!token) {
      setLoading(false);
      return;
    }

    // Verify token in background; this will correct/clear the cached user if token is invalid
    verifyToken(token);
  }, []); // This will only run ONCE when the provider mounts

  const verifyToken = async (token) => {
    try {
      const response = await fetch(`${ENV_CONFIG.API_BASE_URL}/auth/me`, {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (response.ok) {
        const userData = await response.json();
        setUser(userData.data);
        // refresh cached user
        try { localStorage.setItem('authUser', JSON.stringify(userData.data)); } catch(e){}
      } else {
        // Token is invalid
        localStorage.removeItem('authToken');
        localStorage.removeItem('authUser');
        setUser(null);
      }
    } catch (error) {
      console.error('Error verifying token:', error);
      // Network error or server down
      localStorage.removeItem('authToken');
      localStorage.removeItem('authUser');
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  const login = (token, userData) => {
    localStorage.setItem('authToken', token);
    // cache user for immediate restoration on reload
    try { localStorage.setItem('authUser', JSON.stringify(userData)); } catch(e){}
    setUser(userData);
    setLoading(false);
  };

  const logout = () => {
    localStorage.removeItem('authToken');
    setUser(null);
  };

  const value = {
    user,
    loading,
    setUser,
    login,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use the auth context
const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export default useAuth;
