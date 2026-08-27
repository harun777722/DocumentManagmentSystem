package com.example.DocumentManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    @OneToMany(mappedBy = "document", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("document")
    private List<DocumentHistory> histories;

    public List<DocumentHistory> getHistories() {
        return histories;
    }

    public void setHistories(List<DocumentHistory> histories) {
        this.histories = histories;
    }


    public Document() {
    }

    public Document(Long id, User uploader, String status, String filePath, String title) {
        this.id = id;
        this.uploader = uploader;
        this.status = status;
        this.filePath = filePath;
        this.title = title;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getUploader() {
        return uploader;
    }
    public void setUploader(User uploader) {
        this.uploader = uploader;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}


