import '../App.css';
import { Link } from 'react-router-dom';

function HomePage() {
  return (<div className="min-h-screen bg-gradient-to-br from-blue-900 via-purple-900 to-pink-800 flex items-center justify-center">
      <div className="text-center space-y-8">
        <h1 className="text-5xl font-bold text-white mb-8 animate-fade-in">
          Writook - Página Principal
        </h1>
      </div>
    </div>
  );
}

export default HomePage;