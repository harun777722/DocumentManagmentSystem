package com.example.DocumentManagementSystem.controller;

import com.example.DocumentManagementSystem.Service.ApprovalStepService;
import com.example.DocumentManagementSystem.Service.ApprovalWorkflowService;
import com.example.DocumentManagementSystem.entity.ApprovalStep;
import com.example.DocumentManagementSystem.repository.ApprovalStepRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/approval-step")
public class ApprovalStepController {

    private final ApprovalStepService stepService;
    private final ApprovalStepRepository stepRepository;
    private final ApprovalWorkflowService approvalWorkflowService;

    public ApprovalStepController(ApprovalStepService stepService,ApprovalWorkflowService approvalWorkflowService ,ApprovalStepRepository stepRepository) {
        this.stepService = stepService;
        this.stepRepository = stepRepository;
        this.approvalWorkflowService = approvalWorkflowService;
    }

    @PostMapping("/{stepId}/approve")
    public ResponseEntity<Map<String, Object>> approveStep(
            @PathVariable Long stepId,
            @RequestBody(required = false) Map<String, String> requestBody) {

        System.out.println("\n--- 1. İSTEK CONTROLLER'A ULAŞTI. Step ID: " + stepId + " ---");

        try {
            String comment = "Sebep belirtilmedi.";
            if (requestBody != null && requestBody.containsKey("comment")) {
                comment = requestBody.get("comment");
            }

            System.out.println("--- 2. VERİTABANINDAN ADIM ARANIYOR... ---");
            ApprovalStep step = stepRepository.findById(stepId)
                    .orElseThrow(() -> new RuntimeException("Onay adımı bulunamadı!"));

            System.out.println("--- 3. ADIM BULUNDU! WORKFLOW SERVICE ÇAĞRILIYOR... ---");
            approvalWorkflowService.processApproval(step, comment);

            System.out.println("--- 4. WORKFLOW SERVICE İŞLEMİ BAŞARIYLA BİTİRDİ! ---");

            String mesaj;
            String guncelStatus = step.getDocument().getStatus();

            if ("APPROVED".equals(guncelStatus)) {
                mesaj = "Belge başarıyla onaylandı. Onay zinciri tamamlandı!";
            } else {
                mesaj = "Belge onaylandı ve bir sonraki onaya iletildi.";
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", mesaj);
            response.put("documentId", step.getDocument().getId());
            response.put("status", guncelStatus);
            response.put("yoneticiNotu", comment);

            System.out.println("--- 5. YANIT BAŞARIYLA DÖNÜLÜYOR ---");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println("!!! SİSTEM BİR HATA YAKALADI !!!");
            System.out.println("Hata Mesajı: " + e.getMessage());
            e.printStackTrace(); // Bu komut hatanın hangi satırda olduğunu gösterecek
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n");

            throw e; // Hatayı tekrar fırlat ki süreç yarım kalmasın
        }
    }

    @PostMapping("/{stepId}/reject")
    public ResponseEntity<Map<String, Object>> rejectStep(
            @PathVariable Long stepId,
            @RequestBody(required = false) Map<String, String> requestBody) {

        // 1. Postman'den gelen 'comment' verisini al, yoksa varsayılan metni koy
        String comment = "Sebep belirtilmedi.";
        if (requestBody != null && requestBody.containsKey("comment")) {
            comment = requestBody.get("comment");
        }

        // 2. Service metodunu "REJECTED" statüsü ve aldığımız sebep (comment) ile çağırıyoruz
        ApprovalStep step = stepService.processApproval(stepId, "REJECTED", comment);

        // 3. Postman'e dönecek yanıtı hazırlıyoruz
        String mesaj = "Belge reddedildi ve sahibine iade edildi.";

        Map<String, Object> response = new HashMap<>();
        response.put("message", mesaj);
        response.put("documentId", step.getDocument().getId());
        response.put("status", step.getDocument().getStatus());
        response.put("redSebebi", comment); // Postman'de ret sebebini de görelim

        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<ApprovalStep>> getMyPendingSteps() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ApprovalStep> mySteps = stepRepository.findByApproverEmailAndStatus(currentUserEmail, "PENDING");
        return ResponseEntity.ok(mySteps);
    }

    // 2. ADMİNLER VE TESTLER İÇİN (Eski kodunu koruduk, sadece adresine "/all" ekledik)
    @GetMapping("/all")
    public ResponseEntity<List<ApprovalStep>> getAllSteps() {
        // Veritabanındaki tüm onay adımlarını çekip gönderiyoruz
        List<ApprovalStep> steps = stepRepository.findAll();
        return ResponseEntity.ok(steps);
    }
}