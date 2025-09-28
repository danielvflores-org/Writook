import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import useAuth from '../config/AuthContext.jsx';
import Notification from '../components/Notification';
import Layout from '../components/Layout';
import { useNotification } from '../hooks/useNotification';

export default function ReadChapter() {
  const { storyId, chapterNumber } = useParams();
  const navigate = useNavigate();
  const { user } = useAuth();
  
  const [story, setStory] = useState(null);
  const [chapter, setChapter] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isOwner, setIsOwner] = useState(false);
  const { notification, showNotification, hideNotification } = useNotification();

  useEffect(() => {
    loadChapterData();
  }, [storyId, chapterNumber, user]);

  const loadChapterData = async () => {
    try {
      setLoading(true);
      
      // Load story data (public endpoint)
      const response = await fetch(`http://localhost:8080/api/v1/stories/${storyId}`);
      if (!response.ok) throw new Error('Story not found');
      
      const storyData = await response.json();
      setStory(storyData);
      
      // Check if current user is the story owner
      if (user && storyData.author && storyData.author.username === user.username) {
        setIsOwner(true);
      }
      
      const chapterData = storyData.chapters.find(ch => ch.number === parseInt(chapterNumber));
      if (!chapterData) throw new Error('Chapter not found');
      
      setChapter(chapterData);
      
    } catch (error) {
      showNotification('Error loading chapter', 'error');
      navigate('/home');
    } finally {
      setLoading(false);
    }
  };

  const handleBack = () => {
    if (isOwner) {
      // If owner, go to private workspace
      navigate(`/myworks/${storyId}`);
    } else {
      // If not owner, go to public story view
      navigate(`/story/${storyId}`);
    }
  };

  const handleEdit = () => {
    navigate(`/myworks/${storyId}/edit/${chapterNumber}`);
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-b-2 border-blue-600 mx-auto"></div>
          <p className="mt-4 text-gray-600">Loading chapter...</p>
        </div>
      </div>
    );
  }

  if (!story || !chapter) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-gray-800 mb-4">Chapter not found</h2>
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

      {/* Header */}
      <div className="bg-white shadow-lg border-b sticky top-0 z-10 backdrop-blur-sm bg-white/95">
        <div className="max-w-4xl mx-auto px-4 py-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center space-x-4">
              <button
                onClick={handleBack}
                className="text-gray-600 hover:text-gray-800 p-2 rounded-lg hover:bg-gray-100 transition-all duration-200 hover:shadow-md"
              >
                <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
                </svg>
              </button>
              <div>
                <div className="text-sm text-gray-500">
                  {story.title}
                </div>
                <div className="font-medium text-gray-800">
                  {chapter.title}
                </div>
              </div>
            </div>
            
            <div className="flex items-center space-x-2">
              {isOwner && (
                <button
                  onClick={handleEdit}
                  className="text-gray-600 hover:text-blue-600 p-2 rounded-lg hover:bg-gray-100 transition-colors"
                  title="Edit chapter"
                >
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                  </svg>
                </button>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* Story Info */}
      <div className="bg-gradient-to-r from-white to-blue-50/30 border-b shadow-sm">
        <div className="max-w-4xl mx-auto px-4 py-8">
          <div className="flex items-center space-x-6">
            <div className="w-14 h-18 bg-gradient-to-br from-blue-500 to-purple-600 rounded-xl flex items-center justify-center shadow-lg transform hover:scale-105 transition-transform duration-200">
              <span className="text-2xl text-white">📖</span>
            </div>
            <div>
              <h1 className="text-3xl font-bold text-gray-800 mb-1">{story.title}</h1>
              <p className="text-xl font-semibold text-blue-600 mb-2">{chapter.title}</p>
              <div className="flex items-center space-x-4 text-sm text-gray-600">
                <span className="bg-white/80 px-3 py-1 rounded-full shadow-sm">Chapter {chapterNumber}</span>
                <span className="text-gray-400">•</span>
                <span className="bg-white/80 px-3 py-1 rounded-full shadow-sm">Published {new Date().toLocaleDateString()}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Chapter Content */}
      <div className="max-w-4xl mx-auto px-4 py-8">
        <div className="bg-white rounded-2xl shadow-xl border border-gray-100 p-10 hover:shadow-2xl transition-shadow duration-300">
          {/* Chapter Title */}
          <div className="mb-10 text-center">
            <h2 className="text-4xl font-bold text-gray-800 mb-6">
              {chapter.title}
            </h2>
            <div className="w-24 h-1.5 bg-gradient-to-r from-blue-600 to-purple-600 rounded-full mx-auto shadow-sm"></div>
          </div>

          {/* Chapter Content */}
          <div className="prose prose-lg max-w-none">
            <div 
              className="text-gray-800 leading-relaxed text-lg"
              dangerouslySetInnerHTML={{ __html: chapter.content }}
            />
          </div>

          {/* Chapter Footer */}
          <div className="mt-16 pt-8 border-t border-gray-100">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-6">
                <button className="flex items-center space-x-2 text-gray-600 hover:text-red-600 transition-all duration-200 px-4 py-2 rounded-lg hover:bg-red-50 hover:shadow-md">
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                  <span className="font-medium">Like</span>
                </button>
                <button className="flex items-center space-x-2 text-gray-600 hover:text-blue-600 transition-all duration-200 px-4 py-2 rounded-lg hover:bg-blue-50 hover:shadow-md">
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
                  </svg>
                  <span className="font-medium">Comment</span>
                </button>
                <span className="text-gray-500 text-sm bg-gray-50 px-3 py-1 rounded-full">{story.views || 0} views</span>
              </div>
            </div>
          </div>
        </div>

        {/* Comments Section */}
        <div className="mt-8 bg-white rounded-2xl shadow-xl border border-gray-100 p-10 hover:shadow-2xl transition-shadow duration-300">
          <div className="flex items-center space-x-3 mb-8">
            <div className="w-8 h-8 bg-gradient-to-r from-blue-500 to-purple-600 rounded-lg flex items-center justify-center">
              <svg className="w-4 h-4 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
              </svg>
            </div>
            <h3 className="text-2xl font-bold text-gray-800">Comments</h3>
          </div>
          <div className="text-center py-16 text-gray-500">
            <div className="w-16 h-16 bg-gradient-to-br from-blue-100 to-purple-100 rounded-2xl flex items-center justify-center mx-auto mb-6 shadow-lg">
              <div className="text-3xl">💬</div>
            </div>
            <p className="text-xl font-semibold mb-3 text-gray-700">Be the first to comment!</p>
            <p className="text-gray-500 max-w-md mx-auto leading-relaxed">Reader feedback motivates writers to continue their stories.</p>
          </div>
        </div>
      </div>
    </Layout>
  );
}
