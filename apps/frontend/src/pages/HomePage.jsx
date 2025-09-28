import '../App.css';
import Layout from '../components/Layout';

function HomePage() {
  return (
    <Layout>
      <div>
          <div className="text-center mb-12">
            <div className="flex items-center justify-center space-x-3 mb-4">
              <div className="w-12 h-12 bg-blue-600 rounded-xl flex items-center justify-center">
                <span className="text-white font-bold text-xl">📚</span>
              </div>
              <h1 className="text-4xl font-bold text-gray-900">Welcome to Writook</h1>
            </div>
            <p className="text-xl text-gray-600 max-w-2xl mx-auto">
              Discover amazing stories and share your own creative writing with the world
            </p>
          </div>
          
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="bg-white rounded-xl shadow-lg p-8 text-center hover:shadow-xl transition-shadow">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-3xl">📖</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-800 mb-3">Discover Stories</h3>
              <p className="text-gray-600 leading-relaxed">
                Explore a vast collection of creative stories from writers around the world
              </p>
            </div>
            
            <div className="bg-white rounded-xl shadow-lg p-8 text-center hover:shadow-xl transition-shadow">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-3xl">🔺</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-800 mb-3">Write & Share</h3>
              <p className="text-gray-600 leading-relaxed">
                Create your own stories and share them with a community of passionate readers
              </p>
            </div>
            
            <div className="bg-white rounded-xl shadow-lg p-8 text-center hover:shadow-xl transition-shadow">
              <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
                <span className="text-3xl">⭐</span>
              </div>
              <h3 className="text-xl font-semibold text-gray-800 mb-3">Connect</h3>
              <p className="text-gray-600 leading-relaxed">
                Join a vibrant community of writers and readers who share your passion
              </p>
            </div>
          </div>
        </div>
      </Layout>
  );
}

export default HomePage;