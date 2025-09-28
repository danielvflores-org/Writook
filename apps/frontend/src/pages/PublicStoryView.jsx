import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useAuth from '../config/AuthContext';
import Notification from '../components/Notification';
import Layout from '../components/Layout';
import { useNotification } from '../hooks/useNotification';

export default function PublicStoryView() {
  const { storyId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [story, setStory] = useState(null);
  const [loading, setLoading] = useState(true);
  const { notification, showNotification, hideNotification } = useNotification();
  const [isOwner, setIsOwner] = useState(false);
  const [isSynopsisExpanded, setIsSynopsisExpanded] = useState(false);

  useEffect(() => {
    loadStory();
  }, [storyId]);

  const loadStory = async () => {
    try {
      setLoading(true);
      const response = await fetch(`http://localhost:8080/api/v1/stories/${storyId}`);
      
      if (response.ok) {
        const storyData = await response.json();
        setStory(storyData);
        
        // Check ownership if user is logged in
        if (user) {
          await checkOwnership(storyData);
        }
      } else {
        throw new Error('Story not found');
      }
    } catch (error) {
      showNotification('Error loading story', 'error');
      navigate('/home');
    } finally {
      setLoading(false);
    }
  };

  const checkOwnership = async (storyData) => {
    try {
      // First check if the author username matches current user
      const authorUsername = storyData.author?.username;
      if (authorUsername) {
        // Extract username from email if needed (legacy compatibility)
        const cleanAuthorUsername = authorUsername.includes('@') 
          ? authorUsername.substring(0, authorUsername.indexOf('@'))
          : authorUsername;
        
        if (cleanAuthorUsername === user.username) {
          setIsOwner(true);
          return;
        }
      }

      // If no direct match, try ownership endpoint
      const authToken = localStorage.getItem('authToken');
      if (authToken) {
        const ownershipResponse = await fetch(`http://localhost:8080/api/v1/stories/${storyId}/ownership`, {
          headers: {
            'Authorization': `Bearer ${authToken}`
          }
        });
        
        if (ownershipResponse.ok) {
          setIsOwner(true);
        }
      }
    } catch (error) {
      // If ownership check fails, user is not owner (or not logged in)
      console.log('Ownership check failed:', error);
      setIsOwner(false);
    }
  };

  const handleReadChapter = (chapterNumber) => {
    navigate(`/read/${storyId}/${chapterNumber}`);
  };

  const handleShareStory = () => {
    const publicUrl = `${window.location.origin}/story/${storyId}`;
    navigator.clipboard.writeText(publicUrl).then(() => {
      showNotification('Story URL copied to clipboard!', 'success');
    }).catch(() => {
      showNotification('Could not copy to clipboard', 'warning');
    });
  };

  const handleShareChapter = (chapterNumber) => {
    const publicUrl = `${window.location.origin}/read/${storyId}/${chapterNumber}`;
    navigator.clipboard.writeText(publicUrl).then(() => {
      showNotification('Chapter URL copied to clipboard!', 'success');
    }).catch(() => {
      showNotification('Could not copy to clipboard', 'warning');
    });
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading story...</p>
        </div>
      </div>
    );
  }

  if (!story) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Story not found</h2>
          <button 
            onClick={() => navigate('/home')}
            className="text-indigo-600 hover:text-indigo-800 font-medium"
          >
            Return to home
          </button>
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
                  <span className="text-white font-bold text-sm">📖</span>
                </div>
                <h1 className="text-3xl font-bold text-gray-900">{story.title}</h1>
              </div>
              <p className="text-gray-600 mb-4">By {story.author?.displayName || story.author?.username || story.author}</p>
            </div>
            
            <div className="flex items-center space-x-3">
              {isOwner && (
                <button 
                  onClick={() => navigate(`/myworks/${storyId}`)}
                  className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg text-sm flex items-center space-x-2 font-medium transition-colors"
                >
                  <span>✏️</span>
                  <span>Edit Story</span>
                </button>
              )}
              <button 
                onClick={handleShareStory}
                className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg flex items-center space-x-2 font-medium transition-colors"
              >
                <span>🔗</span>
                <span>Share Story</span>
              </button>
            </div>
          </div>
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          
          {/* Story Cover and Metadata */}
          <div className="lg:col-span-1">
            <div className="bg-white rounded-xl shadow-lg overflow-hidden">
              <div className="aspect-[3/4] bg-gradient-to-br from-blue-400 to-purple-600 flex items-center justify-center relative">
                <div className="text-center text-white">
                  <div className="text-6xl mb-3">📖</div>
                  <div className="text-sm font-medium">Preview</div>
                </div>
              </div>
              
              <div className="p-6 space-y-4">
                <div className="flex justify-between items-center">
                  <span className="text-gray-600 text-sm">Rating:</span>
                  <div className="flex items-center space-x-1">
                    <span className="text-yellow-400">⭐</span>
                    <span className="font-medium text-sm">{story.rating || 0}</span>
                  </div>
                </div>
                
                <div className="flex justify-between items-center">
                  <span className="text-gray-600 text-sm">Chapters:</span>
                  <span className="font-semibold text-lg">{story.chapters.length}</span>
                </div>
                
                <div className="flex justify-between items-center">
                  <span className="text-gray-600 text-sm">Status:</span>
                  <span className="text-green-600 bg-green-100 px-2 py-1 rounded-full text-xs font-medium">
                    In Progress
                  </span>
                </div>
                
                {story.genres && story.genres.length > 0 && (
                  <div className="space-y-2">
                    <span className="text-gray-600 text-sm block">Genres</span>
                    <div className="flex flex-wrap gap-1">
                      {story.genres.map((genre, index) => (
                        <span 
                          key={index}
                          className="bg-blue-100 text-blue-800 px-2 py-1 rounded-full text-xs font-medium"
                        >
                          {genre}
                        </span>
                      ))}
                    </div>
                  </div>
                )}
                
                {story.tags && story.tags.length > 0 && (
                  <div className="space-y-2">
                    <span className="text-gray-600 text-sm block">Tags</span>
                    <div className="flex flex-wrap gap-1">
                      {story.tags.map((tag, index) => (
                        <span 
                          key={index}
                          className="bg-gray-100 text-gray-700 px-2 py-1 rounded-full text-xs"
                        >
                          #{tag}
                        </span>
                      ))}
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
          
          {/* Main Content Area */}
          <div className="lg:col-span-2 space-y-6">
            {/* Synopsis Section */}
            {story.synopsis && (
              <div className="bg-white rounded-xl shadow-lg p-6">
                <h3 className="text-lg font-semibold text-gray-800 mb-3">Synopsis</h3>
                <div className="text-gray-700 leading-relaxed">
                  {(() => {
                    const synopsisLength = story.synopsis.length;
                    const maxLength = 300;
                    
                    if (synopsisLength <= maxLength) {
                      return <p>{story.synopsis}</p>;
                    }
                    
                    if (isSynopsisExpanded) {
                      return (
                        <div>
                          <p>{story.synopsis}</p>
                          <button
                            onClick={() => setIsSynopsisExpanded(false)}
                            className="text-blue-600 hover:text-blue-800 text-sm mt-3 flex items-center space-x-1"
                          >
                            <span>Show less</span>
                            <span>▲</span>
                          </button>
                        </div>
                      );
                    }
                    
                    return (
                      <div>
                        <p>{story.synopsis.substring(0, maxLength)}...</p>
                        <button
                          onClick={() => setIsSynopsisExpanded(true)}
                          className="text-blue-600 hover:text-blue-800 text-sm mt-3 flex items-center space-x-1"
                        >
                          <span>Read more</span>
                          <span>▼</span>
                        </button>
                      </div>
                    );
                  })()}
                </div>
              </div>
            )}

            {/* Chapters Section */}
            <div className="bg-white rounded-xl shadow-lg p-6">
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-xl font-semibold text-gray-800">Chapters</h2>
                {isOwner && story.chapters.length > 0 && (
                  <button
                    onClick={() => navigate(`/myworks/${storyId}/new-chapter`)}
                    className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-lg font-medium text-sm transition-colors"
                  >
                    + New Chapter
                  </button>
                )}
              </div>

              {story.chapters.length === 0 ? (
                <div className="text-center py-12 text-gray-500">
                  <div className="text-4xl mb-4">📝</div>
                  <p className="text-lg font-medium mb-2">This story doesn't have chapters yet</p>
                  <p className="text-sm mb-4">The author hasn't published any chapters yet.</p>
                  {isOwner && (
                    <button
                      onClick={() => navigate(`/myworks/${storyId}/new-chapter`)}
                      className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 font-medium transition-colors"
                    >
                      ✨ Write First Chapter
                    </button>
                  )}
                </div>
              ) : (
                <div className="space-y-3">
                  {story.chapters.map((chapter, index) => (
                    <div key={index} className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50 transition-colors">
                      <div className="flex items-center justify-between">
                        <div className="flex-1">
                          <div className="flex items-center space-x-3">
                            <div className="w-8 h-8 bg-blue-100 rounded-full flex items-center justify-center text-blue-600 font-medium text-sm">
                              {chapter.number}
                            </div>
                            <div>
                              <h4 
                                className="font-medium text-gray-800 hover:text-blue-600 cursor-pointer transition-colors"
                                onClick={() => handleReadChapter(chapter.number)}
                              >
                                {chapter.title}
                              </h4>
                              <div className="flex items-center space-x-4 text-xs text-gray-500 mt-1">
                                <span>Published • Sep 27, 2025</span>
                                <div className="flex items-center space-x-1">
                                  <span>⭐</span>
                                  <span>4</span>
                                </div>
                                <div className="flex items-center space-x-1">
                                  <span>💬</span>
                                  <span>0</span>
                                </div>
                                <div className="flex items-center space-x-1">
                                  <span>👁</span>
                                  <span>62</span>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div className="flex items-center space-x-2">
                          {isOwner && (
                            <button
                              onClick={() => navigate(`/myworks/${storyId}/edit/${chapter.number}`)}
                              className="p-2 text-gray-400 hover:text-green-600 transition-colors"
                              title="Edit chapter"
                            >
                              <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z"/>
                              </svg>
                            </button>
                          )}
                          <button
                            onClick={() => handleShareChapter(chapter.number)}
                            className="p-2 text-gray-400 hover:text-blue-600 transition-colors"
                            title="Share chapter"
                          >
                            <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                              <path d="M15 8a3 3 0 10-2.977-2.63l-4.94 2.47a3 3 0 100 4.319l4.94 2.47a3 3 0 10.895-1.789l-4.94-2.47a3.027 3.027 0 000-.74l4.94-2.47C13.456 7.68 14.19 8 15 8z"/>
                            </svg>
                          </button>
                          <button
                            onClick={() => handleReadChapter(chapter.number)}
                            className="p-2 text-gray-400 hover:text-green-600 transition-colors"
                            title="Read chapter"
                          >
                            <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                              <path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
                              <path fillRule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clipRule="evenodd"/>
                            </svg>
                          </button>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}
