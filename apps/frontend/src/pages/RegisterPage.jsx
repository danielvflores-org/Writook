import { Link } from "react-router-dom";
import RegisterForm from "../components/RegisterForm";

function RegisterPage() {
  const handleRegister = async ({ username, email, password }) => {
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username,
          email,
          password,
          displayName: username,
        }),
      });

      const data = await response.json();

      if (response.ok) {
        console.log("Registro exitoso:", data);
        alert("Registro exitoso! Puedes iniciar sesión ahora.");
      } else {
        console.error("Error en registro:", data);
        const errorMessage = data.error || data.message || "Error desconocido";
        alert("Error en el registro: " + errorMessage);
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
          <h1 className="text-3xl font-bold text-white mb-2">Crear Cuenta</h1>
          <p className="text-gray-300">Únete a la comunidad de Writook</p>
        </div>

        <RegisterForm onSubmit={handleRegister} />

        <div className="mt-6 text-center">
          <p className="text-gray-300">
            ¿Ya tienes una cuenta?{" "}
            <Link
              to="/login"
              className="text-pink-400 hover:text-pink-300 font-medium transition-colors"
            >
              Inicia sesión aquí
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

export default RegisterPage;
