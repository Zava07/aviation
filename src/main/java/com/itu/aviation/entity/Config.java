package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_config")
    private Long id;

    @Column(name = "point_fidelite", nullable = false)
    private Integer pointFidelite;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    public Config() {
    }

    @PrePersist
    @PreUpdate
    public void updateDateModification() {
        this.dateModification = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPointFidelite() {
        return pointFidelite;
    }

    public void setPointFidelite(Integer pointFidelite) {
        this.pointFidelite = pointFidelite;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }
}
