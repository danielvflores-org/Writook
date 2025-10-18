import { useState, useRef } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Editor } from '@tinymce/tinymce-react';
import { ENV_CONFIG } from '../config/environment.js';
import Notification from '../components/Notification';
import Layout from '../components/Layout';
import { useNotification } from '../hooks/useNotification';

export default function CreateChapter() {
  const { storyId } = useParams();
  const navigate = useNavigate();
  
  const [content, setContent] = useState('');
  const [title, setTitle] = useState('');
  const [wordCount, setWordCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const { notification, showNotification, hideNotification } = useNotification();
  const editorRef = useRef(null);

  // TinyMCE Properties
  const editorConfig = {
    height: 500,
    menubar: false,
    plugins: [
      'advlist', 'autolink', 'lists', 'link', 'charmap', 'preview',
      'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
      'insertdatetime', 'media', 'table', 'code', 'help', 'wordcount'
    ],
    toolbar: 'undo redo | formatselect | ' +
      'bold italic backcolor | alignleft aligncenter ' +
      'alignright alignjustify | bullist numlist outdent indent | ' +
      'removeformat | help',
    content_style: `
      body { 
        font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif; 
        font-size: 16px; 
        line-height: 1.6; 
        color: #374151;
        max-width: none;
        margin: 0;
        padding: 20px;
      }
      h1, h2, h3 { color: #1f2937; }
      p { margin-bottom: 1em; }
    `,
    skin: 'oxide',
    content_css: 'default',
    branding: false,
    promotion: false,
    statusbar: false,
    tracking: false,
    // Disable premium features when no valid API key
    ...(ENV_CONFIG.TINYMCE_API_KEY === 'no-api-key' && {
      init_instance_callback: function (editor) {
        // This will prevent some errors when no valid API key is present
        console.log('TinyMCE Editor initialized without API key');
      }
    }),
    setup: (editor) => {
      editor.on('keyup', () => {
        const content = editor.getContent({ format: 'text' });
        const words = content.trim().split(/\s+/).length;
        setWordCount(content.trim() === '' ? 0 : words);
      });
    }
  };

  const handleEditorChange = (content, editor) => {
    setContent(content);
    const textContent = editor.getContent({ format: 'text' });
    const words = textContent.trim().split(/\s+/).length;
    setWordCount(textContent.trim() === '' ? 0 : words);
  };

  const handleSave = async () => {
    if (!title.trim()) {
      showNotification('Please enter a chapter title', 'warning');
      return;
    }
    
    if (!content.trim() || content === '<p><br></p>') {
      showNotification('Please write some content for the chapter', 'warning');
      return;
    }

    setLoading(true);
    try {
      // GET Story to get next chapter number
      const storyResponse = await fetch(`${ENV_CONFIG.API_BASE_URL}/stories/${storyId}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        }
      });
      
      if (!storyResponse.ok) throw new Error('No se pudo cargar la historia');
      
      const storyData = await storyResponse.json();
      const nextChapterNumber = storyData.chapters.length + 1;

      const chapterData = {
        title: title.trim(),
        content: content,
        number: nextChapterNumber
      };

      // Use new POST endpoint to add chapter
      const response = await fetch(`${ENV_CONFIG.API_BASE_URL}/stories/${storyId}/chapters`, {
        method: 'POST',
        headers: { 
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('authToken')}`
        },
        body: JSON.stringify(chapterData)
      });

      if (response.ok) {
        showNotification('Chapter created successfully! 📚', 'success');
        navigate(`/myworks/${storyId}`);
      } else {
        const errorData = await response.text();
        throw new Error(errorData || 'Server response error');
      }
      
    } catch (error) {
      console.error('Error creating chapter:', error);
      showNotification(`Error creating chapter: ${error.message}`, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate(`/myworks/${storyId}`);
  };

  const insertQuickText = (text) => {
    if (editorRef.current) {
      editorRef.current.insertContent(text);
    }
  };

  return (
    <Layout>
      <Notification
        notification={notification}
        onClose={hideNotification}
      />
      <div>
        <div className="mb-8">
          <button
            onClick={handleCancel}
            className="flex items-center text-blue-600 hover:text-blue-800 mb-4"
          >
            ← Back to Story
          </button>
          <h1 className="text-3xl font-bold text-gray-800 mb-2">Create New Chapter</h1>
          <p className="text-gray-600">Add a new chapter to your story</p>
        </div>

        <div className="bg-white rounded-xl shadow-lg p-8">
          <div className="mb-6">
            <input
              type="text"
              placeholder="Chapter title..."
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full text-xl font-semibold border-b-2 border-gray-200 focus:border-blue-500 outline-none pb-2"
            />
          </div>

          <div className="mb-6">
            <Editor
              apiKey={ENV_CONFIG.TINYMCE_API_KEY === 'no-api-key' ? undefined : ENV_CONFIG.TINYMCE_API_KEY}
              onInit={(evt, editor) => editorRef.current = editor}
              value={content}
              onEditorChange={handleEditorChange}
              init={editorConfig}
            />
          </div>

          <div className="flex justify-between items-center">
            <div className="text-sm text-gray-500">
              Words: {wordCount} | Reading time: ~{Math.ceil(wordCount / 200)} min
            </div>
            <div className="flex space-x-3">
              <button
                onClick={handleCancel}
                className="px-6 py-2 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50"
              >
                Cancel
              </button>
              <button
                onClick={handleSave}
                disabled={loading}
                className="px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium disabled:opacity-50"
              >
                {loading ? '⏳ Creating...' : '💾 Create Chapter'}
              </button>
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
}
