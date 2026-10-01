import { useState, useEffect } from 'react';
import axios from 'axios';

function Dashboard({ userRole }) {
  const [steps, setSteps] = useState([]);
  const [myDocuments, setMyDocuments] = useState([]);
  const [uploadFile, setUploadFile] = useState(null);
  const [uploadTitle, setUploadTitle] = useState('');

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

  const fetchAllStepsAdmin = async () => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get('http://localhost:8080/api/approval-step/all', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setSteps(response.data);
      alert("Sistemdeki tüm belgeler başarıyla getirildi!");
    } catch (error) {
      console.error("Tüm belgeler çekilirken hata oluştu:", error);
      alert("Bu işlemi yapmaya yetkiniz yok veya bir hata oluştu.");
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

  const handleUpload = async (e) => {
    e.preventDefault();
    if (!uploadFile || !uploadTitle) {
      alert("Lütfen bir dosya seçin ve başlık girin!");
      return;
    }

    const formData = new FormData();
    formData.append('file', uploadFile);
    formData.append('title', uploadTitle);

    try {
      const token = localStorage.getItem('token');
      await axios.post('http://localhost:8080/api/documents', formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      });
      alert("Belge başarıyla yüklendi!");
      setUploadFile(null);
      setUploadTitle('');
      fetchMyDocuments();
    } catch (error) {
      console.error("Belge yükleme hatası:", error);
      alert("Yükleme sırasında bir hata oluştu.");
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
      alert("Onaylama sırasında bir hata oluştu.");
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
    fetchApprovalSteps();
    fetchMyDocuments();
  }, []);

  return (
    <div style={{ padding: '40px 20px', fontFamily: 'Segoe UI, Tahoma, Geneva, Verdana, sans-serif', maxWidth: '1100px', margin: '0 auto', color: '#e0e0e0', backgroundColor: '#121212', minHeight: '100vh' }}>
      <h2 style={{ textAlign: 'center', color: '#ffffff', marginBottom: '30px', letterSpacing: '1px' }}>Belge Yönetim Paneli</h2>

      {userRole === 'ADMIN' && (
        <div style={{ marginBottom: '20px', textAlign: 'left' }}>
          <button
            onClick={fetchAllStepsAdmin}
            style={{ backgroundColor: '#00adb5', color: 'white', padding: '10px 20px', border: 'none', cursor: 'pointer', borderRadius: '6px', fontWeight: 'bold', boxShadow: '0 4px 6px rgba(0,0,0,0.3)' }}>
            Tüm Sistemdeki Belgeleri Gör (Admin)
          </button>
        </div>
      )}

      <div style={{ backgroundColor: '#1e1e1e', padding: '25px', borderRadius: '10px', marginBottom: '35px', border: '1px solid #333', boxShadow: '0 4px 12px rgba(0,0,0,0.5)' }}>
        <h4 style={{ margin: '0 0 15px 0', color: '#00adb5', fontSize: '18px' }}> Yeni Belge Yükle</h4>
        <form onSubmit={handleUpload} style={{ display: 'flex', gap: '15px', alignItems: 'center', flexWrap: 'wrap' }}>
          <input
            type="text"
            placeholder="Belge Başlığı"
            value={uploadTitle}
            onChange={(e) => setUploadTitle(e.target.value)}
            style={{ padding: '10px 14px', borderRadius: '6px', border: '1px solid #444', backgroundColor: '#2a2a2a', color: 'white', flex: '1', minWidth: '200px', outline: 'none' }}
          />
          <input
            type="file"
            onChange={(e) => setUploadFile(e.target.files[0])}
            style={{ color: '#aaa', fontSize: '14px' }}
          />
          <button type="submit" style={{ backgroundColor: '#00adb5', color: 'white', padding: '10px 22px', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: 'bold', transition: 'background 0.2s' }}>
            Yükle
          </button>
        </form>
      </div>

      <h3 style={{ color: '#fff', borderBottom: '2px solid #333', paddingBottom: '8px' }}>Bana Onaya Gelen Belgeler</h3>
      <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '15px', backgroundColor: '#1e1e1e', borderRadius: '8px', overflow: 'hidden', boxShadow: '0 4px 12px rgba(0,0,0,0.3)' }}>
        <thead>
          <tr style={{ backgroundColor: '#252525', color: '#00adb5', textAlign: 'left' }}>
            <th style={{ padding: '14px', borderBottom: '1px solid #333' }}>Adım ID</th>
            <th style={{ padding: '14px', borderBottom: '1px solid #333' }}>Belge Başlığı</th>
            <th style={{ padding: '14px', borderBottom: '1px solid #333', textAlign: 'center' }}>Durum</th>
            <th style={{ padding: '14px', borderBottom: '1px solid #333', textAlign: 'center' }}>İşlem</th>
          </tr>
        </thead>
        <tbody>
          {steps.length === 0 ? (
            <tr>
              <td colSpan="4" style={{ textAlign: 'center', padding: '25px', color: '#777' }}>Şu an gösterilecek belge bulunmuyor...</td>
            </tr>
          ) : (
            steps.map((step) => (
              <tr key={step.id} style={{ borderBottom: '1px solid #2a2a2a' }}>
                <td style={{ padding: '12px 14px', textAlign: 'center', color: '#aaa' }}>{step.id}</td>
                <td style={{ padding: '12px 14px', color: '#fff' }}>
                  {step.document ? step.document.title : 'Belge detayı yok'}
                </td>
                <td style={{ padding: '12px 14px', textAlign: 'center', fontWeight: 'bold', color: '#ffc107' }}>
                  {step.status}
                </td>
                <td style={{ padding: '12px 14px', textAlign: 'center' }}>
                  <button onClick={() => handleApprove(step.id)} style={{ marginRight: '10px', backgroundColor: '#28a745', color: 'white', padding: '7px 14px', border: 'none', cursor: 'pointer', borderRadius: '4px', fontWeight: 'bold' }}>Onayla</button>
                  <button onClick={() => handleReject(step.id)} style={{ backgroundColor: '#dc3545', color: 'white', padding: '7px 14px', border: 'none', cursor: 'pointer', borderRadius: '4px', fontWeight: 'bold' }}>Reddet</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <div style={{ margin: '50px 0' }}></div>

      <h3 style={{ color: '#fff', borderBottom: '2px solid #333', paddingBottom: '8px' }}>Benim Yüklediğim Belgeler</h3>
      <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '15px', backgroundColor: '#1e1e1e', borderRadius: '8px', overflow: 'hidden', boxShadow: '0 4px 12px rgba(0,0,0,0.3)' }}>
        <thead>
          <tr style={{ backgroundColor: '#252525', color: '#00adb5', textAlign: 'left' }}>
            <th style={{ padding: '14px', borderBottom: '1px solid #333', textAlign: 'center' }}>Belge ID</th>
            <th style={{ padding: '14px', borderBottom: '1px solid #333' }}>Başlık</th>
            <th style={{ padding: '14px', borderBottom: '1px solid #333', textAlign: 'center' }}>Durum</th>
            <th style={{ padding: '14px', borderBottom: '1px solid #333' }}>Son Açıklama / Ret Sebebi</th>
          </tr>
        </thead>
        <tbody>
          {myDocuments.length === 0 ? (
            <tr>
              <td colSpan="4" style={{ textAlign: 'center', padding: '25px', color: '#777' }}>Henüz sisteme yüklediğiniz bir belge bulunmuyor.</td>
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
                <tr key={doc.id} style={{ borderBottom: '1px solid #2a2a2a' }}>
                  <td style={{ padding: '12px 14px', textAlign: 'center', color: '#aaa' }}>{doc.id}</td>
                  <td style={{ padding: '12px 14px', color: '#fff' }}>{doc.title}</td>
                  <td style={{ padding: '12px 14px', textAlign: 'center', fontWeight: 'bold',
                      color: doc.status === 'REJECTED' ? '#ff6b6b' : (doc.status === 'APPROVED' ? '#51cf66' : '#ffc107') }}>
                    {doc.status}
                  </td>
                  <td style={{ padding: '12px 14px', color: '#ccc' }}>
                     {doc.status === 'REJECTED' ? (
                         <span style={{ color: '#ff6b6b' }}>{mesaj}</span>
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

export default Dashboard;