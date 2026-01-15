package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fidelite_passager")
public class FidelitePassager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fidelite_passager")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_passager", nullable = false)
    private Passager passager;

    @Column(name = "points", nullable = false)
    private Integer points;

    @Column(name = "date_obtention")
    private LocalDateTime dateObtention;

    public FidelitePassager() {
    }

    @PrePersist
    public void prePersist() {
        if (this.dateObtention == null) {
            this.dateObtention = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passager getPassager() {
        return passager;
    }

    public void setPassager(Passager passager) {
        this.passager = passager;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public LocalDateTime getDateObtention() {
        return dateObtention;
    }

    public void setDateObtention(LocalDateTime dateObtention) {
        this.dateObtention = dateObtention;
    }
}
