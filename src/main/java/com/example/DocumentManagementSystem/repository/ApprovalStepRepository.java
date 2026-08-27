package com.example.DocumentManagementSystem.repository;

import com.example.DocumentManagementSystem.entity.ApprovalStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApprovalStepRepository  extends JpaRepository<ApprovalStep , Long>{

    List<ApprovalStep> findByApproverIdAndStatus(Long approverId, String status);

    boolean existsByDocumentIdAndStatus(Long documentId, String status);

}
