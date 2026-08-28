import React, { useState } from 'react';
import axios from 'axios';

const Register = () => {
    // Form verilerini tutacağımız state'ler
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [departmentId, setDepartmentId] = useState('');
    const [rankId, setRankId] = useState('');

    const [message, setMessage] = useState('');

    // Form gönderildiğinde çalışacak fonksiyon
    const handleRegister = async (e) => {
        e.preventDefault(); // Sayfanın yenilenmesini engeller

        // Spring Boot'un beklediği JSON yapısı (RegisterRequest.java ile birebir aynı)
        const payload = {
            name: name,
            email: email,
            password: password,
            departmentId: parseInt(departmentId),
            rankId: parseInt(rankId)
        };

        try {
            // Backend'e POST isteği atıyoruz
            const response = await axios.post('http://localhost:8080/api/auth/register', payload);

            setMessage('Kayıt işlemi başarıyla tamamlandı! Giriş yapabilirsiniz.');
            // İsteğe bağlı: Kayıt başarılıysa formu temizle
            setName('');
            setEmail('');
            setPassword('');
            setDepartmentId('');
            setRankId('');

        } catch (error) {
            console.error("Kayıt hatası:", error);
            setMessage('Kayıt olurken bir hata oluştu. Lütfen bilgileri kontrol edin.');
        }
    };

    return (
        <div style={{ backgroundColor: '#1e1e1e', color: '#ffffff', minHeight: '100vh', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
            <div style={{ backgroundColor: '#2d2d2d', padding: '30px', borderRadius: '8px', width: '400px', boxShadow: '0 4px 8px rgba(0,0,0,0.2)' }}>
                <h2 style={{ textAlign: 'center', marginBottom: '20px' }}>Yeni Çalışan Kaydı</h2>

                <form onSubmit={handleRegister} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>
                    <div>
                        <label>Ad Soyad:</label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                            style={inputStyle}
                        />
                    </div>

                    <div>
                        <label>E-posta:</label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            required
                            style={inputStyle}
                        />
                    </div>

                    <div>
                        <label>Şifre:</label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            style={inputStyle}
                        />
                    </div>

                    <div>
                        <label>Departman ID:</label>
                        <input
                            type="number"
                            value={departmentId}
                            onChange={(e) => setDepartmentId(e.target.value)}
                            required
                            style={inputStyle}
                        />
                    </div>

                    <div>
                        <label>Rütbe (Rank) ID:</label>
                        <input
                            type="number"
                            value={rankId}
                            onChange={(e) => setRankId(e.target.value)}
                            required
                            style={inputStyle}
                        />
                    </div>

                    <button type="submit" style={buttonStyle}>
                        Sisteme Kayıt Ol
                    </button>
                </form>

                {message && <p style={{ marginTop: '15px', textAlign: 'center', color: message.includes('hata') ? '#ff4d4d' : '#4caf50' }}>{message}</p>}
            </div>
        </div>
    );
};

// Ortak input ve buton stilleri (Koyu tema uyumlu)
const inputStyle = {
    width: '100%',
    padding: '10px',
    marginTop: '5px',
    borderRadius: '4px',
    border: '1px solid #444',
    backgroundColor: '#1e1e1e',
    color: '#fff',
    boxSizing: 'border-box'
};

const buttonStyle = {
    padding: '12px',
    backgroundColor: '#007bff',
    color: 'white',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontWeight: 'bold',
    marginTop: '10px'
};

export default Register;