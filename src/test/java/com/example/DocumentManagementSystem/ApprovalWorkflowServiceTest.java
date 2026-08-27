package com.example.DocumentManagementSystem;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.DocumentManagementSystem.Service.ApprovalChainResolver;
import com.example.DocumentManagementSystem.entity.ApprovalStep;
import com.example.DocumentManagementSystem.entity.Document;
import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.entity.User;
import com.example.DocumentManagementSystem.repository.ApprovalStepRepository;
import com.example.DocumentManagementSystem.repository.DocumentHistoryRepository;
import com.example.DocumentManagementSystem.repository.DocumentRepository;
import com.example.DocumentManagementSystem.Service.ApprovalWorkflowService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Spring Security Importları eklendi
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ApprovalWorkflowServiceTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @Mock
    private ApprovalStepRepository stepRepository;

    @Mock
    private DocumentHistoryRepository documentHistoryRepository;

    @Mock
    private ApprovalChainResolver approvalChainResolver;

    @InjectMocks
    private ApprovalWorkflowService approvalWorkflowService;

    private Document sampleDocument;
    private User managerUser;
    private ApprovalStep dummyStep;

    @BeforeEach
    void setUp() {
        managerUser = new User();
        managerUser.setId(2L);
        managerUser.setName("Mehmet");

        // 1. EKLEME: Sistemin kontrol ettiği asıl benzersiz alanı (username veya email) dolduruyoruz.
        // (Eğer senin User sınıfında email varsa setEmail("mehmet@ornek.com") olarak değiştir)
        managerUser.setEmail("mehmet@ornek.com");

        sampleDocument = new Document();
        sampleDocument.setId(1L);
        sampleDocument.setTitle("Staj Raporu");
        sampleDocument.setStatus("PENDING");

        dummyStep = new ApprovalStep();
        dummyStep.setDocument(sampleDocument);
        dummyStep.setApprover(managerUser);

        // --- GÜVENLİK (SECURITY) MOCK AYARI ---
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);

        // 2. DEĞİŞİKLİK: Sisteme giriş yapan kişinin kimliği, yukarıdaki user'ın kimliği ile birebir aynı olmalı!
        when(authentication.getName()).thenReturn("mehmet@ornek.com");

        SecurityContextHolder.setContext(securityContext);
    }

    // Her testten sonra sahte oturumu temizliyoruz ki diğer testleri bozmasın
    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("belge en üstteki kişi tarafından onaylandıktan sonra kalıcı APPROVED olmalı")
    void shouldSetDocumentStatusToApprovedWhenNoNextApproverInChain() {
        // Hazırlık
        when(approvalChainResolver.findNextApprover(managerUser)).thenReturn(Optional.empty());

        // Aksiyon (Eğer metodun ek olarak "onaylandı" gibi bir comment parametresi alıyorsa buraya virgülle eklemelisin)
        approvalWorkflowService.processApproval(dummyStep);

        // Doğrulama
        assertEquals("APPROVED", sampleDocument.getStatus(), "Belge statüsü APPROVED olmalıydı!");
        verify(documentRepository, times(1)).save(sampleDocument);
        verify(documentHistoryRepository, times(1)).save(any(DocumentHistory.class));
    }

    @Test
    @DisplayName("Belge reddedildiğinde hem belgenin hem de adımın statüsü REJECTED olmalı")
    void shouldSetStatusToRejectedWhenDocumentIsRejected() {
        // Hazırlık
        String rejectionReason = "Rapor içeriği eksik, tekrar düzenlenmeli.";

        // Aksiyon
        approvalWorkflowService.processRejection(dummyStep, rejectionReason);

        // Doğrulama
        assertEquals("REJECTED", dummyStep.getStatus(), "Onay adımının statüsü REJECTED olmalı!");
        assertEquals("REJECTED", sampleDocument.getStatus(), "Belgenin statüsü REJECTED olmalı!");
        verify(stepRepository, times(1)).save(dummyStep);
        verify(documentRepository, times(1)).save(sampleDocument);
        verify(documentHistoryRepository, times(1)).save(any(DocumentHistory.class));
    }
}