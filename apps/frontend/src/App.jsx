import './App.css';

function App() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 via-purple-900 to-pink-800 flex items-center justify-center">
      <div className="text-center space-y-8">
        <h1 className="text-5xl font-bold text-white mb-8 animate-fade-in">
          Writook
        </h1>
        
        {/* Botón principal con animación */}
        <button className="group relative px-8 py-4 bg-gradient-to-r from-purple-600 to-pink-600 text-white font-semibold rounded-lg shadow-lg transform transition-all duration-300 hover:scale-105 hover:shadow-2xl hover:from-purple-500 hover:to-pink-500 active:scale-95">
          <span className="relative z-10">Comenzar a Escribir</span>
          
          {/* Efecto de brillo */}
          <div className="absolute inset-0 bg-gradient-to-r from-transparent via-white to-transparent opacity-0 group-hover:opacity-20 transform -skew-x-12 group-hover:animate-shine rounded-lg"></div>
          
          {/* Ripple effect */}
          <div className="absolute inset-0 rounded-lg overflow-hidden">
            <div className="absolute inset-0 transform scale-0 group-active:scale-100 transition-transform duration-200 bg-white opacity-20 rounded-lg"></div>
          </div>
        </button>

        {/* Botón secundario */}
        <div className="mt-6">
          <button className="px-6 py-3 border-2 border-white text-white font-medium rounded-lg transition-all duration-300 hover:bg-white hover:text-purple-900 transform hover:scale-105 active:scale-95">
            Explorar Historias
          </button>
        </div>

        {/* Texto con animación */}
        <p className="text-gray-300 animate-pulse mt-8">
          Tu próxima gran historia comienza aquí
        </p>
      </div>
    </div>
  );
}

export default App;
