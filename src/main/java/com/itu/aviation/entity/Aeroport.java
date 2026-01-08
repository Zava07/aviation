package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aeroport")
public class Aeroport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeroport")
    private Long id;

    @Column(name = "nom_aeroport", length = 100, nullable = false)
    private String nomAeroport;

    @OneToMany(mappedBy = "aeroportOrigine", fetch = FetchType.LAZY)
    private List<Vol> volsOrigine = new ArrayList<>();

    @OneToMany(mappedBy = "aeroportDestination", fetch = FetchType.LAZY)
    private List<Vol> volsDestination = new ArrayList<>();

    public Aeroport() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomAeroport() {
        return nomAeroport;
    }

    public void setNomAeroport(String nomAeroport) {
        this.nomAeroport = nomAeroport;
    }

    public List<Vol> getVolsOrigine() {
        return volsOrigine;
    }

    public void setVolsOrigine(List<Vol> volsOrigine) {
        this.volsOrigine = volsOrigine;
    }

    public List<Vol> getVolsDestination() {
        return volsDestination;
    }

    public void setVolsDestination(List<Vol> volsDestination) {
        this.volsDestination = volsDestination;
    }
}