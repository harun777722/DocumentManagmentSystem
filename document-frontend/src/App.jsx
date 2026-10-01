import { useState } from 'react';
import './App.css';
import Register from './Register';
import Login from './Login';
import Dashboard from './Dashboard';

function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isRegistering, setIsRegistering] = useState(false);
  const [userRole, setUserRole] = useState('USER');

  // 1. Durum: Kayıt Ekranı
  if (isRegistering) {
    return (
      <div style={{ backgroundColor: '#121212', minHeight: '100vh', position: 'relative' }}>
        <button
          onClick={() => setIsRegistering(false)}
          style={{ position: 'absolute', top: '20px', left: '20px', padding: '10px 20px', backgroundColor: '#333', color: 'white', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }}>
          ⬅ Giriş Ekranına Dön
        </button>
        <Register />
      </div>
    );
  }

  // 2. Durum: Ana Panel (Giriş yapıldıysa)
  if (isLoggedIn) {
    return <Dashboard userRole={userRole} />;
  }

  // 3. Durum: Standart Giriş Ekranı (Login.jsx'e propları gönderiyoruz)
  return (
    <Login
      onLoginSuccess={(role) => {
        setUserRole(role);
        setIsLoggedIn(true);
      }}
      onNavigateRegister={() => setIsRegistering(true)}
    />
  );
}

export default App;