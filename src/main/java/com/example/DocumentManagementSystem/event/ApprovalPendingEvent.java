package com.example.DocumentManagementSystem.event;

import com.example.DocumentManagementSystem.entity.ApprovalStep;

public class ApprovalPendingEvent {

    private final ApprovalStep approvalStep;

    public ApprovalPendingEvent(ApprovalStep approvalStep) {
        this.approvalStep = approvalStep;
    }

    public ApprovalStep getApprovalStep() {
        return approvalStep;
    }
}
