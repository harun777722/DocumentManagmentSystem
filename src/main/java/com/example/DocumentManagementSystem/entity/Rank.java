package com.example.DocumentManagementSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "ranks")
@JsonIgnoreProperties({"hibernateLazyInitializer" , "handler"})
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer rutbe;


    public Rank() {
    }

    public Rank(Long id, Integer rutbe, String name) {
        this.id = id;
        this.rutbe = rutbe;
        this.name = name;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRutbe() {
        return rutbe;
    }
    public void setRutbe(Integer rutbe) {
        this.rutbe = rutbe;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
