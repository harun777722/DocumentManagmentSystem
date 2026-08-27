package com.example.DocumentManagementSystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documentHistory")
public class DocumentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id" , nullable = false)
    @JsonIgnoreProperties("histories")
    private Document document;

    @Column(nullable = false)
    private String action ;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "actor_id" , nullable = false)
    private User actor;

    @Column(nullable = false,updatable = false)
    private LocalDateTime timeStamp = LocalDateTime.now();

    @Column(nullable = true) // Boş geçilebilir istersen true yap
    private String description;

    public DocumentHistory() {
    }

    public DocumentHistory(Long id, Document document, String action, User actor, LocalDateTime timeStamp, String description) {
        this.id = id;
        this.document = document;
        this.action = action;
        this.actor = actor;
        this.timeStamp = timeStamp;
        this.description = description;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Document getDocument() {
        return document;
    }
    public void setDocument(Document document) {
        this.document = document;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public User getActor() {
        return actor;
    }
    public void setActor(User actor) {
        this.actor = actor;
    }
    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }
    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
