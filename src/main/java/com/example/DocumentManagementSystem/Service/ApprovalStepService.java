package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.ApprovalStep;
import com.example.DocumentManagementSystem.entity.Document;
import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.repository.ApprovalStepRepository;
import com.example.DocumentManagementSystem.repository.DocumentHistoryRepository;
import com.example.DocumentManagementSystem.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApprovalStepService {

    @Autowired
    private ApprovalStepRepository approvalStepRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    public List<ApprovalStep> getAllApprovalSteps() {
        return approvalStepRepository.findAll();
    }

    public ApprovalStep createApprovalStep(ApprovalStep approvalStep) {
        return approvalStepRepository.save(approvalStep);
    }

    public ApprovalStep getApprovalStepById(Long id) {
        return approvalStepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Onay adımı bulunamadı!"));
    }


    @Transactional
    public ApprovalStep processApproval(Long stepId, String newStatus, String comment) {

        ApprovalStep step = approvalStepRepository.findById(stepId)
                .orElseThrow(() -> new RuntimeException("Onay adımı bulunamadı!"));

        // 1. Bu adımı güncelle (Veli'nin adımını APPROVED veya REJECTED yap)
        step.setStatus(newStatus);
        ApprovalStep savedStep = approvalStepRepository.save(step);

        Document document = step.getDocument();

        // 2. Karara göre Belgenin ana durumunu belirle
        if ("REJECTED".equalsIgnoreCase(newStatus)) {
            // Biri reddettiyse belge doğrudan reddedilir
            document.setStatus("REJECTED");
        } else if ("APPROVED".equalsIgnoreCase(newStatus)) {
            // Bu belgeye ait hala bekleyen (PENDING) başka bir adım var mı kontrol et
            boolean hasPendingSteps = approvalStepRepository.existsByDocumentIdAndStatus(document.getId(), "PENDING");

            if (hasPendingSteps) {
                // Sırada bekleyen başka onaycılar var (örn. Kemal), belge henüz tam bitmedi
                document.setStatus("PENDING"); // veya sisteminde "PENDING" olarak tutuyorsan "PENDING"
            } else {
                // Başka bekleyen adım kalmadı, son onaycı da onayladı!
                document.setStatus("APPROVED");
            }
        }

        documentRepository.save(document);

        // 3. Tarihçeye kaydet
        DocumentHistory history = new DocumentHistory();
        history.setDocument(document);
        history.setActor(step.getApprover());
        history.setAction(newStatus);
        history.setDescription("Not: " + comment);
        documentHistoryRepository.save(history);

        return savedStep;
    }
}
