import { useState, useEffect } from 'react';
import useNotification from '../hooks/useNotification';

export default function EditStoryModal({ story, isOpen, onClose, onUpdate }) {
  const [formData, setFormData] = useState({
    title: '',
    synopsis: '',
    genres: [],
    tags: []
  });
  const [loading, setLoading] = useState(false);
  const { notification, showNotification, hideNotification } = useNotification();

  const availableGenres = [
    'Fantasía', 'Romance', 'Aventura', 'Misterio', 'Ciencia Ficción', 
    'Horror', 'Drama', 'Comedia', 'Slice of Life', 'Histórico', "Fanfics"
  ];

  useEffect(() => {
    if (story && isOpen) {
      setFormData({
        title: story.title,
        synopsis: story.synopsis,
        genres: story.genres || [],
        tags: story.tags || []
      });
    }
  }, [story, isOpen]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleGenreToggle = (genre) => {
    setFormData(prev => ({
      ...prev,
      genres: prev.genres.includes(genre)
        ? prev.genres.filter(g => g !== genre)
        : [...prev.genres, genre]
    }));
  };

  const handleTagsChange = (e) => {
    const tagsString = e.target.value;
    const tagsArray = tagsString.split(',').map(tag => tag.trim()).filter(tag => tag.length > 0);
    setFormData(prev => ({
      ...prev,
      tags: tagsArray
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData.title.trim()) {
      showNotification('Please enter a title for your story', 'warning');
      return;
    }

    if (!formData.synopsis.trim()) {
      showNotification('Please enter a synopsis for your story', 'warning');
      return;
    }

    if (formData.genres.length === 0) {
      showNotification('Please select at least one genre', 'warning');
      return;
    }

    setLoading(true);
    try {
      const storyData = {
        title: formData.title.trim(),
        synopsis: formData.synopsis.trim(),
        rating: story.rating,
        genres: formData.genres,
        tags: formData.tags
      };

      const response = await fetch(`http://localhost:8080/api/v1/stories/${story.id}/metadata`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: JSON.stringify(storyData)
      });

      if (response.ok) {
        const updatedStory = await response.json();
        showNotification('Story updated successfully! 🎉', 'success');
        onUpdate(updatedStory);
        onClose();
      } else {
        const errorData = await response.text();
        throw new Error(errorData || 'Failed to update story');
      }
    } catch (error) {
      console.error('Error updating story:', error);
      showNotification(`Error updating story: ${error.message}`, 'error');
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg max-w-2xl w-full mx-4 max-h-[90vh] overflow-y-auto">
        <div className="p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">Edit Story</h2>
            <button
              onClick={onClose}
              className="text-gray-500 hover:text-gray-700 text-2xl"
              disabled={loading}
            >
              ×
            </button>
          </div>

          {notification && (
            <div className={`mb-4 p-4 rounded-lg ${
              notification.type === 'success' ? 'bg-green-100 border border-green-400 text-green-700' :
              notification.type === 'error' ? 'bg-red-100 border border-red-400 text-red-700' :
              'bg-yellow-100 border border-yellow-400 text-yellow-700'
            }`}>
              <div className="flex justify-between items-center">
                <span>{notification.message}</span>
                <button onClick={hideNotification} className="ml-4 font-bold">×</button>
              </div>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-6">
            {/* Title */}
            <div>
              <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-2">
                Story Title *
              </label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleInputChange}
                placeholder="e.g. The Lost Kingdom of Dragons"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                maxLength={100}
                disabled={loading}
              />
              <p className="text-xs text-gray-500 mt-1">{formData.title.length}/100 characters</p>
            </div>

            {/* Synopsis */}
            <div>
              <label htmlFor="synopsis" className="block text-sm font-medium text-gray-700 mb-2">
                Synopsis *
              </label>
              <textarea
                id="synopsis"
                name="synopsis"
                value={formData.synopsis}
                onChange={handleInputChange}
                placeholder="Describe what your story is about. What adventures await your readers?"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 h-32 resize-none"
                maxLength={1000}
                disabled={loading}
              />
              <p className="text-xs text-gray-500 mt-1">{formData.synopsis.length}/1000 characters</p>
            </div>

            {/* Genres */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">
                Genres * (select up to 3)
              </label>
              <div className="grid grid-cols-3 gap-2">
                {availableGenres.map((genre) => (
                  <button
                    key={genre}
                    type="button"
                    onClick={() => handleGenreToggle(genre)}
                    disabled={loading || (!formData.genres.includes(genre) && formData.genres.length >= 3)}
                    className={`px-3 py-2 rounded-lg text-sm transition-colors ${
                      formData.genres.includes(genre)
                        ? 'bg-blue-600 text-white'
                        : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                    } ${(!formData.genres.includes(genre) && formData.genres.length >= 3) || loading
                        ? 'opacity-50 cursor-not-allowed'
                        : 'cursor-pointer'
                    }`}
                  >
                    {genre}
                  </button>
                ))}
              </div>
              <p className="text-xs text-gray-500 mt-1">Selected: {formData.genres.length}/3</p>
            </div>

            {/* Tags */}
            <div>
              <label htmlFor="tags" className="block text-sm font-medium text-gray-700 mb-2">
                Tags (optional)
              </label>
              <input
                type="text"
                id="tags"
                value={formData.tags.join(', ')}
                onChange={handleTagsChange}
                placeholder="adventure, magic, friendship (separate with commas)"
                className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                disabled={loading}
              />
              <p className="text-xs text-gray-500 mt-1">Separate tags with commas</p>
            </div>

            {/* Actions */}
            <div className="flex justify-end space-x-4 pt-4">
              <button
                type="button"
                onClick={onClose}
                disabled={loading}
                className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Cancel
              </button>
              <button
                type="submit"
                disabled={loading}
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center"
              >
                {loading && (
                  <svg className="animate-spin -ml-1 mr-3 h-4 w-4 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                  </svg>
                )}
                Update Story
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}