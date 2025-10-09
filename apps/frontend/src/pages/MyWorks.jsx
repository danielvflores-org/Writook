import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import useAuth from '../config/AuthContext.jsx';
import Notification from '../components/Notification';
import Layout from '../components/Layout';
import { useNotification } from '../hooks/useNotification';

export default function MyWorks() {
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [stories, setStories] = useState([]);
  const [loading, setLoading] = useState(true);
  const { notification, showNotification, hideNotification } = useNotification();
  const { loading: authLoading } = useAuth();

  useEffect(() => {
    if (user?.username) {
      loadUserStories();
    }
  }, [user]);

  const loadUserStories = async () => {
    try {
      setLoading(true);
      const token = localStorage.getItem('authToken');
      const headers = {
        'Content-Type': 'application/json'
      };
      
      // IMPORTANT: If we have a token, include it in the Authorization header
      if (token) {
        headers['Authorization'] = `Bearer ${token}`;
      }

      // If we have a token, prefer the authenticated endpoint which uses the token to identify the user.
      const url = token ? `http://localhost:8080/api/v1/stories/me` : `http://localhost:8080/api/v1/stories/author/${user?.username}`;
      const response = await fetch(url, {
        headers
      });
      
      if (response.ok) {
        const userStories = await response.json();
        setStories(userStories);
      } else if (response.status === 404 || response.status === 403) {
        setStories([]);
      } else {
        throw new Error('Failed to load stories');
      }
    } catch (error) {
      console.log('Error loading stories (showing empty state):', error);
      setStories([]);
    } finally {
      setLoading(false);
    }
  };

  const handleStoryClick = (storyId) => {
    navigate(`/myworks/${storyId}`);
  };

  const handleCreateNew = () => {
    navigate('/create-story');
  };

  const handleViewPublic = (storyId) => {
    navigate(`/story/${storyId}`);
  };

  // Wait for auth verification (authLoading) or our local loading state
  if (authLoading || loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading your stories...</p>
        </div>
      </div>
    );
  }

  return (
    <Layout>
      <Notification
        notification={notification}
        onClose={hideNotification}
      />
      <div>
        <div className="mb-8">
          <button
            onClick={() => navigate('/home')}
            className="flex items-center text-blue-600 hover:text-blue-800 mb-6 font-medium"
          >
            ← Back to home
          </button>
          
          <div className="flex justify-between items-start">
            <div className="flex-1">
              <div className="flex items-center space-x-3 mb-2">
                <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
                  <span className="text-white font-bold text-sm">📚</span>
                </div>
                <h1 className="text-3xl font-bold text-gray-900">My Works</h1>
              </div>
              <p className="text-gray-600 mb-4">
                Manage and edit your stories • {stories.length} {stories.length === 1 ? 'story' : 'stories'}
              </p>
            </div>
            
            <div className="flex items-center space-x-3">
              <button
                onClick={handleCreateNew}
                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg flex items-center space-x-2 font-medium transition-colors"
              >
                <span>✨</span>
                <span>Create New Story</span>
              </button>
            </div>
          </div>
        </div>

        {stories.length === 0 ? (
          /* Empty State */
          <div className="text-center py-16">
            <div className="bg-white rounded-xl shadow-lg p-12 max-w-md mx-auto">
              <div className="text-6xl mb-6">📖</div>
              <h2 className="text-2xl font-bold text-gray-800 mb-4">Start Your Writing Journey</h2>
              <p className="text-gray-600 mb-8">
                You haven't created any stories yet. Share your imagination with the world!
              </p>
              <button
                onClick={handleCreateNew}
                className="bg-blue-600 text-white px-8 py-3 rounded-lg hover:bg-blue-700 transition-colors font-medium inline-flex items-center space-x-2"
              >
                <span>✨</span>
                <span>Create Your First Story</span>
              </button>
            </div>
          </div>
        ) : (
          /* Stories Grid */
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {stories.map((story) => (
              <div key={story.id} className="bg-white rounded-xl shadow-lg overflow-hidden hover:shadow-xl transition-shadow">
                {/* Story Cover */}
                <div className="aspect-[4/3] bg-gradient-to-br from-blue-400 to-purple-600 flex items-center justify-center">
                  <div className="text-center text-white">
                    <div className="text-4xl mb-2">📚</div>
                    <div className="text-sm font-medium">{story.title}</div>
                  </div>
                </div>
                
                {/* Story Info */}
                <div className="p-6">
                  <h3 className="text-xl font-bold text-gray-800 mb-2 line-clamp-2">
                    {story.title}
                  </h3>
                  <p className="text-gray-600 text-sm mb-4 line-clamp-3">
                    {story.synopsis}
                  </p>
                  
                  {/* Stats */}
                  <div className="flex items-center justify-between text-sm text-gray-500 mb-4">
                    <div className="flex items-center space-x-4">
                      <span className="flex items-center">
                        <span className="text-yellow-500">⭐</span>
                        <span className="ml-1">{story.rating || 0}</span>
                      </span>
                      <span className="flex items-center">
                        <span className="text-blue-500">📄</span>
                        <span className="ml-1">{story.chapters?.length || 0}</span>
                      </span>
                    </div>
                    <span className="text-green-600 text-xs bg-green-100 px-2 py-1 rounded-full">
                      In Progress
                    </span>
                  </div>

                  {/* Genres */}
                  <div className="flex flex-wrap gap-1 mb-4">
                    {story.genres?.slice(0, 2).map((genre, index) => (
                      <span key={index} className="px-2 py-1 bg-blue-100 text-blue-800 text-xs rounded-full font-medium">
                        {genre}
                      </span>
                    ))}
                    {story.genres?.length > 2 && (
                      <span className="px-2 py-1 bg-gray-100 text-gray-600 text-xs rounded-full">
                        +{story.genres.length - 2}
                      </span>
                    )}
                  </div>

                  {/* Actions */}
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleStoryClick(story.id)}
                      className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition-colors text-sm font-medium"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleViewPublic(story.id)}
                      className="flex-1 bg-gray-100 text-gray-700 px-4 py-2 rounded-lg hover:bg-gray-200 transition-colors text-sm font-medium"
                    >
                      View
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
}
