package com.example.DocumentManagementSystem.Service;

import com.example.DocumentManagementSystem.entity.ApprovalStep;
import com.example.DocumentManagementSystem.entity.Document;
import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.event.ApprovalPendingEvent;
import com.example.DocumentManagementSystem.repository.ApprovalStepRepository;
import com.example.DocumentManagementSystem.repository.DocumentHistoryRepository;
import com.example.DocumentManagementSystem.repository.DocumentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApprovalWorkflowService {

    @Autowired
    private DocumentHistoryRepository documentHistoryRepository;

    @Autowired
    private  ApprovalChainResolver chainResolver;
    @Autowired
    private ApprovalStepRepository stepRepository;
    @Autowired
    private  DocumentRepository documentRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public ApprovalWorkflowService(ApprovalChainResolver chainResolver,
                                   ApprovalStepRepository stepRepository,
                                   DocumentRepository documentRepository,
                                   ApplicationEventPublisher eventPublisher,
                                   DocumentHistoryRepository documentHistoryRepository)  {
        this.chainResolver = chainResolver;
        this.stepRepository = stepRepository;
        this.documentRepository = documentRepository;
        this.eventPublisher = eventPublisher;
        this.documentHistoryRepository = documentHistoryRepository;
    }

    public void startWorkflow(Document document, User uploader) {

        Optional<User> firstApprover = chainResolver.findNextApprover(uploader);

        if (firstApprover.isPresent()) {
            ApprovalStep step = new ApprovalStep();
            step.setDocument(document);
            step.setApprover(firstApprover.get());
            step.setStatus("PENDING");
            step.setStepOrder(1);
            stepRepository.save(step);

            eventPublisher.publishEvent(new ApprovalPendingEvent(step));

            document.setStatus("PENDING");
        } else {
            // onay adımı oluşturmaya gerek yok, doğrudan onaylanır.
            document.setStatus("APPROVED");
        }

        documentRepository.save(document);
    }

    // Testlerin patlamaması için bu metodu ApprovalWorkflowService sınıfına ekle:
    public void processApproval(ApprovalStep currentStep) {
        processApproval(currentStep, "Sebep belirtilmedi.");
    }

    public void processApproval(ApprovalStep currentStep , String comment) {

        verifyUserAuthorization(currentStep);

        currentStep.setStatus("APPROVED");
        stepRepository.save(currentStep);

        Document document = currentStep.getDocument();
        User currentApprover = currentStep.getApprover();


        DocumentHistory history = new DocumentHistory();
        history.setDocument(document);
        history.setAction("APPROVED");
        history.setActor(currentApprover);
        history.setDescription("Not : " + comment);
        documentHistoryRepository.save(history);

        Optional<User> nextApprover = chainResolver.findNextApprover(currentApprover);

        if (nextApprover.isPresent()) {
            ApprovalStep nextStep = new ApprovalStep();
            nextStep.setDocument(document);
            nextStep.setApprover(nextApprover.get());
            nextStep.setStatus("PENDING");
            nextStep.setStepOrder(currentStep.getStepOrder() + 1);
            stepRepository.save(nextStep);

            eventPublisher.publishEvent(new ApprovalPendingEvent(nextStep));

        } else {
            document.setStatus("APPROVED");
            documentRepository.save(document);
        }
    }

    public void processRejection(ApprovalStep currentStep ,String description) {

        verifyUserAuthorization(currentStep);

        currentStep.setStatus("REJECTED");
        stepRepository.save(currentStep);

        Document document = currentStep.getDocument();
        document.setStatus("REJECTED");
        documentRepository.save(document);

        DocumentHistory history = new DocumentHistory();
        history.setDocument(document);
        history.setAction("REJECTED");

        history.setActor(currentStep.getApprover());

        history.setDescription(description);

        documentHistoryRepository.save(history);

    }
    private void verifyUserAuthorization(ApprovalStep step) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String stepOwnerEmail = step.getApprover().getEmail();

        if (!currentUserEmail.equals(stepOwnerEmail)) {
            throw new AccessDeniedException("Bu işlem için yetkiniz yok! Sadece kendi adımlarınızı onaylayabilirsiniz.");
        }
    }

}
