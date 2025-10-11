import { useState, useEffect } from 'react';
import { commentService } from '../services/commentService';
import { chapterCommentService } from '../services/chapterCommentService';
import useAuth from '../config/AuthContext';

export default function CommentsSection({ storyId, chapterNumber = null }) {
  const { user } = useAuth();
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newComment, setNewComment] = useState('');
  const [submitting, setSubmitting] = useState(false);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const [error, setError] = useState('');

  // Determine if this is for a chapter or story
  const isChapterComment = chapterNumber !== null;
  const currentService = isChapterComment ? chapterCommentService : commentService;

  useEffect(() => {
    loadComments();
  }, [storyId, chapterNumber]);

  const loadComments = async (pageNum = 0, reset = true) => {
    try {
      setLoading(pageNum === 0);
      
      let response;
      if (isChapterComment) {
        response = await currentService.getChapterComments(storyId, chapterNumber, pageNum, 10);
      } else {
        response = await currentService.getStoryComments(storyId, pageNum, 10);
      }
      
      // Handle wrapped API response
      const commentsData = response.data || response;
      const commentsArray = commentsData.comments || commentsData.content || [];
      
      if (reset) {
        setComments(commentsArray);
      } else {
        setComments(prev => [...prev, ...commentsArray]);
      }
      
      // Handle pagination info from wrapped response
      const hasMoreComments = commentsData.hasNext !== undefined ? 
        commentsData.hasNext : 
        !commentsData.last;
      setHasMore(hasMoreComments);
      setPage(pageNum);
    } catch (error) {
      console.error('Error loading comments:', error);
      setError('Error loading comments');
    } finally {
      setLoading(false);
    }
  };

  const handleSubmitComment = async (e) => {
    e.preventDefault();
    if (!newComment.trim() || !user) return;

    try {
      setSubmitting(true);
      setError('');
      
      if (isChapterComment) {
        await currentService.createChapterComment(storyId, chapterNumber, newComment.trim());
      } else {
        await currentService.createComment(storyId, newComment.trim());
      }
      
      setNewComment('');
      // Reload comments to show the new one
      await loadComments(0, true);
    } catch (error) {
      console.error('Error submitting comment:', error);
      setError(error.message || 'Error submitting comment');
    } finally {
      setSubmitting(false);
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('Are you sure you want to delete this comment?')) return;

    try {
      if (isChapterComment) {
        await currentService.deleteChapterComment(commentId);
      } else {
        await currentService.deleteComment(commentId);
      }
      setComments(prev => prev.filter(comment => comment.id !== commentId));
    } catch (error) {
      console.error('Error deleting comment:', error);
      setError(error.message || 'Error deleting comment');
    }
  };

  const loadMoreComments = () => {
    if (hasMore && !loading) {
      loadComments(page + 1, false);
    }
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  return (
    <div className="bg-white rounded-lg shadow-lg p-6">
      <h3 className="text-xl font-bold text-gray-800 mb-6">
        {isChapterComment ? 'Chapter Comments' : 'Story Comments'} {comments.length > 0 && `(${comments.length})`}
      </h3>

      {/* Comment Form */}
      {user && (
        <form onSubmit={handleSubmitComment} className="mb-6">
          <div className="flex items-start space-x-3">
            <div className="w-8 h-8 bg-blue-600 rounded-full flex items-center justify-center">
              <span className="text-white text-sm font-medium">
                {user.username[0].toUpperCase()}
              </span>
            </div>
            <div className="flex-1">
              <textarea
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                placeholder={isChapterComment ? 
                  "Share your thoughts about this chapter..." : 
                  "Share your thoughts about this story..."}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
                rows="3"
                maxLength="2000"
                disabled={submitting}
              />
              <div className="flex justify-between items-center mt-2">
                <span className="text-sm text-gray-500">
                  {newComment.length}/2000 characters
                </span>
                <button
                  type="submit"
                  disabled={!newComment.trim() || submitting}
                  className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
                >
                  {submitting ? 'Posting...' : 'Post Comment'}
                </button>
              </div>
            </div>
          </div>
        </form>
      )}

      {/* Error Message */}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      {/* Comments List */}
      {loading && page === 0 ? (
        <div className="flex justify-center py-8">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>
      ) : comments.length === 0 ? (
        <div className="text-center py-8 text-gray-500">
          <div className="text-4xl mb-2">💬</div>
          <p>No comments yet. Be the first to share your thoughts!</p>
        </div>
      ) : (
        <div className="space-y-4">
          {comments.map((comment) => (
            <div key={comment.id} className="border-b border-gray-200 pb-4 last:border-b-0">
              <div className="flex justify-between items-start mb-2">
                <div className="flex items-center space-x-2">
                  <div className="w-6 h-6 bg-gray-600 rounded-full flex items-center justify-center">
                    <span className="text-white text-xs font-medium">
                      {comment.username?.[0]?.toUpperCase() || 'U'}
                    </span>
                  </div>
                  <span className="font-medium text-gray-800">
                    {comment.username || 'Anonymous'}
                  </span>
                  <span className="text-sm text-gray-500">
                    {formatDate(comment.createdAt)}
                  </span>
                </div>
                
                {user && user.username === comment.username && (
                  <button
                    onClick={() => handleDeleteComment(comment.id)}
                    className="text-red-500 hover:text-red-700 text-sm"
                  >
                    Delete
                  </button>
                )}
              </div>
              
              <p className="text-gray-700 whitespace-pre-wrap">
                {comment.content}
              </p>
            </div>
          ))}

          {/* Load More Button */}
          {hasMore && (
            <div className="text-center pt-4">
              <button
                onClick={loadMoreComments}
                disabled={loading}
                className="text-blue-600 hover:text-blue-800 font-medium disabled:text-gray-400"
              >
                {loading ? 'Loading...' : 'Load more comments'}
              </button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}