package com.example.DocumentManagementSystem.repository;


import com.example.DocumentManagementSystem.entity.DocumentHistory;
import com.example.DocumentManagementSystem.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentHistoryRepository extends JpaRepository<DocumentHistory , Long> {

    List<DocumentHistory> findByDocumentIdOrderByTimeStampDesc(Long documentId);
}
