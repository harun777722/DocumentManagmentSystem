package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.repository.DocumentHistoryRepository;
import com.example.DocumentManagementSystem.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service

public class DocumentHistoryService {

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    private DocumentRepository documentRepository;

    public DocumentHistoryService(DocumentHistoryRepository documentHistoryRepository,
                                  DocumentRepository documentRepository) {
        this.documentHistoryRepository = documentHistoryRepository;
        this.documentRepository = documentRepository;
    }

    public List<DocumentHistory> getAllHistory() {
        return documentHistoryRepository.findAll();
    }

    public DocumentHistory createHistory(DocumentHistory history) {
        return documentHistoryRepository.save(history);
    }

    public DocumentHistory getHistoryById(Long id){
        return documentHistoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Geçmiş kaydı bulunamadı"));

    }
    public List<DocumentHistory> getDocumentHistory(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new RuntimeException("Belge bulunamadı! ID: " + documentId);
        }

        return documentHistoryRepository.findByDocumentIdOrderByTimeStampDesc(documentId);
    }
}
