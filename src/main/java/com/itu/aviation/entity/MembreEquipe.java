package com.itu.aviation.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "membre_equipe")
public class MembreEquipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membre")
    private Long id;

    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_membre", nullable = false)
    private TypeMembre typeMembre;

    @Column(name = "qualifications", length = 100)
    private String qualifications;

    @Column(name = "heures_vol")
    private Integer heuresVol;

    public MembreEquipe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public TypeMembre getTypeMembre() {
        return typeMembre;
    }

    public void setTypeMembre(TypeMembre typeMembre) {
        this.typeMembre = typeMembre;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public Integer getHeuresVol() {
        return heuresVol;
    }

    public void setHeuresVol(Integer heuresVol) {
        this.heuresVol = heuresVol;
    }
}