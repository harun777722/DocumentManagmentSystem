import { useState } from 'react';
import axios from 'axios';

function Login({ onLoginSuccess, onNavigateRegister }) {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/authenticate', { email, password });
      localStorage.setItem('token', response.data.token || response.data.accessToken);

      const role = (email.toLowerCase() === 'furkan@test.com' || email.toLowerCase().includes('admin')) ? 'ADMIN' : 'USER';
      onLoginSuccess(role);
    } catch (error) {
      console.error("Giriş hatası:", error);
      alert("Giriş başarısız oldu. Lütfen bilgilerinizi kontrol edin.");
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', backgroundColor: '#121212', fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif' }}>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', width: '340px', padding: '30px', backgroundColor: '#1e1e1e', borderRadius: '10px', boxShadow: '0 8px 24px rgba(0,0,0,0.6)', border: '1px solid #333' }}>
        <h2 style={{ textAlign: 'center', color: '#fff', marginBottom: '25px', fontSize: '22px' }}>Sistem Girişi</h2>
        <input type="email" placeholder="E-posta adresiniz" value={email} onChange={(e) => setEmail(e.target.value)} style={{ padding: '12px', borderRadius: '6px', border: '1px solid #444', backgroundColor: '#2a2a2a', color: 'white', marginBottom: '15px', outline: 'none' }} />
        <input type="password" placeholder="Şifreniz" value={password} onChange={(e) => setPassword(e.target.value)} style={{ padding: '12px', borderRadius: '6px', border: '1px solid #444', backgroundColor: '#2a2a2a', color: 'white', marginBottom: '20px', outline: 'none' }} />
        <button type="submit" style={{ padding: '12px', backgroundColor: '#00adb5', color: 'white', border: 'none', cursor: 'pointer', borderRadius: '6px', fontWeight: 'bold', fontSize: '15px' }}>
          Giriş Yap
        </button>
        <button type="button" onClick={onNavigateRegister} style={{ padding: '12px', backgroundColor: '#333', color: '#fff', border: 'none', cursor: 'pointer', borderRadius: '6px', fontWeight: 'bold', fontSize: '15px', marginTop: '10px' }}>
            Yeni Kayıt Ekle
        </button>
      </form>
    </div>
  );
}

export default Login;