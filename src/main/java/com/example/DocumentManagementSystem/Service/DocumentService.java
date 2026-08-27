package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.Document;
import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.entity.ApprovalStep;
import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.repository.DocumentRepository;
import com.example.DocumentManagementSystem.repository.ApprovalStepRepository;
import com.example.DocumentManagementSystem.repository.DocumentHistoryRepository;
import com.example.DocumentManagementSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.PrimitiveIterator;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private ApprovalStepRepository approvalStepRepository;

    @Autowired
    private ApprovalWorkflowService approvalWorkflowService;

    private final String UPLOAD_DIR = "uploads/";
    // dosyaların kaydedileceği yerel klasör yolu

    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    public Document getDocumentById(Long id){
        return documentRepository.findById(id).orElseThrow(()-> new RuntimeException("Belge bulunamadı."));
    }

    public List<Document> getMyDocuments(String email) {
        return documentRepository.findByUploaderEmail(email);
    }

    @Transactional
    public Document creatDocument(MultipartFile file, String title, Long uploaderId) throws IOException {


        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        User uploader = userRepository.findById(uploaderId)
                .orElseThrow(() -> new RuntimeException("Belgeyi yükleyen kullanıcı bulunamadı!"));

        Document document = new Document();
        document.setTitle(title);
        document.setFilePath(filePath.toString());
        document.setStatus("PENDING");
        document.setUploader(uploader);

        Document savedDocument = documentRepository.save(document);


        DocumentHistory history = new DocumentHistory();
        history.setDocument(savedDocument);
        history.setActor(uploader);
        history.setAction("UPLOADED");
        history.setDescription("Belge sisteme başarıyla yüklendi ve onay sürecine girdi.");
        documentHistoryRepository.save(history);




        // Onay sürecini başlatma işini uzman servise devrediyoruz
        approvalWorkflowService.startWorkflow(savedDocument, uploader);

        return savedDocument;
    }
}
