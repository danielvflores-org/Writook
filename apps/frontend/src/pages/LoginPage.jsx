import { Link, useNavigate } from "react-router-dom";
import LoginForm from "../components/LoginForm";
import useAuth from "../config/AuthContext.js";

function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async ({ email, password }) => {
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: email,
          password: password,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        console.log("Login exitoso:", data);
        console.log("Token recibido:", data.data.token);
        
        login(data.data.token, data.data.user || { username: email });
        
        alert("Login exitoso!");
        window.location.href = "/home";
      } else {
        console.error("Error en login:", data);
        const errorMessage = data.error || data.message || "Error desconocido";
        alert("Error en el login: " + errorMessage);
      }
    } catch (error) {
      console.error("Error de conexión:", error);
      alert("Error de conexión. Verifica que el backend esté corriendo.");
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-900 via-purple-900 to-pink-800 flex items-center justify-center">
      <div className="bg-white bg-opacity-10 backdrop-blur-lg rounded-xl p-8 shadow-2xl w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-white mb-2">Iniciar Sesión</h1>
          <p className="text-gray-300">Bienvenido de vuelta a Writook</p>
        </div>

        <LoginForm onSubmit={handleLogin} />

        <div className="mt-6 text-center">
          <p className="text-gray-300">
            ¿No tienes una cuenta?{" "}
            <Link
              to="/register"
              className="text-pink-400 hover:text-pink-300 font-medium transition-colors"
            >
              Regístrate aquí
            </Link>
          </p>
        </div>

        <div className="mt-4 text-center">
          <Link
            to="/"
            className="text-gray-400 hover:text-gray-300 text-sm transition-colors"
          >
            ← Volver al inicio
          </Link>
        </div>
      </div>
    </div>
  );
}

export default LoginPage;
