import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import InitialPage from './pages/InitialPage';
import RegisterPage from './pages/RegisterPage';
import LoginPage from './pages/LoginPage';
import HomePage from './pages/HomePage';
import useAuth from './config/AuthContext.js';
import './App.css';

function App() {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Cargando...</div>;
  }

  return (
    <Router>
      <Routes>
        <Route path="/" element={
          user ? <Navigate to="/home" /> : <InitialPage />
        } />
        <Route path="/login" element={
          user ? <Navigate to="/home" /> : <LoginPage />
        } />
        <Route path="/register" element={
          user ? <Navigate to="/home" /> : <RegisterPage />
        } />
        <Route path="/home" element={
          user ? <HomePage /> : <Navigate to="/" />
        } />
      </Routes>
    </Router>
  );
}
export default App;

