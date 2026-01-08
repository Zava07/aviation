package com.itu.aviation.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modele_avion")
public class ModeleAvion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modele_avion")
    private Long id;

    @Column(name = "nom_modele", length = 50, nullable = false)
    private String nomModele;

    @OneToMany(mappedBy = "modeleAvion", fetch = FetchType.LAZY)
    private List<Avion> avions = new ArrayList<>();

    public ModeleAvion() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomModele() {
        return nomModele;
    }

    public void setNomModele(String nomModele) {
        this.nomModele = nomModele;
    }

    public List<Avion> getAvions() {
        return avions;
    }

    public void setAvions(List<Avion> avions) {
        this.avions = avions;
    }
}