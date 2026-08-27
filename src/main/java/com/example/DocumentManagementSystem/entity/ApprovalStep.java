package com.example.DocumentManagementSystem.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "approve")
public class ApprovalStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approver_id" , nullable = false)
    private User approver;

    @Column(nullable = false , name = "step_order")
    private int stepOrder;

    @Column(nullable = false)
    private String status;

    public ApprovalStep() {
    }

    public ApprovalStep(Long id, String status, User approver, Document document, int stepOrder) {
        this.id = id;
        this.status = status;
        this.approver = approver;
        this.document = document;
        this.stepOrder = stepOrder;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public int getStepOrder() {
        return stepOrder;
    }
    public void setStepOrder(int stepOrder) {
        this.stepOrder = stepOrder;
    }
    public User getApprover() {
        return approver;
    }
    public void setApprover(User approver) {
        this.approver = approver;
    }
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }
}
