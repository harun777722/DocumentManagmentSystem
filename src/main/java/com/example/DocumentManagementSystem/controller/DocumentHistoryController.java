package com.example.DocumentManagementSystem.controller;

import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.Service.DocumentHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/document-history")
public class DocumentHistoryController {



    @Autowired
    private DocumentHistoryService documentHistoryService;

    public DocumentHistoryController(DocumentHistoryService documentHistoryService) {
        this.documentHistoryService = documentHistoryService;
    }


    @PostMapping
    public DocumentHistory createHistory(@RequestBody DocumentHistory history) {
        return documentHistoryService.createHistory(history);
    }

    @GetMapping
    public ResponseEntity<List<DocumentHistory>> getAllHistory(){
        return ResponseEntity.ok(documentHistoryService.getAllHistory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentHistory> getHistoryById(@PathVariable("id") Long id){
        return ResponseEntity.ok(documentHistoryService.getHistoryById(id));
    }

    @GetMapping("/{id}/history")
    public ResponseEntity<List<DocumentHistory>> getDocumentHistory(@PathVariable Long id) {
        List<DocumentHistory> historyList = documentHistoryService.getDocumentHistory(id);
        return ResponseEntity.ok(historyList);
    }
}
