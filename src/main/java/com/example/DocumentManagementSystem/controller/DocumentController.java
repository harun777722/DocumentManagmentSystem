package com.example.DocumentManagementSystem.controller;

import com.example.DocumentManagementSystem.Service.DocumentService;
import com.example.DocumentManagementSystem.entity.Document;
import com.example.DocumentManagementSystem.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository documentRepository;


    @GetMapping
    public List<Document> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}")
    public Document getDocumentById(@PathVariable("id") Long id){
        return documentService.getDocumentById(id);
    }



    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Document> creatDocument(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("title") String title,
                                                  Authentication authentication) throws IOException {

        // 1. İstek atan kişinin e-postasını token'dan alıyoruz
        String userEmail = authentication.getName();

        // 2. Servis katmanına ID yerine e-postayı gönderiyoruz (veya serviste e-postadan kullanıcıyı buluyoruz)
        Document savedDocument = documentService.creatDocument(file, title,userEmail);

        return ResponseEntity.ok(savedDocument);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        try {
            // 1. Veritabanından belgeyi (Document) bul
             Document document = documentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Belge bulunamadı!"));

             Path filePath = Paths.get(document.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("Fiziksel dosya diskte bulunamadı!");
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                // Eğer tip belirlenemezse varsayılan olarak genel bir byte akışı atıyoruz
                contentType = "application/octet-stream";
            }

            // 4. Dosyayı HTTP başlıklarıyla birlikte indirme (attachment) formatında fırlat
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Document>> filterDocumentByStatus(@RequestParam String status){

        List<Document>filteredDocuments = documentRepository.findByStatus(status.toUpperCase());

        return ResponseEntity.ok(filteredDocuments);
    }

    @GetMapping("/my-documents")
    public ResponseEntity<List<Document>> getMyDocuments(Authentication authentication) {
        String userEmail = authentication.getName();

        List<Document> myDocuments = documentService.getMyDocuments(userEmail);

        return ResponseEntity.ok(myDocuments);
    }


}
