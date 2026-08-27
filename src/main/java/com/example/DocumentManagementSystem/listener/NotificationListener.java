package com.example.DocumentManagementSystem.listener;

import com.example.DocumentManagementSystem.event.ApprovalPendingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @Autowired
    private JavaMailSender mailSender;

    @EventListener
    public void handleApprovalPendingEvent(ApprovalPendingEvent event) {
        try {
            String approverEmail = event.getApprovalStep().getApprover().getEmail();
            String documentTitle = event.getApprovalStep().getDocument().getTitle();

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("harunbayrakli194@gmail.com");
            message.setTo(approverEmail);
            message.setSubject("Yeni Belge Onayı Bekliyor: " + documentTitle);
            message.setText("Sayın yetkili,\n\n'" + documentTitle + "' başlıklı belge sistem üzerinden onayınızı beklemektedir.\n\nLütfen sisteme giriş yaparak işlemi tamamlayınız.");

            mailSender.send(message);

            System.out.println("✔️ E-posta başarıyla gönderildi: " + approverEmail);

        } catch (Exception e) {
            System.err.println("❌ E-posta gönderilirken hata oluştu: " + e.getMessage());
        }
    }
}