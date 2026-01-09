package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vol")
public class Vol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vol")
    private Long id;

    @Column(name = "numero_vol", length = 20, nullable = false, unique = true)
    private String numeroVol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeroport_origine", nullable = false)
    private Aeroport aeroportOrigine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aeroport_destination", nullable = false)
    private Aeroport aeroportDestination;

    @OneToMany(mappedBy = "vol", fetch = FetchType.LAZY)
    private List<PrixVolClasse> prixParClasse = new ArrayList<>();

    @OneToMany(mappedBy = "vol", fetch = FetchType.LAZY)
    private List<VolInstance> instances = new ArrayList<>();

    public Vol() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroVol() {
        return numeroVol;
    }

    public void setNumeroVol(String numeroVol) {
        this.numeroVol = numeroVol;
    }

    public Aeroport getAeroportOrigine() {
        return aeroportOrigine;
    }

    public void setAeroportOrigine(Aeroport aeroportOrigine) {
        this.aeroportOrigine = aeroportOrigine;
    }

    public Aeroport getAeroportDestination() {
        return aeroportDestination;
    }

    public void setAeroportDestination(Aeroport aeroportDestination) {
        this.aeroportDestination = aeroportDestination;
    }

    public List<PrixVolClasse> getPrixParClasse() {
        return prixParClasse;
    }

    public void setPrixParClasse(List<PrixVolClasse> prixParClasse) {
        this.prixParClasse = prixParClasse;
    }

    public List<VolInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<VolInstance> instances) {
        this.instances = instances;
    }
}