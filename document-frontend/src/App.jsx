import { useState, useEffect } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const [steps, setSteps] = useState([]);
  const [myDocuments, setMyDocuments] = useState([]);

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/api/auth/authenticate', {
        email: email,
        password: password
      });
      localStorage.setItem('token', response.data.token || response.data.accessToken);
      setIsLoggedIn(true);
    } catch (error) {
      console.error("Giriş hatası:", error);
      alert("Giriş başarısız oldu. Lütfen bilgilerinizi kontrol edin.");
    }
  };

  const fetchApprovalSteps = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/approval-step', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setSteps(response.data);
    } catch (error) {
      console.error("Onay adımları çekilirken hata oluştu:", error);
    }
  };


  const fetchMyDocuments = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/documents/my-documents', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setMyDocuments(response.data);
    } catch (error) {
      console.error("Belgelerim çekilirken hata oluştu:", error);
    }
  };

  const handleApprove = async (stepId) => {
    try {
      const token = localStorage.getItem('token');
      await axios.post(`http://localhost:8080/api/approval-step/${stepId}/approve`,
        { comment: "Uygun bulunarak onaylanmıştır." },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert("Belge başarıyla onaylandı!");
      fetchApprovalSteps();
      fetchMyDocuments();
    } catch (error) {
      console.error("Onay hatası:", error);
      alert("Onaylama sırasında bir hata oluştu. Kendi adımınız olduğundan emin olun.");
    }
  };

  const handleReject = async (stepId) => {
    const reason = window.prompt("Lütfen ret sebebini giriniz:");
    if (!reason) return;

    try {
      const token = localStorage.getItem('token');
      await axios.post(`http://localhost:8080/api/approval-step/${stepId}/reject`,
        { comment: reason },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      alert("Belge reddedildi.");
      fetchApprovalSteps();
      fetchMyDocuments();
    } catch (error) {
      console.error("Ret hatası:", error);
      alert("Reddetme sırasında bir hata oluştu.");
    }
  };

    useEffect(() => {
    if (isLoggedIn) {
      fetchApprovalSteps();
      fetchMyDocuments();
    }
  }, [isLoggedIn]);

  if (isLoggedIn) {
    return (
      <div style={{ padding: '50px', fontFamily: 'Arial', maxWidth: '1000px', margin: '0 auto' }}>
        <h2 style={{ textAlign: 'center' }}>Belge Yönetim Paneli </h2>

        {}
        <h3> Bana Onaya Gelen Belgeler</h3>
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
          <thead>
            <tr style={{ backgroundColor: '#333', color: 'white' }}>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Adım ID</th>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Belge Başlığı</th>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Durum</th>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>İşlem</th>
            </tr>
          </thead>
          <tbody>
            {steps.length === 0 ? (
              <tr>
                <td colSpan="4" style={{ textAlign: 'center', padding: '20px' }}>Şu an onay bekleyen belge bulunmuyor...</td>
              </tr>
            ) : (
              steps.map((step) => (
                <tr key={step.id}>
                  <td style={{ border: '1px solid #ddd', padding: '10px', textAlign: 'center' }}>{step.id}</td>
                  <td style={{ border: '1px solid #ddd', padding: '10px' }}>
                    {step.document ? step.document.title : 'Belge detayı yok'}
                  </td>
                  <td style={{ border: '1px solid #ddd', padding: '10px', textAlign: 'center', fontWeight: 'bold' }}>
                    {step.status}
                  </td>
                  <td style={{ border: '1px solid #ddd', padding: '10px', textAlign: 'center' }}>
                    <button onClick={() => handleApprove(step.id)} style={{ marginRight: '10px', backgroundColor: '#28a745', color: 'white', padding: '8px 15px', border: 'none', cursor: 'pointer', borderRadius: '4px' }}>Onayla</button>
                    <button onClick={() => handleReject(step.id)} style={{ backgroundColor: '#dc3545', color: 'white', padding: '8px 15px', border: 'none', cursor: 'pointer', borderRadius: '4px' }}>Reddet</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        <hr style={{ margin: '40px 0', border: '1px solid #ccc' }} />

        <h2> Benim Yüklediğim Belgeler</h2>
        <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '10px' }}>
          <thead>
            <tr style={{ backgroundColor: '#0056b3', color: 'white' }}>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Belge ID</th>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Başlık</th>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Durum</th>
              <th style={{ border: '1px solid #ddd', padding: '12px' }}>Son Açıklama / Ret Sebebi</th>
            </tr>
          </thead>
          <tbody>
            {myDocuments.length === 0 ? (
              <tr>
                <td colSpan="4" style={{ textAlign: 'center', padding: '20px' }}>Henüz sisteme yüklediğiniz bir belge bulunmuyor.</td>
              </tr>
            ) : (
              myDocuments.map((doc) => {


                let mesaj = "-";
                if (doc.histories && doc.histories.length > 0) {
                    mesaj = doc.histories[doc.histories.length - 1].description || "-";
                } else if (doc.documentHistory && doc.documentHistory.length > 0) {
                    mesaj = doc.documentHistory[doc.documentHistory.length - 1].description || "-";
                }

                return (
                  <tr key={doc.id}>
                    <td style={{ border: '1px solid #ddd', padding: '10px', textAlign: 'center' }}>{doc.id}</td>
                    <td style={{ border: '1px solid #ddd', padding: '10px' }}>{doc.title}</td>
                    <td style={{ border: '1px solid #ddd', padding: '10px', textAlign: 'center', fontWeight: 'bold',
                        color: doc.status === 'REJECTED' ? 'red' : (doc.status === 'APPROVED' ? 'green' : '#ffc107') }}>
                      {doc.status}
                    </td>
                    <td style={{ border: '1px solid #ddd', padding: '10px' }}>
                       {doc.status === 'REJECTED' ? (
                           <span style={{ color: 'red' }}> {mesaj}</span>
                       ) : (
                           <span>{mesaj}</span>
                       )}
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>

      </div>
    );
  }

  return (
    <div style={{ padding: '50px', textAlign: 'center', fontFamily: 'Arial' }}>
      <h2>Belge Yönetim Sistemi Girişi</h2>
      <form onSubmit={handleLogin} style={{ display: 'flex', flexDirection: 'column', width: '300px', margin: '0 auto', gap: '15px' }}>
        <input type="email" placeholder="E-posta adresiniz" value={email} onChange={(e) => setEmail(e.target.value)} style={{ padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
        <input type="password" placeholder="Şifreniz" value={password} onChange={(e) => setPassword(e.target.value)} style={{ padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }} />
        <button type="submit" style={{ padding: '10px', backgroundColor: '#007bff', color: 'white', border: 'none', cursor: 'pointer', borderRadius: '4px', fontWeight: 'bold' }}>Giriş Yap</button>
      </form>
    </div>
  );
}

export default App;

bana bu yazdığımız frontend kodunu açıklamanı istiyorum tek tek nerede ne yaptık