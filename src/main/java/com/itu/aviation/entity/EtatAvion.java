package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "etat_avion")
public class EtatAvion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_etat_avion")
    private Long id;

    @Column(name = "etat", length = 50, nullable = false)
    private String etat;

    @OneToMany(mappedBy = "etatAvion", fetch = FetchType.LAZY)
    private List<Avion> avions = new ArrayList<>();

    public EtatAvion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public List<Avion> getAvions() {
        return avions;
    }

    public void setAvions(List<Avion> avions) {
        this.avions = avions;
    }
}