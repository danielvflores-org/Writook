import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useAuth from '../config/AuthContext.jsx';
import Notification from '../components/Notification';
import { useNotification } from '../hooks/useNotification';

export default function MyStoryDetails() {
  const { storyId } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [story, setStory] = useState(null);
  const [loading, setLoading] = useState(true);
  const { notification, showNotification, hideNotification } = useNotification();
  const [isOwner, setIsOwner] = useState(false);
  const [isEditingStory, setIsEditingStory] = useState(false);
  const [editedStory, setEditedStory] = useState({});
  const [isSynopsisExpanded, setIsSynopsisExpanded] = useState(false);

  useEffect(() => {
    loadStory();
  }, [storyId]);

  const loadStory = async () => {
    try {
      setLoading(true);

      const token = localStorage.getItem('authToken');
      
      const ownershipResponse = await fetch(`http://localhost:8080/api/v1/stories/${storyId}/ownership`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      
      if (ownershipResponse.ok) {
        const storyData = await ownershipResponse.json();
        setStory(storyData);
        setIsOwner(true);
        return;
      }
      
      if (ownershipResponse.status === 403) {
        const publicResponse = await fetch(`http://localhost:8080/api/v1/stories/${storyId}`);
        
        if (publicResponse.ok) {
          const storyData = await publicResponse.json();
          setStory(storyData);
          
          if (user && storyData.author && 
              (storyData.author.username === user.username || 
               storyData.author.username === user.sub)) {
            setIsOwner(true);
          } else {
            navigate(`/story/${storyId}`);
            return;
          }
        } else {
          throw new Error('Could not load story');
        }
      } else if (ownershipResponse.status === 404) {
        showNotification('Story not found', 'error');
        navigate('/home');
      } else if (ownershipResponse.status === 401) {
        showNotification('Your session has expired. Please log in again.', 'error');
        navigate('/home');
      } else {
        showNotification('Error loading story', 'error');
        navigate('/home');
      }
    } catch (error) {
      showNotification('Connection error loading story', 'error');
      navigate('/home');
    } finally {
      setLoading(false);
    }
  };

  const handleEditChapter = (chapterNumber) => {
    navigate(`/myworks/${storyId}/edit/${chapterNumber}`);
  };

  const handleReadChapter = (chapterNumber) => {
    navigate(`/read/${storyId}/${chapterNumber}`);
  };

  const handleShareStory = () => {
    if (user && story) {
      const publicUrl = `${window.location.origin}/story/${storyId}`;
      navigator.clipboard.writeText(publicUrl).then(() => {
        showNotification('Story URL copied to clipboard! 📋', 'success');
      }).catch(() => {
        prompt('Copy this URL to share your story:', publicUrl);
      });
    }
  };

  const handleShareChapter = (chapterNumber) => {
    if (user && story) {
      const publicUrl = `${window.location.origin}/read/${storyId}/${chapterNumber}`;
      navigator.clipboard.writeText(publicUrl).then(() => {
        showNotification('Chapter URL copied to clipboard! 📋', 'success');
      }).catch(() => {
        prompt('Copy this URL to share the chapter:', publicUrl);
      });
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto mb-4"></div>
          <p className="text-gray-600">Loading story...</p>
        </div>
      </div>
    );
  }

  if (!story) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Story not found</h2>
          <button 
            onClick={() => navigate('/home')}
            className="text-indigo-600 hover:text-indigo-800"
          >
            Return to home
          </button>
        </div>
      </div>
    );
  }

  const handleNewChapter = () => {
    navigate(`/myworks/${storyId}/new-chapter`);
  };

  const handleEditStory = () => {
    setEditedStory({
      title: story.title,
      synopsis: story.synopsis,
      genres: story.genres,
      tags: story.tags,
      rating: story.rating
    });
    setIsEditingStory(true);
  };

  const handleSaveStoryChanges = async () => {
    try {
      const token = localStorage.getItem('authToken');
      
      const response = await fetch(`http://localhost:8080/api/v1/stories/${storyId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify(editedStory)
      });

      if (response.ok) {
        const updatedStory = await response.json();
        setStory(updatedStory);
        setIsEditingStory(false);
        showNotification('Story updated successfully! ✨', 'success');
      } else {
        throw new Error('Error updating story');
      }
    } catch (error) {
      showNotification('Error updating story', 'error');
    }
  };

  const handleCancelEdit = () => {
    setIsEditingStory(false);
    setEditedStory({});
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      <Notification
        notification={notification}
        onClose={hideNotification}
      />
      
      <div className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <button
            onClick={() => navigate('/myworks')}
            className="flex items-center text-indigo-600 hover:text-indigo-800 mb-4"
          >
            ← Back to My Works
          </button>
          <div className="flex justify-between items-start">
            <div className="flex-1 mr-4">
              {!isEditingStory ? (
                <>
                  <h1 className="text-3xl font-bold text-gray-800 mb-2">{story.title}</h1>
                  <p className="text-gray-600 mb-4">by {story.author?.displayName || story.author?.username || story.author}</p>
                  {story.synopsis && (
                    <div className="text-gray-700 mb-4">
                      <h3 className="font-semibold mb-2">Synopsis:</h3>
                      {(() => {
                        const synopsisLength = story.synopsis.length;
                        const maxLength = 200;
                        
                        if (synopsisLength <= maxLength) {
                          return <p className="text-sm leading-relaxed">{story.synopsis}</p>;
                        }
                        
                        if (isSynopsisExpanded) {
                          return (
                            <div>
                              <p className="text-sm leading-relaxed">{story.synopsis}</p>
                              <button
                                onClick={() => setIsSynopsisExpanded(false)}
                                className="text-indigo-600 hover:text-indigo-800 text-xs mt-2 flex items-center space-x-1"
                              >
                                <span>Show less</span>
                                <span>▲</span>
                              </button>
                            </div>
                          );
                        }
                        
                        return (
                          <div>
                            <p className="text-sm leading-relaxed">{story.synopsis.substring(0, maxLength)}...</p>
                            <button
                              onClick={() => setIsSynopsisExpanded(true)}
                              className="text-indigo-600 hover:text-indigo-800 text-xs mt-2 flex items-center space-x-1"
                            >
                              <span>Read more</span>
                              <span>▼</span>
                            </button>
                          </div>
                        );
                      })()}
                    </div>
                  )}
                  {isOwner && (
                    <button
                      onClick={handleEditStory}
                      className="text-indigo-600 hover:text-indigo-800 text-sm flex items-center space-x-1"
                    >
                      <span>✏️</span>
                      <span>Edit Story Details</span>
                    </button>
                  )}
                </>
              ) : (
                <div className="space-y-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Title</label>
                    <input
                      type="text"
                      value={editedStory.title}
                      onChange={(e) => setEditedStory({...editedStory, title: e.target.value})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Synopsis</label>
                    <textarea
                      value={editedStory.synopsis || ''}
                      onChange={(e) => setEditedStory({...editedStory, synopsis: e.target.value})}
                      rows={4}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      placeholder="Brief description of your story..."
                    />
                  </div>
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">Genres (comma-separated)</label>
                    <input
                      type="text"
                      value={editedStory.genres ? editedStory.genres.join(', ') : ''}
                      onChange={(e) => setEditedStory({...editedStory, genres: e.target.value.split(',').map(g => g.trim()).filter(g => g)})}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
                      placeholder="Fantasy, Adventure, Romance..."
                    />
                  </div>
                  <div className="flex space-x-2">
                    <button
                      onClick={handleSaveStoryChanges}
                      className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg text-sm"
                    >
                      Save Changes
                    </button>
                    <button
                      onClick={handleCancelEdit}
                      className="border border-gray-300 hover:bg-gray-50 text-gray-700 px-4 py-2 rounded-lg text-sm"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              )}
            </div>
            <div className="flex-shrink-0">
              <button 
                onClick={handleShareStory}
                className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg flex items-center space-x-2"
              >
                <span>🔗</span>
                <span>Share Story</span>
              </button>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-4 gap-8">
          <div className="lg:col-span-1">
            <div className="bg-white rounded-xl shadow-lg p-6">
              <div className="aspect-[3/4] bg-gradient-to-br from-indigo-200 to-blue-200 rounded-lg mb-4 flex items-center justify-center">
                <div className="text-center text-gray-600">
                  <div className="text-4xl mb-2">📖</div>
                  <div className="text-sm">Story Cover</div>
                </div>
              </div>
              
              <div className="space-y-3 text-sm">
                <div className="flex justify-between">
                  <span className="text-gray-600">Chapters:</span>
                  <span className="font-medium">{story.chapters.length}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Views:</span>
                  <span className="font-medium">{story.views || 0}</span>
                </div>
                <div className="flex justify-between">
                  <span className="text-gray-600">Genres:</span>
                  <span className="font-medium">{story.genres.join(', ')}</span>
                </div>
              </div>
            </div>
          </div>

          <div className="lg:col-span-3">
            <div className="bg-white rounded-xl shadow-lg p-8">
              <div className="flex items-center justify-between mb-6">
                <h2 className="text-xl font-semibold text-gray-800">Chapters</h2>
                <button
                  onClick={handleNewChapter}
                  className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-lg"
                >
                  + New Chapter
                </button>
              </div>

              {story.chapters.length === 0 ? (
                <div className="text-center py-12 text-gray-500">
                  <div className="text-4xl mb-4">📝</div>
                  <p className="text-lg font-medium mb-2">Time to start writing!</p>
                  <p className="text-sm mb-4">Add your first chapter so readers can enjoy your story.</p>
                  <button
                    onClick={handleNewChapter}
                    className="bg-indigo-600 text-white px-6 py-3 rounded-lg hover:bg-indigo-700"
                  >
                    ✨ Write First Chapter
                  </button>
                </div>
              ) : (
                <div className="space-y-4">
                  {story.chapters.map((chapter, index) => (
                    <div key={index} className="border border-gray-200 rounded-lg p-4 hover:bg-gray-50">
                      <div className="flex items-center justify-between">
                        <div>
                          <h4 
                            className="font-medium text-gray-800 hover:text-indigo-600 cursor-pointer"
                            onClick={() => handleReadChapter(chapter.number)}
                          >
                            Chapter {chapter.number}: {chapter.title}
                          </h4>
                          <p className="text-sm text-gray-500 mt-1">
                            Last updated: {new Date().toLocaleDateString()}
                          </p>
                        </div>
                        <div className="flex items-center space-x-2">
                          <button
                            onClick={() => handleShareChapter(chapter.number)}
                            className="text-blue-500 hover:text-blue-700 p-2"
                            title="Share chapter"
                          >
                            🔗
                          </button>
                          <button
                            onClick={() => handleEditChapter(chapter.number)}
                            className="text-gray-500 hover:text-gray-700 p-2"
                            title="Edit chapter"
                          >
                            ✏️
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
    </div>
  );
}
